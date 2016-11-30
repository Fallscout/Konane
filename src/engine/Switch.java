package engine;

import java.security.InvalidParameterException;

public class Switch extends CGTValue {
	private Number left;
	private Number right;

	public Switch(Number left, Number right) {
		if (left.getValue() < right.getValue()) {
			throw new InvalidParameterException("In a switch, left must be greater than right");
		}
		this.left = left;
		this.right = right;
	}

	public Switch add(Number number) {
		return new Switch(new Number(this.left.getValue() + number.getValue()),
				new Number(this.right.getValue() + number.getValue()));
	}
}
