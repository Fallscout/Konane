package engine;

import java.util.List;

import game.Board;
import game.Move;

/**
 * This is an standard alpha-beta solver for Konane. It uses transposition
 * tables and move ordering. The solver calculates the winner for black starting
 * and white starting. Afterwards it combines the result and returns the outcome
 * type for the board.
 */
public class AlphaBetaSolver extends Solver {

	/**
	 * Transposition table array.
	 */
	@SuppressWarnings("unchecked")
	private final TTEntry<Integer>[] tTable = new TTEntry[(int) Math.pow(2, 24)];
	/**
	 * Cutoff counter.
	 */
	private int cutoff;

	/**
	 * The main solve method that calls the intern solve methods. The method
	 * combines the outcome for black starting and white starting the game.
	 * 
	 * @return The solver returns the combined outcome type.
	 * @throws The
	 *             solver throws an IllegalStateException because the method
	 *             could not determine a winner.
	 */
	@Override
	public OutcomeType solve(Board board) {
		this.counter = 0;
		this.counterTT = 0;

		int blackOutcome = solve(board, true, -100, 100, 0);
		int whiteOutcome = solve(board, false, -100, 100, 0);

		printCounter();
		resetCounter();

		if (blackOutcome == 1 && whiteOutcome == 1) {
			return OutcomeType.FIRST;
		} else if (blackOutcome == -1 && whiteOutcome == 1) {
			return OutcomeType.WHITE;
		} else if (blackOutcome == 1 && whiteOutcome == -1) {
			return OutcomeType.BLACK;
		} else if (blackOutcome == -1 && whiteOutcome == -1) {
			return OutcomeType.SECOND;
		}

		throw new IllegalStateException("Cannot determine winner.");
	}

	/**
	 * Internal solve method.
	 * 
	 * @param board
	 *            The actual game board.
	 * @param blackTurn
	 *            This boolean indicates whose turn it is.
	 * @param alpha
	 *            The alpha border.
	 * @param beta
	 *            The beta border.
	 * @param ply
	 *            The actual depth of the tree
	 * @return The outcome value will be returned at the end of the solver.
	 */
	private int solve(Board board, boolean blackTurn, int alpha, int beta, int ply) {
		// Lookup in TT
		long boardHash = board.getZobristHash();
		TTEntry<Integer> ttEntry = tTable[getIndexOfHash(boardHash)];
		if (ttEntry != null) {
			if (ttEntry.getZobristHash() == boardHash) {
				if (blackTurn && ttEntry.getLeftValue() != null) {
					counterTT++;
					return ttEntry.getLeftValue();
				} else if (!blackTurn && ttEntry.getRightValue() != null) {
					counterTT++;
					return ttEntry.getRightValue();
				}
			} else {
				ttEntry = null;
			}
		}

		counter++;
		int returnValue = 0;

		//Get the options for left or right
		List<Move> availableMoves;
		if (blackTurn) {
			availableMoves = board.getLeftOptions();
		} else {
			availableMoves = board.getRightOptions();
		}

		//move ordering when options are available
		if (availableMoves.isEmpty()) {
			returnValue = -1;
		} else {
			orderMoves(availableMoves, board, blackTurn);
		}

		//If there are moves available, do negamax
		if (returnValue == 0) {
			returnValue = -100;
			for (Move move : availableMoves) {
				board.executeMove(move);
				int value = -solve(board, !blackTurn, -beta, -alpha, ply + 1);
				board.revertMove(move);

				if (value > returnValue) {
					returnValue = value;
				}
				if (returnValue > alpha) {
					alpha = returnValue;
				}
				if (alpha >= beta) {
					cutoff++;
					break;
				}
				if (returnValue == 1) {
					cutoff++;
					break;
				}
			}
		}

		//Write into the transposition table when there is no entry 
		if (ttEntry == null) {
			if (blackTurn) {
				tTable[getIndexOfHash(boardHash)] = new TTEntry<>(boardHash, returnValue, null);
			} else {
				tTable[getIndexOfHash(boardHash)] = new TTEntry<>(boardHash, null, returnValue);
			}
		} else {
			if (blackTurn) {
				ttEntry.setLeftValue(returnValue);
			} else {
				ttEntry.setRightValue(returnValue);
			}
		}
		return returnValue;
	}

	@Override
	public void printCounter() {
		System.out.println(AlphaBetaSolver.class.getSimpleName());
		System.out.println("Nodes searched: " + counter);
		System.out.println("Nodes found in TT: " + counterTT);
		System.out.println("Cutoff: " + (1.0 * cutoff / counter));
	}

	@Override
	public void resetCounter() {
		counter = 0;
		counterTT = 0;
		cutoff = 0;
	}
}