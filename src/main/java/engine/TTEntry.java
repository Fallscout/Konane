package engine;

public class TTEntry<T> {
    private final long zobristHash;
    private T leftValue;
    private T rightValue;

    public TTEntry(long zobristHash, T leftValue, T rightValue) {
        this.zobristHash = zobristHash;
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    public T getLeftValue() {
        return leftValue;
    }

    public void setLeftValue(T leftValue) {
        this.leftValue = leftValue;
    }

    public T getRightValue() {
        return rightValue;
    }

    public void setRightValue(T rightValue) {
        this.rightValue = rightValue;
    }

    public long getZobristHash() {
        return zobristHash;
    }
}
