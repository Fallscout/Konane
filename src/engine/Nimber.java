package engine;

public class Nimber extends CGTValue {
    private final int value;

    public Nimber(int value) {
        if(value <= 0){
            throw new IllegalArgumentException("The value of the Nimber must be positive.");
        }
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "Nimber{" + "value=" + value + '}';
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Nimber nimber = (Nimber) o;

        return value == nimber.value;
    }

    @Override public int hashCode() {
        return value;
    }
}
