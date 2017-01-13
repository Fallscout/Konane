package engine;

public class Infinitesimal extends CGTValue {
	private int value;
	
    @Override
    public CGTValue add(CGTValue other) throws IllegalAdditionException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toString() {
        if(this.value > 0) {
        	return value+"UP";
        } else if(this.value < 0) {
        	return value+"DOWN";
        } else {
        	throw new IllegalArgumentException("Invalid value for infinitesimal.");
        }
    }
    
    public int getValue() {
    	return value;
    }
}
