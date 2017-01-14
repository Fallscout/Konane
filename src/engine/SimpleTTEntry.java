package engine;

public class SimpleTTEntry {
    private final long zobristHash;
    private final int value;

    public SimpleTTEntry(long zobristHash, int value) {
        this.zobristHash = zobristHash;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public long getZobristHash() {
        return zobristHash;
    }
}
