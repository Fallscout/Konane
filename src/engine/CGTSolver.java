package engine;

import java.util.List;

import game.Board;
import game.Move;
import game.Piece;

public class CGTSolver {

	public OutcomeType solve(Board board, boolean blacksTurn) {

		CGTValue result = this.calculate(board, blacksTurn);

		if (result.getClass() == Number.class) {
			Number number = (Number) result;
			if (number.getValue() == 0) {
				return OutcomeType.SECOND;
			} else if (number.getValue() > 0) {
				return OutcomeType.BLACK;
			} else if (number.getValue() < 0) {
				return OutcomeType.WHITE;
			}
		} else if (result.getClass() == Nimber.class) {
			return OutcomeType.FIRST;
		}

		return null;
	}

	private CGTValue calculate(Board board, boolean blacksTurn) {
		List<Move> leftOptions = board.getLeftOptions();
		List<Move> rightOptions = board.getRightOptions();
		//remark: if left's best option is positive and right's best option is positive
		//left wins and vice versa (see Berlekamp et al., p. 47)
		//Adding nimbers and numbers, see Berlekamp et al., p. 59
		
		if (leftOptions.isEmpty() && rightOptions.isEmpty()) {
			//{ | } = 0 (Berlekamp et al., p.27)
			return new Number(0);
		} 
		else {
			CGTValue blackOutcome = null;
			CGTValue whiteOutcome = null;

			CGTValue value = null;
			for (Move option : leftOptions) {
				board.executeMove(option);
				value = calculate(board, !blacksTurn);
				board.revertMove(option);
				
				//TODO: Discard blackOutcome if new value is better
				if(blackOutcome == null) {
					blackOutcome = value;
				}
			}

			for (Move option : rightOptions) {
				board.executeMove(option);
				value = calculate(board, !blacksTurn);
				board.revertMove(option);
				
				//TODO: Discard whiteOutcome if new value is better
				if(whiteOutcome == null) {
					whiteOutcome = value;
				}
			}
			
			if(blackOutcome == null && whiteOutcome != null) {
//				return new Number(-1);
				//{ | -n} = -(n+1) (Berlekamp et al., p.39)
				//What about outcome types other than number?
				return new Number(-(((Number)whiteOutcome).getValue()+1));
			} else if(blackOutcome != null && whiteOutcome == null) {
//				return new Number(1);
				//{n| }=n+1 (Berlekamp et al., p. 27)
				return new Number(((Number)blackOutcome).getValue()+1);
			} else if(blackOutcome != null && whiteOutcome != null) {
				if(blackOutcome.getClass() == Number.class && whiteOutcome.getClass() == Number.class) {
					return CGTValue.getOutcome((Number)blackOutcome, (Number)whiteOutcome);
				} else if(blackOutcome.getClass() == Number.class && whiteOutcome.getClass() == Nimber.class) {
					return CGTValue.getOutcome((Number)blackOutcome, (Nimber)whiteOutcome);
				} else if(blackOutcome.getClass() == Number.class && whiteOutcome.getClass() == Switch.class) {
					return CGTValue.getOutcome((Number)blackOutcome, (Switch)whiteOutcome);
				} else if(blackOutcome.getClass() == Number.class && whiteOutcome.getClass() == Infinitesimal.class) {
					return CGTValue.getOutcome((Number)blackOutcome, (Infinitesimal)whiteOutcome);
				} else if(blackOutcome.getClass() == Nimber.class && whiteOutcome.getClass() == Number.class) {
					return CGTValue.getOutcome((Nimber)blackOutcome, (Number)whiteOutcome);
				} else if(blackOutcome.getClass() == Nimber.class && whiteOutcome.getClass() == Nimber.class) {
					return CGTValue.getOutcome((Nimber)blackOutcome, (Nimber)whiteOutcome);
				} else if(blackOutcome.getClass() == Nimber.class && whiteOutcome.getClass() == Switch.class) {
					return CGTValue.getOutcome((Nimber)blackOutcome, (Switch)whiteOutcome);
				} else if(blackOutcome.getClass() == Nimber.class && whiteOutcome.getClass() == Infinitesimal.class) {
					return CGTValue.getOutcome((Nimber)blackOutcome, (Infinitesimal)whiteOutcome);
				} else if(blackOutcome.getClass() == Switch.class && whiteOutcome.getClass() == Number.class) {
					return CGTValue.getOutcome((Switch)blackOutcome, (Number)whiteOutcome);
				} else if(blackOutcome.getClass() == Switch.class && whiteOutcome.getClass() == Nimber.class) {
					return CGTValue.getOutcome((Switch)blackOutcome, (Nimber)whiteOutcome);
				} else if(blackOutcome.getClass() == Switch.class && whiteOutcome.getClass() == Switch.class) {
					return CGTValue.getOutcome((Switch)blackOutcome, (Switch)whiteOutcome);
				} else if(blackOutcome.getClass() == Switch.class && whiteOutcome.getClass() == Infinitesimal.class) {
					return CGTValue.getOutcome((Switch)blackOutcome, (Infinitesimal)whiteOutcome);
				} else if(blackOutcome.getClass() == Infinitesimal.class && whiteOutcome.getClass() == Number.class) {
					return CGTValue.getOutcome((Infinitesimal)blackOutcome, (Number)whiteOutcome);
				} else if(blackOutcome.getClass() == Infinitesimal.class && whiteOutcome.getClass() == Nimber.class) {
					return CGTValue.getOutcome((Infinitesimal)blackOutcome, (Nimber)whiteOutcome);
				} else if(blackOutcome.getClass() == Infinitesimal.class && whiteOutcome.getClass() == Switch.class) {
					return CGTValue.getOutcome((Infinitesimal)blackOutcome, (Switch)whiteOutcome);
				} else if(blackOutcome.getClass() == Infinitesimal.class && whiteOutcome.getClass() == Infinitesimal.class) {
					return CGTValue.getOutcome((Infinitesimal)blackOutcome, (Infinitesimal)whiteOutcome);
				}
			}
			
			return null;
		}
	}

	private CGTValue LT(Piece[] row, Piece[] col) {
		return null;
	}
	
	private CGTValue LOT(Piece[] row, Piece[] col, int offset) {
		return null;
	}
	
	private CGTValue L(Piece[] row) {
		int length = 0;
		boolean lastBlack = false;
		for (int i = 0; i < row.length; i++) {
			Piece piece = row[i];
			if (piece != null) {
				if (piece.getColor() && !lastBlack) {
					length++;
					lastBlack = true;
				} else if (piece.getColor() && lastBlack) {
					length = 0;
				} else if (!piece.getColor() && lastBlack) {
					length++;
				} else if (!piece.getColor() && !lastBlack) {
					length = 0;
				}
			}
		}

		if (length % 2 == 1) {
			return new Number(-(length / 2));
		} else if (length % 4 == 0) {
			return new Number(0);
		} else if (length % 4 == 2) {
			return new Nimber(1);
		}
		
		return null;
	}
}
