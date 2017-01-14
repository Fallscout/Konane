package game;

import java.util.ArrayList;
import java.util.List;

public class Move {
	private int targetRow;
	private int targetCol;
	private int sourceRow;
	private int sourceCol;
	private List<Piece> capturedPieces;
	
	public Move(int sourceRow, int sourceCol, int targetRow, int targetCol) {
		this.sourceRow = sourceRow;
		this.sourceCol = sourceCol;
		this.targetRow = targetRow;
		this.targetCol = targetCol;
		this.capturedPieces = new ArrayList<Piece>();
	}
	
	public int getTargetRow() {
		return this.targetRow;
	}
	
	public int getTargetCol() {
		return this.targetCol;
	}
	
	public int getSourceRow() {
		return this.sourceRow;
	}
	
	public int getSourceCol() {
		return this.sourceCol;
	}
	
	public List<Piece> getCapturedPieces() {
		return this.capturedPieces;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null || obj.getClass() != Move.class) return false;
		Move move = (Move)obj;
		return this.getSourceCol() == move.getSourceCol() &&
				this.getSourceRow() == move.getSourceRow() &&
				this.getTargetCol() == move.getTargetCol() &&
				this.getTargetRow() == move.getTargetRow();
	}
}
