package engine;

import game.Board;
import game.Move;

import java.util.Collections;
import java.util.List;

public class CABSolverSerial extends Solver {

	private final TTEntry[] tTable = new TTEntry[(int) Math.pow(2, 24)];
	private final CGTSolver cgtSolver = new CGTSolver();

	private int counter = 0;
	private int counterTT = 0;

	public OutcomeType solve(Board board) {
		CGTValue blackOutcome = new Number(-100);
		CGTValue whiteOutcome = new Number(100);

		CGTValue value;

		List<Move> blackMoves = board.getLeftOptions();
		for (Move move : blackMoves) {
			board.executeMove(move);
			value = solve(board, false, new Number(-100), new Number(100), 1);
			board.revertMove(move);
			blackOutcome = CGTValue.max(value, blackOutcome, true);
		}

		List<Move> whiteMoves = board.getRightOptions();
		for (Move move : whiteMoves) {
			board.executeMove(move);
			value = solve(board, true, new Number(-100), new Number(100), 1);
			board.revertMove(move);
			whiteOutcome = CGTValue.max(value, whiteOutcome, false);
		}

		System.out.println("AB-CGT solver:");
		System.out.println("Nodes looked up in TT: " + counterTT);
		System.out.println("Node counter: " + counter);
		System.out.println("CGT counter PreTT: " + cgtSolver.getCounterPreTT());
		System.out.println("CGT counter PostTT: " + cgtSolver.getCounterPostTT());

		return determineWinner(blackOutcome, whiteOutcome);
	}

	@Override
	public void printCounter() {

	}

	private void orderMoves(List<Move> moves, Board board, boolean blacksMove) {
		int[] possibleOpponentsMoves = new int[moves.size()];
		for (int i = 0; i < moves.size(); i++) {
			Move move = moves.get(i);
			board.executeMove(move);
			int opponentsMoves;
			if (blacksMove) {
				opponentsMoves = board.getRightOptions().size();
			} else {
				opponentsMoves = board.getLeftOptions().size();
			}
			board.revertMove(move);
			possibleOpponentsMoves[i] = opponentsMoves;
		}

		for (int i = 0; i < possibleOpponentsMoves.length; i++) {
			for (int j = i + 1; j < possibleOpponentsMoves.length; j++) {
				int opponentsMoves = possibleOpponentsMoves[i];
				int opponentsMoves2 = possibleOpponentsMoves[j];

				if (opponentsMoves > opponentsMoves2) {
					possibleOpponentsMoves[i] = opponentsMoves2;
					possibleOpponentsMoves[j] = opponentsMoves;

					Collections.swap(moves, i, j);
				}
			}
		}
	}

	private CGTValue solve(Board board, boolean blackTurn, CGTValue alpha, CGTValue beta, int ply) {

		// Lookup in TT
		long boardHash = board.getZobristHash();
		TTEntry ttEntry = tTable[getIndexOfHash(boardHash)];
		if (ttEntry != null) {
			if (ttEntry.getZobristHash() == boardHash) {
				counterTT++;
				return ttEntry.getCgtValue();
			}
		}
		counter++;
		CGTValue returnValue = null;
		Move bestLeftOption = null;
		Move bestRightOption = null;

		List<Move> availableMoves;
		if (blackTurn) {
			availableMoves = board.getLeftOptions();
		} else {
			availableMoves = board.getRightOptions();
		}

		if (availableMoves.isEmpty()) {
			if (blackTurn) {
				returnValue = new Number(-1); // White wins
			} else {
				returnValue = new Number(1); // Black wins
			}
		} else {
			orderMoves(availableMoves, board, blackTurn);
		}

		if (returnValue == null) {
			if (board.isEndgame()) {
				// Do CGT Stuff
				returnValue = cgtSolver.calculate(board);
			}
		}

		CGTValue value;

		if (returnValue == null) {
			if (blackTurn) {
				returnValue = new Number(-Double.MAX_VALUE);
				for (Move move : availableMoves) {
					board.executeMove(move);
					value = solve(board, false, alpha, beta, ply + 1);
					board.revertMove(move);

					if (CGTValue.greater(value, returnValue)) {
						returnValue = value;
					}

					if (CGTValue.greater(returnValue, alpha)) {
						alpha = returnValue;
						bestLeftOption = move;
					}
					if (CGTValue.lessEqual(beta, alpha)) {
						break;
					}
				}
			} else {
				returnValue = new Number(Double.MAX_VALUE);
				for (Move move : availableMoves) {
					board.executeMove(move);
					value = solve(board, true, alpha, beta, ply + 1);
					board.revertMove(move);

					if (CGTValue.less(value, returnValue)) {
						returnValue = value;
					}

					if (CGTValue.less(returnValue, beta)) {
						beta = returnValue;
						bestRightOption = move;
					}
					if (CGTValue.lessEqual(beta, alpha)) {
						break;
					}
				}
			}
		}

		tTable[getIndexOfHash(boardHash)] = new TTEntry(boardHash, bestLeftOption, bestRightOption, returnValue);

		return returnValue;
	}

