package engine;

import game.Move;

public class TTEntry {
	private long key;
	private Move move;
	private int score;
	private ScoreFlag flag;
	private int depth;
	
	public TTEntry(long key, Move move, int score, ScoreFlag flag, int depth) {
		this.key = key;
		this.move = move;
		this.score = score;
		this.flag = flag;
		this.depth = depth;
	}

	public long getKey() {
		return key;
	}

	public Move getMove() {
		return move;
	}

	public int getScore() {
		return score;
	}

	public ScoreFlag getFlag() {
		return flag;
	}

	public int getDepth() {
		return depth;
	}
}
