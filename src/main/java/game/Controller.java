package game;

import engine.AlphaBetaSolver;
import engine.CABSolverSerial;
import engine.OutcomeType;
import engine.Solver;

/**
 * This is the starting point of the program.  
 */
public class Controller {


	public static void main(String[] args) {
		int maxSize = 36;

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
