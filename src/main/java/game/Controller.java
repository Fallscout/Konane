package game;

import java.util.concurrent.ExecutionException;

import engine.ABCGTSolver;
import engine.OutcomeType;

public class Controller {

	public static void main(String[] args) {
		Board board = new Board(5, 5, false);

		// Board board = new Board(new Piece[][] {
		// {null, null, new Piece(0, 2, true), new Piece(0, 3, false)},
		// {new Piece(1, 0, false), null, null, null},
		// {new Piece(2, 0, true), null, null, new Piece(2, 3, false)},
		// {null, null, null, null}
		// });

		System.out.println(board.getBoardRepresentation());

		// CGTSolver solver = new CGTSolver();
		// AlphaBetaSolver solver = new AlphaBetaSolver();
		ABCGTSolver solver = new ABCGTSolver();

		try {
			OutcomeType result = solver.solve(board);
			System.out.println(result);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} finally {
			solver.shutdown();
		}
	}
}
