package engine;

import java.util.List;

import game.Board;
import game.Move;

public class AlphaBetaSolver extends Solver {

    private final TTEntry<Integer>[] tTable = new TTEntry[(int) Math.pow(2, 24)];

	private int cutoff;

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

		List<Move> availableMoves;
		if (blackTurn) {
			availableMoves = board.getLeftOptions();
		} else {
			availableMoves = board.getRightOptions();
		}

		if (availableMoves.isEmpty()) {
            returnValue = -1;
        } else {
            orderMoves(availableMoves, board, blackTurn);
        }

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

    @Override public void printCounter() {
        System.out.println(AlphaBetaSolver.class.getSimpleName());
        System.out.println("Nodes searched: " + counter);
        System.out.println("Nodes found in TT: " + counterTT);
        System.out.println("Cutoff: " + (1.0 * cutoff / counter));
    }

    @Override public void resetCounter() {
        counter = 0;
        counterTT = 0;
        cutoff = 0;
    }
}