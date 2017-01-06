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
			//find p and n
			int p = 0;
			int n = 0;
			return new Number((2*p+1)/Math.pow(2, n+1));
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
		return new Nimber(black.getValue() ^ white.getValue());
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
	
	public static CGTValue max(CGTValue oldValue, CGTValue newValue, boolean blackTurn) {
		if(oldValue == null) {
			return newValue;
		}
		else if(oldValue.getClass() == Number.class && newValue.getClass() == Number.class) {
			
		} else if(oldValue.getClass() == Number.class && newValue.getClass() == Nimber.class) {
			return oldValue;
		} else if(oldValue.getClass() == Number.class && newValue.getClass() == Switch.class) {
			return oldValue;
		} else if(oldValue.getClass() == Number.class && newValue.getClass() == Infinitesimal.class) {
			return oldValue;
		} else if(oldValue.getClass() == Nimber.class && newValue.getClass() == Number.class) {
			return newValue;
		} else if(oldValue.getClass() == Nimber.class && newValue.getClass() == Nimber.class) {
			
		} else if(oldValue.getClass() == Nimber.class && newValue.getClass() == Switch.class) {
			return oldValue;
		} else if(oldValue.getClass() == Nimber.class && newValue.getClass() == Infinitesimal.class) {
			return oldValue;
		} else if(oldValue.getClass() == Switch.class && newValue.getClass() == Number.class) {
			return newValue;
		} else if(oldValue.getClass() == Switch.class && newValue.getClass() == Nimber.class) {
			return newValue;
		} else if(oldValue.getClass() == Switch.class && newValue.getClass() == Switch.class) {
			
		} else if(oldValue.getClass() == Switch.class && newValue.getClass() == Infinitesimal.class) {
			return oldValue;
		} else if(oldValue.getClass() == Infinitesimal.class && newValue.getClass() == Number.class) {
			return newValue;
		} else if(oldValue.getClass() == Infinitesimal.class && newValue.getClass() == Nimber.class) {
			return newValue;
		} else if(oldValue.getClass() == Infinitesimal.class && newValue.getClass() == Switch.class) {
			return newValue;
		} else if(oldValue.getClass() == Infinitesimal.class && newValue.getClass() == Infinitesimal.class) {
			
		}
		
		return null;
	}
}
