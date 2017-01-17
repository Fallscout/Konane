package engine;

public class Number extends CGTValue {

    private final double value;

    public Number(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Number{" + "value=" + value + '}';
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Number number = (Number) o;

        return Math.abs(number.value - this.value) < 0.001;
    }

    @Override public int hashCode() {
        long temp = Double.doubleToLongBits(value);
        return (int) (temp ^ (temp >>> 32));
    }
}
