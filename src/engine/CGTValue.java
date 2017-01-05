package engine;

public abstract class CGTValue extends CGTGame {
	public static CGTValue getOutcome(Number black, Number white) {
		//For fractions, see Berlekamp et al., p. 41
		//For switches, see Berlekamp et al., p. 141
		
		if(black.getValue() == 0 && white.getValue() == 0) {
			return new Nimber(0);
		}
		
		if(black.getValue()+1 == white.getValue()) {
			//{n|n+1}=n+1/2 (Berlekamp et al., p. 27)
			return new Number(black.getValue() + 0.5);
		}
		
		if(black.getValue() < 0 && white.getValue() > 0) {
			return new Number(0);
		}
		
		if(black.getValue() > 0 && white.getValue() > 0) {
			
		}
		
		if(black.getValue() < 0 && white.getValue() < 0) {
			
		}
		return null;
	}
	
	public static CGTValue getOutcome(Number black, Nimber white) {
		return null;
	}
	
	public static CGTValue getOutcome(Number black, Switch white) {
		return null;
	}
	
	public static CGTValue getOutcome(Number black, Infinitesimal white) {
		return null;
	}
	
	public static CGTValue getOutcome(Nimber black, Number white) {
		return CGTValue.getOutcome(white, black);
	}
	
	public static CGTValue getOutcome(Nimber black, Nimber white) {
		return null;
	}
	
	public static CGTValue getOutcome(Nimber black, Switch white) {
		return null;
	}
	
	public static CGTValue getOutcome(Nimber black, Infinitesimal white) {
		return null;
	}
	
	public static CGTValue getOutcome(Switch black, Number white) {
		return CGTValue.getOutcome(white, black);
	}
	
	public static CGTValue getOutcome(Switch black, Nimber white) {
		return CGTValue.getOutcome(white, black);
	}
	
	public static CGTValue getOutcome(Switch black, Switch white) {
		return null;
	}
	
	public static CGTValue getOutcome(Switch black, Infinitesimal white) {
		return null;
	}
	
	public static CGTValue getOutcome(Infinitesimal black, Number white) {
		return CGTValue.getOutcome(white, black);
	}
	
	public static CGTValue getOutcome(Infinitesimal black, Nimber white) {
		return CGTValue.getOutcome(white, black);
	}
	
	public static CGTValue getOutcome(Infinitesimal black, Switch white) {
		return CGTValue.getOutcome(white, black);
	}
	
	public static CGTValue getOutcome(Infinitesimal black, Infinitesimal white) {
		return null;
	}
}
