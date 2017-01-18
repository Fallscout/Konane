package engine;


import java.util.Collections;
import java.util.List;

import game.Board;
import game.Move;

public abstract class Solver {

	protected long counter;
	protected long counterTT;

    public abstract OutcomeType solve(Board board);

    public OutcomeType solveWithTime(Board board){
        Long starttime = System.currentTimeMillis();
        OutcomeType result = solve(board);
        System.out.println("Time (ms): "+ (System.currentTimeMillis()-starttime));
        return result;
    }

	public long getCounter() {
		return counter;
    }

	public long getCounterTT() {
		return counterTT;
    }

    public abstract void printCounter();

	public abstract void resetCounter();

	/**
	 * This method returns the primary hash code by using a bit mask
	 *
	 * @param zobristHash The complete hash
	 * @return the primary hash code
	 */
	protected int getIndexOfHash(long zobristHash) {
		return (int) Math.abs(zobristHash & 0xFFFFFF);
	}

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
