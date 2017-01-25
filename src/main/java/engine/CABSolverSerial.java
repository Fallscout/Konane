package engine;

import java.util.List;

import game.Board;
import game.Move;

/**
 * This is the Combinatorial Alpha Beta Solver. This solver calculates the
 * outcome type of a Konane board. Therefore it uses a standard alpha-beta
 * solver and an additional CGT solver.
 */
public class CABSolverSerial extends Solver {

	/**
	 * Transposition table array.
	 */
	@SuppressWarnings("unchecked")
	private final TTEntry<CGTValue>[] tTable = new TTEntry[(int) Math.pow(2, 24)];
	/**
	 * The CGT solver
	 */
	private final CGTSolver cgtSolver = new CGTSolver();
	/**
	 * Zero position constant.
	 */
	private final CGTValue ZERO = new Number(0);

	/**
	 * Counter for the CGT part.
	 */
	private int foundCGT;
	/**
	 * Counter for the CGT part.
	 */
	private int notFoundCGT;
	/**
	 * Counter for the cutoffs.
	 */
	private int cutoff;

	/**
	 * Main solve method. This method calls the internal solver method for black
	 * starting and white starting. Afterwards it combines both results to
	 * determine the overall winner.
	 */
	@Override
	public OutcomeType solve(Board board) {
		// Counter initialization
		this.counter = 0;
		this.counterTT = 0;
		this.foundCGT = 0;
		this.notFoundCGT = 0;
		this.cutoff = 0;

		// Solve for black starting
		CGTValue blackOutcome = solve(board, true, new Number(-10), new Number(10), 0);
		// Solve for white starting
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

		// returns the determined winner
		return determineWinner(blackOutcome, whiteOutcome);
	}

	/**
	 * This method is the internal solver method. It uses transposition tables
	 * and move ordering. The special part of this solver is that the solver
	 * stops at a board position that fulfills the end-game condition. If the
	 * end-game condition is fulfilled, the CGT value of the board will be
	 * calculated by the CGT solver. In this way the alpha-beta solver can safe
	 * some ply of searching and therefore safe very much nodes.
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

		// Get the options for left or right
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

		// move ordering when options are available
		if (returnValue == null) {
			if (availableMoves.isEmpty()) {
				returnValue = CGTValue.max(new Number(1), new Number(-1), !blackTurn);
			} else if (availableMoves.size() > 1) {
				orderMoves(availableMoves, board, blackTurn);
			}
		}

		// If there are moves available, do alpha beta
		if (returnValue == null) {
			if (blackTurn) {
				returnValue = new Number(-10);
				for (Move move : availableMoves) {
					board.executeMove(move);
					CGTValue value = solve(board, false, alpha, beta, ply + 1);
					board.revertMove(move);

					returnValue = CGTValue.max(value, returnValue, true);
					alpha = CGTValue.max(returnValue, alpha, true);

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
					beta = CGTValue.max(returnValue, beta, false);

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

		// Write into the transposition table when there is no entry
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

	/**
	 * This method determines the winner given the outcome for black and white.
	 * 
	 * @param blackOutcome
	 *            calculated outcome of black.
	 * @param whiteOutcome
	 *            calculated outcome of white.
	 * @return returns the combined outcome of black and white starting.
	 */
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
					} else if (white.isMixed()) {
						return OutcomeType.BLACK;
					}
				} else {
					if (white.isNegative()) {
						return OutcomeType.WHITE;
					} else if (white.isPositive()) {
						return OutcomeType.SECOND;
					} else if (white.isMixed()) {
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
			// Nimber black = (Nimber) blackOutcome;

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
				} else if (white.isMixed()) {
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
					} else if (black.isMixed()) {
						return OutcomeType.SECOND;
					}
				} else {
					if (black.isNegative()) {
						return OutcomeType.WHITE;
					} else if (black.isPositive()) {
						return OutcomeType.FIRST;
					} else if (black.isMixed()) {
						return OutcomeType.WHITE;
					}
				}

			} else if (whiteOutcome instanceof Nimber) {
				// Nimber white = (Nimber) whiteOutcome;

				if (black.isNegative()) {
					return OutcomeType.SECOND;
				} else if (black.isPositive()) {
					return OutcomeType.BLACK;
				} else if (black.isMixed()) {
					return OutcomeType.SECOND;
				}

			} else if (whiteOutcome instanceof Switch) {
				Switch white = (Switch) whiteOutcome;

				if (black.isNegative()) {
					if (white.isNegative()) {
						return OutcomeType.WHITE;
					} else if (white.isPositive()) {
						return OutcomeType.SECOND;
					} else if (white.isMixed()) {
						return OutcomeType.SECOND;
					}
				} else if (black.isPositive()) {
					if (white.isNegative()) {
						return OutcomeType.FIRST;
					} else if (white.isPositive()) {
						return OutcomeType.BLACK;
					} else if (white.isMixed()) {
						return OutcomeType.BLACK;
					}
				} else if (black.isMixed()) {
					if (white.isNegative()) {
						return OutcomeType.WHITE;
					} else if (white.isPositive()) {
						return OutcomeType.SECOND;
					} else if (white.isMixed()) {
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
				} else if (black.isMixed()) {
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

				if (white.getValue() > 0) {
					if (black.getValue() > 0) {
						return OutcomeType.BLACK;
					} else {
						return OutcomeType.SECOND;
					}
				}

				if (white.getValue() <= 0) {
					if (black.getValue() > 0) {
						return OutcomeType.FIRST;
					} else {
						return OutcomeType.WHITE;
					}
				}
			} else if (whiteOutcome instanceof Nimber) {
				if (black.getValue() > 0) {
					return OutcomeType.BLACK;
				} else {
					return OutcomeType.SECOND;
				}
			} else if (whiteOutcome instanceof Switch) {
				Switch white = (Switch) whiteOutcome;

				if (white.isNegative()) {
					if (black.getValue() > 0) {
						return OutcomeType.FIRST;
					} else {
						return OutcomeType.WHITE;
					}
				} else if (white.isPositive()) {
					if (black.getValue() > 0) {
						return OutcomeType.BLACK;
					} else {
						return OutcomeType.SECOND;
					}
				} else if (white.isMixed()) {
					if (black.getValue() > 0) {
						return OutcomeType.BLACK;
					} else {
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
				} else {
					if (white.getValue() > 0) {
						return OutcomeType.SECOND;
					} else {
						return OutcomeType.WHITE;
					}
				}
			}
		}

		throw new IllegalArgumentException("Cannot determine winner for given arguments.");
	}

	@Override
	public void printCounter() {
		System.out.println(CABSolverSerial.class.getSimpleName());
		System.out.println("Nodes searched: " + counter);
		long all = cgtSolver.getCounter() + counter;
		System.out.println("AB nodes + CGT nodes searched: " + all);
		System.out.println("Nodes found in TT: " + counterTT);
		System.out.println("Cutoff: " + (1.0 * cutoff / counter));
		System.out.println("CGTNodes searched: " + cgtSolver.getCounter());
		System.out.println("CGTNodes found in TT: " + cgtSolver.getCounterTT());
		System.out.println("CGTValue found: " + (1.0 * foundCGT / (foundCGT + notFoundCGT)));
	}

	@Override
	public void resetCounter() {
		counter = 0;
		counterTT = 0;
		cutoff = 0;
		foundCGT = 0;
		notFoundCGT = 0;
	}
}
