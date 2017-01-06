package engine;

public class Nimber extends CGTValue {
	private int value;
	
	public Nimber(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
	
	@Override
	public String toString() {
		return value + "*";
	}
}
