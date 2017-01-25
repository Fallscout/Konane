package game;

public class Move {
	private final int targetRow;
	private final int targetCol;
	private final int sourceRow;
	private final int sourceCol;
	private Piece capturedPiece;
	
	public Move(int sourceRow, int sourceCol, int targetRow, int targetCol) {
		this.sourceRow = sourceRow;
		this.sourceCol = sourceCol;
		this.targetRow = targetRow;
		this.targetCol = targetCol;
		this.capturedPiece = null;
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
	
	public Piece getCapturedPiece() {
		return this.capturedPiece;
	}

	public void setCapturedPiece(Piece capturedPiece) {
		this.capturedPiece = capturedPiece;
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
