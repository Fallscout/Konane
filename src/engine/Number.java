package engine;

public class Number extends CGTValue {
	private double value;
	
	public Number(double value) {
		this.value = value;
	}
	
	public Number add(Number other) {
		return new Number(this.value + other.value);
	}
	
	public double getValue() {
		return this.value;
	}
	
	@Override
	public String toString() {
		return ""+value;
	}
}
