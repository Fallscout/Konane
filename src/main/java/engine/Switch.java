package engine;

import java.security.InvalidParameterException;

public class Switch extends CGTValue {
    private final Number left;
    private final Number right;

    public Switch(Number left, Number right) {
        if (left.getValue() <= right.getValue()) {
            throw new InvalidParameterException("In a switch, left must be greater than right");
        }
        this.left = left;
        this.right = right;
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
    
    public boolean isNegative() {
        return this.left.getValue() < 0 && this.right.getValue() <= 0;
    }
    
    public boolean isPositive() {
        return this.left.getValue() >= 0 && this.right.getValue() > 0;
    }
    
    public boolean isNimber() {
    	return this.left.getValue() >= 0 && this.right.getValue() <= 0;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Switch aSwitch = (Switch) o;

        if (left != null ? !left.equals(aSwitch.left) : aSwitch.left != null)
            return false;
        return right != null ? right.equals(aSwitch.right) : aSwitch.right == null;
    }

    @Override public int hashCode() {
        int result = left != null ? left.hashCode() : 0;
        result = 31 * result + (right != null ? right.hashCode() : 0);
        return result;
    }
}
