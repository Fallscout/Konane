package engine;

public class Nimber extends CGTValue {
	private int value;
	
	public Nimber(int value) {
		this.value = value;
	}
	
	public Nimber add(Nimber other) {
		return new Nimber(this.value ^ other.value);
	}
	
	public double getValue() {
		return this.value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}

}
