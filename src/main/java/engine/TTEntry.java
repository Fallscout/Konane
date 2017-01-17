package engine;

import game.Move;

public class TTEntry {
    private final long zobristHash;
    private CGTValue leftValue;
    private CGTValue rightValue;

    public TTEntry(long zobristHash, CGTValue leftValue, CGTValue rightValue) {
        this.zobristHash = zobristHash;
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    public CGTValue getLeftValue() {
        return leftValue;
    }

    public void setLeftValue(CGTValue leftValue) {
        this.leftValue = leftValue;
    }

    public CGTValue getRightValue() {
        return rightValue;
    }

    public void setRightValue(CGTValue rightValue) {
        this.rightValue = rightValue;
    }

    public long getZobristHash() {
        return zobristHash;
    }
}
