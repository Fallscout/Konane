package engine;

import java.util.List;

import game.Board;
import game.Move;

public class ABCGTSolver {
	
	private static final CGTValue ZERO = new Number(0);
	private static final CGTValue ONE = new Number(1);
	private static final CGTValue NEGATIVE_ONE = new Number(-1);
	
	//Remark: Cannot use NegaMax, because some CGTValues cannot be negated(?)
	public OutcomeType solve(Board board) {
		CGTValue blackOutcome = null;
		CGTValue whiteOutcome = null;
		
		List<Move> blackMoves = board.getLeftOptions();
		
		for(Move move : blackMoves) {
			board.executeMove(move);
			blackOutcome = solve(board, false, new Number(-100), new Number(100), 1);
			board.revertMove(move);
			if(blackOutcome.greater(ZERO)) {
				break;
			}
		}
		
		List<Move> whiteMoves = board.getRightOptions();
		
		for(Move move : whiteMoves) {
			board.executeMove(move);
			whiteOutcome = solve(board, true, new Number(-100), new Number(100), 1);
			board.revertMove(move);
			if(whiteOutcome.less(ZERO)) {
				break;
			}
		}
		
		return this.determineWinner(blackOutcome, whiteOutcome);
	}
	
	private CGTValue solve(Board board, boolean blackTurn, CGTValue alpha, CGTValue beta, int ply) {
		List<Move> availableMoves;
		if(blackTurn) {
			availableMoves = board.getLeftOptions();
		} else {
			availableMoves = board.getRightOptions();
		}
		
		if(availableMoves.isEmpty()) {
			if(blackTurn) {
				return new Number(-1); //White wins
			} else {
				return new Number(1); //Black wins
			}
		}
		
		if(board.isEndgame()) {
			//Do CGT Stuff
			CGTValue cgtvalue = new CGTSolver().calculate(board);
			if(cgtvalue != null) {
				return cgtvalue;
			}
		}
		
		CGTValue value;
		
		if(blackTurn) {
			for(Move move : availableMoves) {
				board.executeMove(move);
				value = solve(board, !blackTurn, alpha, beta, ply+1);
				board.revertMove(move);
				
				if(value.greaterEqual(beta)) return beta;
				if(value.greater(alpha)) alpha = value;
			}
		} else {
			for(Move move : availableMoves) {
				board.executeMove(move);
				value = solve(board, !blackTurn, alpha, beta, ply+1);
				board.revertMove(move);
				
				if(value.lessEquals(alpha)) return alpha;
				if(value.less(beta)) beta = value;
			}
		}
		
		return null;
	}
	
	private OutcomeType determineWinner(CGTValue black, CGTValue white) {
		if(black == null && white == null) {
			
		}
		else if(black == null && white != null) {
			
		}
		else if(black != null && white == null) {
			
		} else {
			if(black instanceof Number) {
				if(white instanceof Number) {
					
				} else if(white instanceof Nimber) {
					
				} else if(white instanceof Switch) {
					
				} else if(white instanceof Infinitesimal) {
					
				}
			} else if(black instanceof Nimber) {
				if(white instanceof Number) {
					
				} else if(white instanceof Nimber) {
					
				} else if(white instanceof Switch) {
					
				} else if(white instanceof Infinitesimal) {
					
				}
			} else if(black instanceof Switch) {
				if(white instanceof Number) {
					
				} else if(white instanceof Nimber) {
					
				} else if(white instanceof Switch) {
					
				} else if(white instanceof Infinitesimal) {
					
				}
			} else if(black instanceof Infinitesimal) {
				if(white instanceof Number) {
					
				} else if(white instanceof Nimber) {
					
				} else if(white instanceof Switch) {
					
				} else if(white instanceof Infinitesimal) {
					
				}
			}
		}
		
		throw new IllegalArgumentException("Cannot determine winner for given values.");
	}
}
