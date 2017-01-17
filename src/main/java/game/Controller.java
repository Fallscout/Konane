package game;

import java.io.IOException;

import engine.AlphaBetaSolver;
import engine.CABSolverSerial;
import engine.CGTSolver;
import engine.OutcomeType;
import engine.Solver;

public class Controller {

	private static final int rows = 5;
	private static final int cols = 6;
	private static final boolean blackInCorner = true;
	
	public static void main(String[] args) {
		Board board = new Board(rows, cols, blackInCorner);

//		 Board board = new Board(new Piece[][] {
//		 {null, null, new Piece(0, 2, true), new Piece(0, 3, false)},
//		 {new Piece(1, 0, false), null, null, null},
//		 {new Piece(2, 0, true), null, null, new Piece(2, 3, false)},
//		 {null, null, null, null}
//		 });

		System.out.println(board.getBoardRepresentation());
		
//		System.out.println();
		
//		CGTSolver cgt = new CGTSolver();
		
		AlphaBetaSolver ab = new AlphaBetaSolver();
		System.out.println(ab.solveWithTime(board));
		try {
			board.writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		
//		System.out.println();
		
//		board = new Board(rows, cols, blackInCorner);
//		CABSolverSerial cab = new CABSolverSerial();
//		System.out.println(cab.solveWithTime(board));
//		try {
//			board.writer.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
