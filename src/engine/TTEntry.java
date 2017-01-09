package engine;

import game.Move;

public class TTEntry {
    private final long zobristHash;
    private final Move bestLeftMove;
    private final Move bestRightMove;
    private final CGTValue cgtValue;

    public TTEntry(long zobristHash, Move bestLeftMove, Move bestRightMove, CGTValue cgtValue) {
        this.zobristHash = zobristHash;
        this.bestLeftMove = bestLeftMove;
        this.bestRightMove = bestRightMove;
        this.cgtValue = cgtValue;
    }

	public Move getBestLeftMove() {
		return bestLeftMove;
	}

	public Move getBestRightMove() {
        return bestRightMove;
    }

    public CGTValue getCgtValue() {
        return cgtValue;
    }

    public long getZobristHash() {
        return zobristHash;
    }
}
