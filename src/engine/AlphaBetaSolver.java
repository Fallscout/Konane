package engine;

import java.util.List;

import game.Board;
import game.Move;

public class AlphaBetaSolver {

	public OutcomeType solve(Board board) {
		int blackOutcome = 0;
		int whiteOutcome = 0;
		
		List<Move> blackMoves = board.getLeftOptions();
		
		for(Move move : blackMoves) {
			board.executeMove(move);
			blackOutcome = -solve(board, false, -1, 1, 1);
			board.revertMove(move);
			if(blackOutcome == 1) {
				break;
			}
		}
		
		List<Move> whiteMoves = board.getRightOptions();
		
		for(Move move : whiteMoves) {
			board.executeMove(move);
			whiteOutcome = -solve(board, true, -1, 1, 1);
			board.revertMove(move);
			if(whiteOutcome == 1) {
				break;
			}
		}
		
		if(blackOutcome == 1 && whiteOutcome == 1) {
			return OutcomeType.FIRST;
		} else if(blackOutcome == -1 && whiteOutcome == 1) {
			return OutcomeType.WHITE;
		} else if(blackOutcome == 1 && whiteOutcome == -1) {
			return OutcomeType.BLACK;
		} else {
			return OutcomeType.SECOND;
		}
	}
	
	private int solve(Board board, boolean blackTurn, int alpha, int beta, int ply) {
		List<Move> availableMoves;
		if(blackTurn) {
			availableMoves = board.getLeftOptions();
		} else {
			availableMoves = board.getRightOptions();
		}
		
		if(availableMoves.isEmpty()) {
			return -1;
		}
		
		int score = Integer.MIN_VALUE;
		int value;
		
		for(Move move : availableMoves) {
			board.executeMove(move);
			value = -solve(board, !blackTurn, -beta, -alpha, ply+1);
			board.revertMove(move);
			
			if(value > score) score = value;
			if(score > alpha) alpha = score;
			if(score >= beta) break;
		}
		
		return score;
	}
}
