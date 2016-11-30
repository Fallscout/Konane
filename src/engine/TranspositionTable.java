package engine;

import game.Move;

public class TranspositionTable {
	private TTEntry[][] entries;
	private int keysize;

	public TranspositionTable(int keysize) {
		this.keysize = keysize;
		this.entries = new TTEntry[1 << this.keysize][2];
	}

	public void addEntry(long hash, Move move, int score, ScoreFlag flag, int depth) {
		int index = (int) (hash & (1 << this.keysize) - 1);
		long key = hash >>> this.keysize;

		TTEntry entry = new TTEntry(key, move, score, flag, depth);
		TTEntry[] row = this.entries[index];

		// replacement scheme TwoDeep

		if(row[0] == null) {
			row[0] = entry;
		} else if (row[0] != null && entry.getDepth() >= row[0].getDepth()) {
			row[1] = row[0];
			row[0] = entry;
		} else {
			row[1] = entry;
		}

	}

	public TTEntry getEntry(long hash) {
		int index = (int) (hash & (1 << this.keysize) - 1);
		long key = hash >>> this.keysize;

		TTEntry[] row = this.entries[index];

		for (TTEntry entry : row) {
			if (entry != null && entry.getKey() == key) {
				return entry;
			}
		}

		return null;
	}

	public void clear() {
		this.entries = new TTEntry[1 << this.keysize][2];
	}
}
