package engine;

public class Number extends CGTValue {

    private final double value;

    public Number(double value) {
        this.value = value;
    }

    @Override
    public CGTValue add(CGTValue other) throws IllegalAdditionException {
        if (other instanceof Number) {
            Number otherNumber = (Number) other;
            return new Number(value + otherNumber.value);
        } else {
            throw new IllegalAdditionException("The addition of the two CGTValues (" + this + ", " + other
                + ") is not allowed: Not yet implemented");
        }
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Number{" + "value=" + value + '}';
    }
}
