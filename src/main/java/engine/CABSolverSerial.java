package engine;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import game.Board;
import game.Move;
import game.Piece;

public class CABSolverSerial extends Solver {

	private final TTEntry<CGTValue>[] tTable = new TTEntry[(int) Math.pow(2, 24)];
	private final CGTSolver cgtSolver = new CGTSolver();
	private final CGTValue ZERO = new Number(0);

	private final String filepath = "Documents\\cgsuite.txt";
	public FileWriter writer;

	private int foundCGT;
	private int notFoundCGT;
	private int cutoff;
	
	public OutcomeType solve(Board board) {
		this.counter = 0;
		this.counterTT = 0;
		this.foundCGT = 0;
		this.notFoundCGT = 0;
		this.cutoff = 0;

		CGTValue blackOutcome = solve(board, true, new Number(-10), new Number(10), 0);
		CGTValue whiteOutcome = solve(board, false, new Number(-10), new Number(10), 0);

		if (blackOutcome.equals(new Number(0))) {
			blackOutcome = new Nimber(1);
		} else if (blackOutcome instanceof Nimber) {
			blackOutcome = new Number(0);
		}

		if (whiteOutcome.equals(new Number(0))) {
			whiteOutcome = new Nimber(1);
		} else if (whiteOutcome instanceof Nimber) {
			whiteOutcome = new Number(0);
		}

		printCounter();
		resetCounter();
		cgtSolver.resetCounter();

		return determineWinner(blackOutcome, whiteOutcome);
	}

	private CGTValue solve(Board board, boolean blackTurn, CGTValue alpha, CGTValue beta, int ply) {
		// Lookup in TT
		long boardHash = board.getZobristHash();
		TTEntry<CGTValue> ttEntry = tTable[getIndexOfHash(boardHash)];
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
		CGTValue returnValue = null;

		List<Move> availableMoves;
		if (blackTurn) {
			availableMoves = board.getLeftOptions();
		} else {
			availableMoves = board.getRightOptions();
		}

		if (board.isEndgame()) {
			// Do CGT Stuff
			returnValue = cgtSolver.calculate(board);
			if (returnValue != null) {
				//					board.printToFile(returnValue);
				foundCGT++;
				if (returnValue.equals(new Number(0))) {
					returnValue = new Nimber(1);
				} else if (returnValue instanceof Nimber) {
					returnValue = new Number(0);
				}
			} else {
				notFoundCGT++;
			}
		}

		if (returnValue == null) {
			if (availableMoves.isEmpty()) {
				returnValue = CGTValue.max(new Number(1), new Number(-1), !blackTurn);
			} else if (availableMoves.size() > 1) {
				orderMoves(availableMoves, board, blackTurn);
			}
		}

		if (returnValue == null) {
			if (blackTurn) {
				returnValue = new Number(-10);
				for (Move move : availableMoves) {
					board.executeMove(move);
					CGTValue value = solve(board, false, alpha, beta, ply + 1);
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
					if (CGTValue.max(returnValue, ZERO, true).equals(returnValue) || returnValue.equals(ZERO)) {
						cutoff++;
						break;
					}
				}
			} else {
				returnValue = new Number(10);
				for (Move move : availableMoves) {
					board.executeMove(move);
					CGTValue value = solve(board, true, alpha, beta, ply + 1);
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
					if (CGTValue.max(returnValue, ZERO, false).equals(returnValue) || returnValue.equals(ZERO)) {
						cutoff++;
						break;
					}
				}
			}
		}

		if (returnValue.equals(new Number(0))) {
			returnValue = new Nimber(1);
		} else if (returnValue instanceof Nimber) {
			returnValue = new Number(0);
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

//	public void printToFile(CGTValue result) {
//		StringBuilder builder = new StringBuilder();
//
//		builder.append("g := game.grid.Konane(\"");
	//
//		for (int row = 0; row < this.rows; row++) {
//			for (int col = 0; col < this.cols; col++) {
//				Piece piece = this.gameState[row][col];
//				if (piece != null) {
//					if (piece.isBlack()) {
//						builder.append("x");
//					} else {
//						builder.append("o");
//					}
//				} else {
//					builder.append(".");
//				}
//			}
//			if (row < this.rows - 1) {
//				builder.append("|");
//			}
//		}
	//
//		builder.append("\")");
	//
//		builder.append(" = " + result.toString() + "\n");
//
//		try {
//			this.writer.write(builder.toString());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	@Override public void printCounter() {
		System.out.println(CABSolverSerial.class.getSimpleName());
		System.out.println("Nodes searched: " + counter);
		System.out.println("Nodes found in TT: " + counterTT);
		System.out.println("Cutoff: " + (1.0 * cutoff / counter));
		System.out.println("CGTNodes searched: " + cgtSolver.getCounter());
		System.out.println("CGTNodes found in TT: " + cgtSolver.getCounterTT());
		System.out.println("CGTValue found: " + (1.0 * foundCGT / (foundCGT + notFoundCGT)));
	}

	@Override public void resetCounter() {
		counter = 0;
		counterTT = 0;
		cutoff = 0;
		foundCGT = 0;
		notFoundCGT = 0;
	}
}
