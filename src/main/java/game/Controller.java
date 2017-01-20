package game;

import java.io.IOException;

import engine.AlphaBetaSolver;
import engine.CABSolverSerial;
import engine.OutcomeType;
import engine.Solver;

public class Controller {

	private static final int rows = 4;
	private static final int cols = 7;
	private static final boolean blackInCorner = false;

	public static void main(String[] args) {
		// Board board = new Board(rows, cols, blackInCorner);
		//
		//// Board board = new Board(new Piece[][] {
		//// {null, null, new Piece(0, 2, true), new Piece(0, 3, false)},
		//// {new Piece(1, 0, false), null, null, null},
		//// {new Piece(2, 0, true), null, null, new Piece(2, 3, false)},
		//// {null, null, null, null}
		//// });
		//
		// System.out.println(board.getBoardRepresentation());
		//
		// AlphaBetaSolver ab = new AlphaBetaSolver();
		// System.out.println(ab.solveWithTime(board));
		// try {
		// board.writer.close();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// System.out.println();
		//
		// board = new Board(rows, cols, blackInCorner);
		// CABSolverSerial cab = new CABSolverSerial();
		// System.out.println(cab.solveWithTime(board));
		// try {
		// board.writer.close();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		int maxSize = 30;

		Solver[] solvers = new Solver[2];
		solvers[0] = new AlphaBetaSolver();
		solvers[1] = new CABSolverSerial();

		for (int i = 4; i < maxSize / 3; i++) {
			for (int j = 4; j < maxSize / 3; j++) {

				if (i * j > maxSize) {
					continue;
				}

				for (int k = 0; k < 2; k++) {
					System.out.println("---------------Testing " + i + "x" + j + "-----" + (k == 0 ? "BLACK" : "WHITE") + "-----");
					OutcomeType outcomeType = null;
					for (Solver solver : solvers) {
						Board board = new Board(i, j, k == 0 ? true : false);
						OutcomeType currentSolverOutcome = solver.solveWithTime(board);
						System.out.println(currentSolverOutcome);
						if (outcomeType == null) {
							outcomeType = currentSolverOutcome;
						} else {
							if (outcomeType != currentSolverOutcome) {
								System.out.println();
								System.out.println("!!!NOT THE SAME OUTCOMETYPE FOR " + i + "x" + j + " BOARD!!!");
								System.out.println("Former outcome: " + outcomeType);
								System.out.println("Current outcome: " + currentSolverOutcome);
								System.out.println();
							}
						}
						System.out.println();
					}
				}
			}
		}
	}
}
