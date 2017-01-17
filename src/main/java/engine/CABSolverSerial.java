package engine;

import java.util.List;

import game.Board;
import game.Move;

public class CABSolverSerial extends Solver {

	private final TTEntry[] tTable = new TTEntry[(int)Math.pow(2, 24)];
	private final CGTSolver cgtSolver = new CGTSolver();
	private final CGTValue ZERO = new Number(0);

	private int counter;
	private int foundCGT;
	private int notFoundCGT;
	private int cutoff;
	
	public OutcomeType solve(Board board) {
		this.counter = 0;
		this.counterTT = 0;
		this.foundCGT = 0;
		this.notFoundCGT = 0;
		this.cutoff = 0;
		
		CGTValue blackOutcome = new Number(-10);
		CGTValue whiteOutcome = new Number(10);

		CGTValue value;

		List<Move> blackMoves = board.getLeftOptions();
		for (Move move : blackMoves) {
			board.executeMove(move);
			value = solve(board, false, new Number(-10), new Number(10), 1);
			board.revertMove(move);
			blackOutcome = CGTValue.max(value, blackOutcome, true);
			if(CGTValue.max(blackOutcome, ZERO, true).equals(blackOutcome) || blackOutcome.equals(ZERO)) {
				break;
			}
//			if(CGTValue.greaterEqual(blackOutcome, ZERO)) {
//				break;
//			}
		}

		List<Move> whiteMoves = board.getRightOptions();
		for (Move move : whiteMoves) {
			board.executeMove(move);
			value = solve(board, true, new Number(-10), new Number(10), 1);
			board.revertMove(move);
			whiteOutcome = CGTValue.max(value, whiteOutcome, false);
			if(CGTValue.max(whiteOutcome, ZERO, false).equals(whiteOutcome) || whiteOutcome.equals(ZERO)) {
				break;
			}
//			if(CGTValue.lessEqual(whiteOutcome, ZERO)) {
//				break;
//			}
		}

		System.out.println("CAB solver:");
		System.out.println("Node counter: " + counter);
		System.out.println("Nodes looked up in TT: " + counterTT);
		System.out.println("Found CGT Value: " + foundCGT);
		System.out.println("Could not find CGT Value: " + notFoundCGT);
		System.out.println("Cutoffs: " + cutoff);
//		System.out.println("CGT counter PreTT: " + cgtSolver.getCounterPreTT());
//		System.out.println("CGT counter PostTT: " + cgtSolver.getCounterPostTT());

		return determineWinner(blackOutcome, whiteOutcome);
	}

	private CGTValue solve(Board board, boolean blackTurn, CGTValue alpha, CGTValue beta, int ply) {
		// Lookup in TT
//		long boardHash = board.getZobristHash();
//		TTEntry ttEntry = tTable[getIndexOfHash(boardHash)];
//		if (ttEntry != null) {
//			if (ttEntry.getZobristHash() == boardHash) {
//				counterTT++;
//				return ttEntry.getCgtValue();
//			}
//		}
		
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
//			orderMoves(availableMoves, board, blackTurn);
		}

		if (returnValue == null) {
			if (board.isEndgame()) {
				// Do CGT Stuff
				returnValue = cgtSolver.calculate(board);
				if(returnValue != null) {
//					board.printToFile(returnValue);
					foundCGT++;
				} else {
					notFoundCGT++;
				}
			}
		}

		CGTValue value;

		if (returnValue == null) {
			if (blackTurn) {
				returnValue = new Number(-1000);
				for (Move move : availableMoves) {
					board.executeMove(move);
					value = solve(board, false, alpha, beta, ply + 1);
					board.revertMove(move);

					returnValue = CGTValue.max(value, returnValue, true);
//					if (CGTValue.greater(value, returnValue)) {
//						returnValue = value;
//					}

					alpha = CGTValue.max(returnValue, alpha, true);
//					if (CGTValue.greater(returnValue, alpha)) {
//						alpha = returnValue;
//						bestLeftOption = move;
//					}
					
					CGTValue test = CGTValue.max(alpha, beta, true);
					if (test.equals(alpha) || alpha.equals(beta)) {
						cutoff++;
						break;
					}
				}
			} else {
				returnValue = new Number(1000);
				for (Move move : availableMoves) {
					board.executeMove(move);
					value = solve(board, true, alpha, beta, ply + 1);
					board.revertMove(move);

					returnValue = CGTValue.max(value, returnValue, false);
//					if (CGTValue.less(value, returnValue)) {
//						returnValue = value;
//					}
					
					beta = CGTValue.max(returnValue, beta, false);
//					if (CGTValue.less(returnValue, beta)) {
//						beta = returnValue;
//						bestRightOption = move;
//					}
					
					CGTValue test = CGTValue.max(alpha, beta, false);
					if (test.equals(beta) || alpha.equals(beta)) {
						cutoff++;
						break;
					}
				}
			}
		}

//		tTable[getIndexOfHash(boardHash)] = new TTEntry(boardHash, bestLeftOption, bestRightOption, returnValue);

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

				if (black.getValue() >= 0 && white.getValue() <= 0) {
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
	
	@Override
	public void printCounter() {

	}
}
