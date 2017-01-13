package engine;

public class Infinitesimal extends CGTValue {
	private final int value;

	public Infinitesimal(int value){
	    if(value == 0){
	        throw new IllegalArgumentException("The Infinitesimal mustn't be 0");
        }
        this.value = value;
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

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Infinitesimal that = (Infinitesimal) o;

        return value == that.value;
    }

    @Override public int hashCode() {
        return value;
    }
}
