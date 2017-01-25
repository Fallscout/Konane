package engine;

import java.util.List;

import game.Board;
import game.Move;

public class CGTSolver extends Solver {

    @SuppressWarnings("unchecked")
	private final TTEntry<CGTValue>[] tTable = new TTEntry[(int) Math.pow(2, 24)];

	/**
	 * Solves a given board and returns the {@link OutcomeType}
	 *
	 * @param board
	 *            The board that shall be solved
	 * @return The {@link OutcomeType} of the game (FIRST, SECOND, BLACK, WHITE)
	 */
	@Override
	public OutcomeType solve(Board board) {
		throw new UnsupportedOperationException("This solving method for CGTSolver is not used");
	}

	/**
	 * Returns the CGTValue of the given <code>board</code>.
	 *
	 * @param board
	 *            The board one wants to calculate the {@link CGTValue} for.
	 * @return The board's value
	 */
	public CGTValue calculate(Board board) {
		// Lookup in TT
		long boardHash = board.getZobristHash();
		TTEntry<CGTValue> ttEntry = tTable[getIndexOfHash(boardHash)];
		if (ttEntry != null) {
			if (ttEntry.getZobristHash() == boardHash) {
				counterTT++;
				return ttEntry.getLeftValue();
			}
		}

		counter++;

		// Get the possible options of both players
		List<Move> leftOptions = board.getLeftOptions();
		List<Move> rightOptions = board.getRightOptions();

		CGTValue leftOutcome = null;
		CGTValue rightOutcome = null;

		// Calculate values of left options
		for (Move option : leftOptions) {
			board.executeMove(option);
			CGTValue value = calculate(board);
			board.revertMove(option);

			CGTValue max = CGTValue.max(value, leftOutcome, true);
			if (max != leftOutcome) {
				leftOutcome = max;
			}
		}

		// Calculate values of right options
		for (Move option : rightOptions) {
			board.executeMove(option);
			CGTValue value = calculate(board);
			board.revertMove(option);

			CGTValue max = CGTValue.max(value, rightOutcome, false);
			if (max != rightOutcome) {
				rightOutcome = max;
			}
		}

		// Get the final outcome and store it in the transposition table
		CGTValue cgtValue = CGTValue.combine(leftOutcome, rightOutcome);
		tTable[getIndexOfHash(boardHash)] = new TTEntry<>(boardHash, cgtValue, null);

		return cgtValue;
	}

	@Override public void printCounter() {
		System.out.println(CGTSolver.class.getSimpleName());
		System.out.println("Nodes searched: " + counter);
		System.out.println("Nodes found in TT: " + counterTT);
	}

	@Override public void resetCounter() {
		counter = 0;
		counterTT = 0;
	}
}
