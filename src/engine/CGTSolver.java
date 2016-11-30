package engine;

import java.util.ArrayList;
import java.util.List;

import game.Board;
import game.Move;
import game.Piece;

public class CGTSolver {

	public OutcomeType solve(Board board, boolean blacksTurn) {

		CGTGame result = this.calculate(board, blacksTurn);

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

	private CGTGame calculate(Board board, boolean blacksTurn) {
		List<Move> leftOptions = board.getLeftOptions();
		List<Move> rightOptions = board.getRightOptions();
		//remark: if left's best option is positive and right's best option is positive
		//left wins and vice versa (see Berlekamp et al., p. 47)
		//Adding nimbers and numbers, see Berlekamp et al., p. 59
		
		if (leftOptions.isEmpty() && rightOptions.isEmpty()) {
			//{ | } = 0 (Berlekamp et al., p.27)
			return new CGTGame();
		} 
//		else if (leftOptions.isEmpty() && !rightOptions.isEmpty()) {
//			//{ | -n} = -(n+1) (Berlekamp et al., p.39)
//			return new Number(-(rightOptions.size()+1));
//		} else if (!leftOptions.isEmpty() && rightOptions.isEmpty()) {
//			//{n| }=n+1 (Berlekamp et al., p. 27)
//			return new Number(leftOptions.size()+1);
//		} 
		else {
			CGTValue blackOutcome = null;
			CGTValue whiteOutcome = null;

			CGTGame game = null;
			for (Move option : leftOptions) {
				board.executeMove(option);
				game = calculate(board, !blacksTurn);
				board.revertMove(option);
				
				if(blackOutcome == null) {
					blackOutcome = value;
				}
			}

			for (Move option : rightOptions) {
				board.executeMove(option);
				game = calculate(board, !blacksTurn);
				board.revertMove(option);
				
				if(whiteOutcome == null) {
					whiteOutcome = value;
				}
			}
			
			if(blackOutcome == null && whiteOutcome != null) {
				return new Number(-1);
			} else if(blackOutcome != null && whiteOutcome == null) {
				return new Number(1);
			} else if(blackOutcome != null && whiteOutcome != null) {
				if(blackOutcome.getClass() == Number.class && whiteOutcome.getClass() == Number.class) {
					Number black = (Number)blackOutcome;
					Number white = (Number)whiteOutcome;
					
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
				} else if(blackOutcome.getClass() == Number.class && whiteOutcome.getClass() == Nimber.class) {
					
				} else if(blackOutcome.getClass() == Nimber.class && whiteOutcome.getClass() == Number.class) {
					
				} else if(blackOutcome.getClass() == Nimber.class && whiteOutcome.getClass() == Nimber.class) {
					
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
