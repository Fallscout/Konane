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

	@Override public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Piece piece = (Piece) o;

		if (isBlack != piece.isBlack)
			return false;
		if (row != piece.row)
			return false;
		return col == piece.col;
	}

	@Override public int hashCode() {
		int result = (isBlack ? 1 : 0);
		result = 31 * result + row;
		result = 31 * result + col;
		return result;
	}
}
