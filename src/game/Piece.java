package game;

public class Piece {
	private boolean isBlack;
	private int row;
	private int col;
	
	public Piece(int row, int col, boolean isBlack) {
		this.row = row;
		this.col = col;
		this.isBlack = isBlack;
	}
	
	public Piece(Piece piece) {
		this.row = piece.row;
		this.col = piece.col;
		this.isBlack = piece.isBlack;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public boolean isBlack() {
		return isBlack;
	}
	
	public void setIsBlack(boolean black) {
		this.isBlack = black;
	}

	public void setRow(int row) {
		this.row = row;
	}
	
	public void setCol(int col) {
		this.col = col;
	}
}
