package engine;

import java.util.List;

import game.Board;
import game.Move;

public class CGTSolver extends Solver {

	private final TTEntry[] tTable = new TTEntry[(int) Math.pow(2, 24)];

	/**
	 * Solves a given board and returns the {@link OutcomeType}
	 *
	 * @param board
	 *            The board that shall be solved
	 * @return The {@link OutcomeType} of the game (FIRST, SECOND, BLACK, WHITE)
	 */
	@Override
	public OutcomeType solve(Board board) {

		// CGTValue result = this.calculate(board);
		//
		// System.out.println("Result: " + result);
		//
		// if (result != null) {
		// Class<? extends CGTValue> resultingClass = result.getClass();
		//
		// if (resultingClass == Number.class) {
		// Number number = (Number) result;
		// if (number.getValue() == 0) {
		// return OutcomeType.SECOND;
		// } else if (number.getValue() > 0) {
		// return OutcomeType.BLACK;
		// } else if (number.getValue() < 0) {
		// return OutcomeType.WHITE;
		// }
		// } else if (resultingClass == Nimber.class) {
		// return OutcomeType.FIRST;
		// } else if (resultingClass == Switch.class) {
		// // TODO:
		// } else if (resultingClass == Infinitesimal.class) {
		// // TODO:
		// } else {
		// throw new IllegalStateException("The result class is not supported
		// yet: " + resultingClass);
		// }
		// }

		return null;
	}

	/**
	 * Returns the CGTValue of the given <code>board</code>.
	 *
	 * @param board
	 *            The board one wants to calculate the {@link CGTValue} for.
	 * @return The board's value
	 */
	// TODO: https://github.com/Fallscout/Konane/issues/3
	public CGTValue calculate(Board board) {
		//		// Lookup in TT
		//		long boardHash = board.getZobristHash();
		//		TTEntry ttEntry = tTable[getIndexOfHash(boardHash)];
		//		if (ttEntry != null) {
		//			if (ttEntry.getZobristHash() == boardHash) {
		//				counterTT++;
		//				return ttEntry.getLeftValue();
		//			}
		//		}

		counter++;

		// Get the possible options of both players
		List<Move> leftOptions = board.getLeftOptions();
		List<Move> rightOptions = board.getRightOptions();

		CGTValue cgtValue;

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
			if (max != leftOutcome) {
				rightOutcome = max;
			}
		}

		// Get the final outcome and store it in the transposition table
		cgtValue = CGTValue.combine(leftOutcome, rightOutcome);
		//		tTable[getIndexOfHash(boardHash)] = new TTEntry(boardHash, cgtValue, null);

		return cgtValue;
	}

	/**
	 * This method returns the primary hash code by using a bit mask
	 * 
	 * @param zobristHash
	 *            The complete hash
	 * @return the primary hash code
	 */
	private int getIndexOfHash(long zobristHash) {
		return (int) Math.abs(zobristHash & 0xFFFFFF);
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
