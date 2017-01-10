package engine;

public class Nimber extends CGTValue {
    private final int value;

    public Nimber(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public CGTValue add(CGTValue other) throws IllegalAdditionException {
        throw new IllegalAdditionException("The addition of the two CGTValues (" + this + ", " + other
            + ") is not allowed: Not yet implemented");
    }

    @Override
    public String toString() {
        return "Nimber{" + "value=" + value + '}';
    }
}
