package game;

import engine.AlphaBetaSolver;
import engine.CABSolverSerial;
import engine.OutcomeType;
import engine.Solver;

public class Controller {

    public static void main(String[] args) {
        int maxSize = 30;

        Solver[] solvers = new Solver[2];
        solvers[0] = new AlphaBetaSolver();
        solvers[1] = new CABSolverSerial();

        for (int i = 3; i < maxSize / 3; i++) {
            for (int j = 3; j < maxSize / 3; j++) {

                if (i * j > maxSize) {
                    continue;
                }

                System.out.println("---------------Testing " + i + "x" + j + "---------------");
                OutcomeType outcomeType = null;
                for (Solver solver : solvers) {
                    Board board = new Board(i, j, false);
                    OutcomeType currentSolverOutcome = solver.solveWithTime(board);
                    if (outcomeType == null) {
                        outcomeType = currentSolverOutcome;
                    } else {
                        if (outcomeType != currentSolverOutcome) {
                            System.err.println("!!!NOT THE SAME OUTCOMETYPE FOR " + i + "x" + j + " BOARD!!!");
                            System.err.println("Former outcome: " + outcomeType);
                            System.err.println("Current outcome: " + currentSolverOutcome);
                        }
                    }
                    System.out.println();
                }
            }
        }

    }
}
