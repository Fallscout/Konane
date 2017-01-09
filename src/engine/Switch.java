package engine;

import java.security.InvalidParameterException;

public class Switch extends CGTValue {
    private final Number left;
    private final Number right;

    public Switch(Number left, Number right) {
        if (left.getValue() < right.getValue()) {
            throw new InvalidParameterException("In a switch, left must be greater than right");
        }
        this.left = left;
        this.right = right;
    }

    @Override
    public CGTValue add(CGTValue other) throws IllegalAdditionException {
        if (other instanceof Switch) {
            Switch otherSwitch = (Switch) other;
            return new Switch((Number) getLeft().add(otherSwitch.getLeft()), (Number) getRight().add(otherSwitch.getRight()));
        } else {
            throw new IllegalAdditionException("The addition of the two CGTValues (" + this + ", " + other
                + ") is not allowed: Not yet implemented");
        }
    }

    public Number getLeft() {
        return left;
    }

    public Number getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "Switch{" + "left=" + left + ", right=" + right + '}';
    }
}
