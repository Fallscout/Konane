package engine;

import java.util.List;

import game.Board;
import game.Move;

public class AlphaBetaSolver extends Solver {

	private final SimpleTTEntry[] tTable = new SimpleTTEntry[(int)Math.pow(2, 24)];

	private int cutoff;

	@Override
	public OutcomeType solve(Board board) {
		this.counter = 0;
		this.counterTT = 0;

		int blackOutcome = -1;
		int whiteOutcome = 1;

		int value;

		List<Move> blackMoves = board.getLeftOptions();
		for (Move move : blackMoves) {
			board.executeMove(move);
			value = solve(board, false, -10, 10, 1);
			board.revertMove(move);

			blackOutcome = Math.max(value, blackOutcome);
			if (blackOutcome == 1) {
				break;
			}
		}

		List<Move> whiteMoves = board.getRightOptions();
		for (Move move : whiteMoves) {
			board.executeMove(move);
			value = solve(board, true, -10, 10, 1);
			board.revertMove(move);

			whiteOutcome = Math.min(value, whiteOutcome);
			if (whiteOutcome == -1) {
				break;
			}
		}

        printCounter();
        resetCounter();

		if (blackOutcome == 1 && whiteOutcome == -1) {
			return OutcomeType.FIRST;
		} else if (blackOutcome == -1 && whiteOutcome == -1) {
			return OutcomeType.WHITE;
		} else if (blackOutcome == 1 && whiteOutcome == 1) {
			return OutcomeType.BLACK;
		} else if (blackOutcome == -1 && whiteOutcome == 1) {
			return OutcomeType.SECOND;
		}

		throw new IllegalStateException("Cannot determine winner.");
	}

	private int solve(Board board, boolean blackTurn, int alpha, int beta, int ply) {
        //		// Lookup in TT
        //        long boardHash = board.getZobristHash();
        //        SimpleTTEntry ttEntry = tTable[getIndexOfHash(boardHash)];
        //        if (ttEntry != null) {
        //            if (ttEntry.getZobristHash() == boardHash) {
        //                if (blackTurn && ttEntry.getLeftValue() != null) {
        //                	counterTT++;
        //                    return ttEntry.getLeftValue();
        //                } else if (!blackTurn && ttEntry.getRightValue() != null) {
        //                	counterTT++;
        //                    return ttEntry.getRightValue();
        //                }
        //            } else {
        //                ttEntry = null;
        //            }
        //        }

		counter++;
		int returnValue = 0;

		List<Move> availableMoves;
		if (blackTurn) {
			availableMoves = board.getLeftOptions();
		} else {
			availableMoves = board.getRightOptions();
		}

		if (availableMoves.isEmpty()) {
			if (blackTurn) {
				returnValue = -1; // White wins
			} else {
				returnValue = 1; // Black wins
			}
		} else {
//			orderMoves(availableMoves, board, blackTurn);
		}

		int value;

		if (returnValue == 0) {
			if (blackTurn) {
				returnValue = Integer.MIN_VALUE;
				for (Move move : availableMoves) {
					board.executeMove(move);
					value = solve(board, !blackTurn, alpha, beta, ply + 1);
					board.revertMove(move);

					if (value > returnValue) {
						returnValue = value;
					}
					if (returnValue > alpha) {
						alpha = returnValue;
					}
					if (beta <= alpha) {
						cutoff++;
						break;
					}
				}
			} else {
				returnValue = Integer.MAX_VALUE;
				for (Move move : availableMoves) {
					board.executeMove(move);
					value = solve(board, !blackTurn, alpha, beta, ply + 1);
					board.revertMove(move);

					if (value < returnValue) {
						returnValue = value;
					}
					if (returnValue < beta) {
						beta = returnValue;
					}
					if (beta <= alpha) {
						cutoff++;
						break;
					}
				}
			}
		}

        //        if (ttEntry == null) {
        //            if (blackTurn) {
        //                tTable[getIndexOfHash(boardHash)] = new SimpleTTEntry(boardHash, returnValue, null);
        //            } else {
        //                tTable[getIndexOfHash(boardHash)] = new SimpleTTEntry(boardHash, null, returnValue);
        //            }
        //        } else {
        //            if (blackTurn) {
        //                ttEntry.setLeftValue(returnValue);
        //            } else {
        //                ttEntry.setRightValue(returnValue);
        //            }
        //        }
        return returnValue;
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