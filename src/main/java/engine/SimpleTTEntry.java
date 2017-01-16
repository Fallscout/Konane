package engine;

public class SimpleTTEntry {
    private final long zobristHash;
    private Integer leftValue;
    private Integer rightValue;

    public SimpleTTEntry(long zobristHash, Integer leftValue, Integer rightValue) {
        this.zobristHash = zobristHash;
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    public Integer getLeftValue() {
        return leftValue;
    }

    public void setLeftValue(Integer leftValue) {
        this.leftValue = leftValue;
    }

    public Integer getRightValue() {
        return rightValue;
    }

    public void setRightValue(Integer rightValue) {
        this.rightValue = rightValue;
    }

    public long getZobristHash() {
        return zobristHash;
    }
}
