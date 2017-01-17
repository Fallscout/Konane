package engine;


import java.util.Collections;
import java.util.List;

import game.Board;
import game.Move;

public abstract class Solver {

    protected int counter;
    protected int counterTT;

    public abstract OutcomeType solve(Board board);

    public OutcomeType solveWithTime(Board board){
        Long starttime = System.currentTimeMillis();
        OutcomeType result = solve(board);
        System.out.println("Time (ms): "+ (System.currentTimeMillis()-starttime));
        return result;
    }

    public int getCounter() {
        return counter;
    }

    public int getCounterTT() {
        return counterTT;
    }

    public abstract void printCounter();
    
	protected void orderMoves(List<Move> moves, Board board, boolean blacksMove) {
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
}