	public static OutcomeType determineWinner(CGTValue blackOutcome, CGTValue whiteOutcome) {
		if (blackOutcome instanceof Number) {
			Number black = (Number) blackOutcome;

			if (whiteOutcome instanceof Number) {
				Number white = (Number) whiteOutcome;

				if (black.getValue() >= 0 && white.getValue() > 0) {
					return OutcomeType.BLACK;
				}

				if (black.getValue() > 0 && white.getValue() < 0) {
					return OutcomeType.FIRST;
				}

				if (black.getValue() < 0 && white.getValue() <= 0) {
					return OutcomeType.WHITE;
				}

				if (black.getValue() < 0 && white.getValue() > 0) {
					return OutcomeType.SECOND;
				}

				if (black.getValue() == 0 && white.getValue() == 0) {
					return OutcomeType.FIRST;
				}
			} else if (whiteOutcome instanceof Nimber) {
				// Nimber white = (Nimber)whiteOutcome;

				if (black.getValue() > 0) {
					return OutcomeType.BLACK;
				}

				if (black.getValue() < 0) {
					return OutcomeType.SECOND;
				}

				if (black.getValue() == 0) {
					return OutcomeType.BLACK;
				}

			} else if (whiteOutcome instanceof Switch) {
				Switch white = (Switch) whiteOutcome;

				if (black.getValue() >= 0) {
					if (white.isNegative()) {
						return OutcomeType.FIRST;
					} else if (white.isPositive()) {
						return OutcomeType.BLACK;
					} else if (white.isNimber()) {
						return OutcomeType.BLACK;
					}
				} else {
					if (white.isNegative()) {
						return OutcomeType.WHITE;
					} else if (white.isPositive()) {
						return OutcomeType.SECOND;
					} else if (white.isNimber()) {
						return OutcomeType.SECOND;
					}
				}

			} else if (whiteOutcome instanceof Infinitesimal) {
				Infinitesimal white = (Infinitesimal) whiteOutcome;

				if (black.getValue() > 0) {
					if (white.getValue() > 0) {
						return OutcomeType.BLACK;
					} else {
						return OutcomeType.FIRST;
					}
				}

				if (black.getValue() < 0) {
					if (white.getValue() > 0) {
						return OutcomeType.SECOND;
					} else {
						return OutcomeType.WHITE;
					}
				}

				if (black.getValue() == 0) {
					if (white.getValue() > 0) {
						return OutcomeType.BLACK;
					} else {
						return OutcomeType.FIRST;
					}
				}
			}
		} else if (blackOutcome instanceof Nimber) {
			//			Nimber black = (Nimber) blackOutcome;

			if (whiteOutcome instanceof Number) {
				Number white = (Number) whiteOutcome;

				if (white.getValue() > 0) {
					return OutcomeType.SECOND;
				}

				if (white.getValue() < 0) {
					return OutcomeType.WHITE;
				}

				if (white.getValue() == 0) {
					return OutcomeType.WHITE;
				}

			} else if (whiteOutcome instanceof Nimber) {
				// Nimber white = (Nimber)whiteOutcome;

				return OutcomeType.SECOND;
			} else if (whiteOutcome instanceof Switch) {
				Switch white = (Switch) whiteOutcome;

				if (white.isNegative()) {
					return OutcomeType.WHITE;
				} else if (white.isPositive()) {
					return OutcomeType.SECOND;
				} else if (white.isNimber()) {
					return OutcomeType.SECOND;
				}

			} else if (whiteOutcome instanceof Infinitesimal) {
				Infinitesimal white = (Infinitesimal) whiteOutcome;

				if (white.getValue() > 0) {
					return OutcomeType.SECOND;
				} else {
					return OutcomeType.WHITE;
				}
			}
		} else if (blackOutcome instanceof Switch) {
			Switch black = (Switch) blackOutcome;

			if (whiteOutcome instanceof Number) {
				Number white = (Number) whiteOutcome;

				if (white.getValue() > 0) {
					if (black.isNegative()) {
						return OutcomeType.SECOND;
					} else if (black.isPositive()) {
						return OutcomeType.BLACK;
					} else if (black.isNimber()) {
						return OutcomeType.SECOND;
					}
				} else {
					if (black.isNegative()) {
						return OutcomeType.WHITE;
					} else if (black.isPositive()) {
						return OutcomeType.FIRST;
					} else if (black.isNimber()) {
						return OutcomeType.WHITE;
					}
				}

			} else if (whiteOutcome instanceof Nimber) {
				// Nimber white = (Nimber) whiteOutcome;

				if (black.isNegative()) {
					return OutcomeType.SECOND;
				} else if (black.isPositive()) {
					return OutcomeType.BLACK;
				} else if (black.isNimber()) {
					return OutcomeType.SECOND;
				}

			} else if (whiteOutcome instanceof Switch) {
				Switch white = (Switch) whiteOutcome;

				if (black.isNegative()) {
					if (white.isNegative()) {
						return OutcomeType.WHITE;
					} else if (white.isPositive()) {
						return OutcomeType.SECOND;
					} else if (white.isNimber()) {
						return OutcomeType.SECOND;
					}
				} else if (black.isPositive()) {
					if (white.isNegative()) {
						return OutcomeType.FIRST;
					} else if (white.isPositive()) {
						return OutcomeType.BLACK;
					} else if (white.isNimber()) {
						return OutcomeType.BLACK;
					}
				} else if (black.isNimber()) {
					if (white.isNegative()) {
						return OutcomeType.WHITE;
					} else if (white.isPositive()) {
						return OutcomeType.SECOND;
					} else if (white.isNimber()) {
						return OutcomeType.SECOND;
					}
				}
			} else if (whiteOutcome instanceof Infinitesimal) {
				Infinitesimal white = (Infinitesimal) whiteOutcome;

				if (black.isNegative()) {
					if (white.getValue() > 0) {
						return OutcomeType.SECOND;
					} else {
						return OutcomeType.WHITE;
					}
				} else if (black.isPositive()) {
					if (white.getValue() > 0) {
						return OutcomeType.BLACK;
					} else {
						return OutcomeType.FIRST;
					}
				} else if (black.isNimber()) {
					if (white.getValue() > 0) {
						return OutcomeType.SECOND;
					} else {
						return OutcomeType.WHITE;
					}
				}
			}
		} else if (blackOutcome instanceof Infinitesimal) {
			Infinitesimal black = (Infinitesimal) blackOutcome;

			if (whiteOutcome instanceof Number) {
				Number white = (Number) whiteOutcome;

				if(white.getValue() > 0) {
					if(black.getValue() > 0) {
						return OutcomeType.BLACK;
					} else {
						return OutcomeType.SECOND;
					}
				}

				if(white.getValue() <= 0) {
					if(black.getValue() > 0) {
						return OutcomeType.FIRST;
					} else {
						return OutcomeType.WHITE;
					}
				}
			} else if (whiteOutcome instanceof Nimber) {
				//				Nimber white = (Nimber) whiteOutcome;

				if(black.getValue() > 0) {
					return OutcomeType.BLACK;
				} else {
					return OutcomeType.SECOND;
				}
			} else if (whiteOutcome instanceof Switch) {
				Switch white = (Switch) whiteOutcome;

				if(white.isNegative()) {
					if(black.getValue() > 0) {
						return OutcomeType.FIRST;
					} else {
						return OutcomeType.WHITE;
					}
				} else if(white.isPositive()) {
					if(black.getValue() > 0) {
						return OutcomeType.BLACK;
					} else {
						return OutcomeType.SECOND;
					}
				} else if(white.isNimber()) {
					if(black.getValue() > 0) {
						return OutcomeType.BLACK;
					} else {
						return OutcomeType.SECOND;
					}
				}
			} else if (whiteOutcome instanceof Infinitesimal) {
				Infinitesimal white = (Infinitesimal) whiteOutcome;

				if(black.getValue() > 0) {
					if(white.getValue() > 0) {
						return OutcomeType.BLACK;
					} else {
						return OutcomeType.FIRST;
					}
				} else {
					if(white.getValue() > 0) {
						return OutcomeType.SECOND;
					} else {
						return OutcomeType.WHITE;
					}
				}
			}
		}

		throw new IllegalArgumentException("Cannot determine winner for given arguments.");
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
}