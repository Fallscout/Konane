package game;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import engine.CGTValue;
import engine.ZobristHashCalculator;

public class Board {

	private final int rows;
	private final int cols;
	private final Piece[][] gameState;
	private final List<Piece> blackPieces;
	private final List<Piece> whitePieces;
	private final ZobristHashCalculator hashCalculator;

	private long zobristHash;

	public Board(Piece[][] gameState) {

		this.blackPieces = new ArrayList<>();
		this.whitePieces = new ArrayList<>();
		this.rows = gameState.length;
		this.cols = gameState[0].length;
		this.gameState = new Piece[this.rows][this.cols];

		for (int row = 0; row < gameState.length; row++) {
			if (gameState[row].length != this.cols) {
				throw new RuntimeException("Given board is not rectangular.");
			}
			for (int col = 0; col < gameState[row].length; col++) {
				Piece piece = gameState[row][col];
				if (piece != null) {
					this.gameState[row][col] = new Piece(piece);
					if (piece.isBlack()) {
						this.blackPieces.add(this.gameState[row][col]);
					} else {
						this.whitePieces.add(this.gameState[row][col]);
					}
				}
			}
		}

		this.hashCalculator = new ZobristHashCalculator(rows, cols);
		this.zobristHash = hashCalculator.calculateHash(this);
	}

	public Board(int rows, int cols, boolean blackInUpperLeftCorner) {

		this.rows = rows;
		this.cols = cols;

		this.gameState = new Piece[this.rows][this.cols];
		this.blackPieces = new ArrayList<>();
		this.whitePieces = new ArrayList<>();

		for (int row = 0; row < this.rows; row++) {
			for (int col = 0; col < this.cols; col++) {
				Piece piece;
				if ((col + row) % 2 == 0) {
					// insert starting color piece
					piece = new Piece(row, col, blackInUpperLeftCorner);

					if (blackInUpperLeftCorner) {
						this.blackPieces.add(piece);
					} else {
						this.whitePieces.add(piece);
					}
				} else {
					// insert other color piece
					piece = new Piece(row, col, !blackInUpperLeftCorner);

					if (!blackInUpperLeftCorner) {
						this.blackPieces.add(piece);
					} else {
						this.whitePieces.add(piece);
					}
				}

				this.gameState[row][col] = piece;
			}
		}

		Piece firstRemove = this.gameState[rows / 2][cols / 2 - 1];
		Piece secondRemove = this.gameState[rows / 2][cols / 2];

		if (firstRemove.isBlack()) {
			this.blackPieces.remove(firstRemove);
			this.whitePieces.remove(secondRemove);
		} else {
			this.whitePieces.remove(firstRemove);
			this.blackPieces.remove(secondRemove);
		}

		this.gameState[rows / 2][cols / 2 - 1] = null;
		this.gameState[rows / 2][cols / 2] = null;
		this.hashCalculator = new ZobristHashCalculator(rows, cols);
		this.zobristHash = hashCalculator.calculateHash(this);
	}

	public Board(Board board) {
		this.rows = board.rows;
		this.cols = board.cols;

		this.gameState = new Piece[board.rows][board.cols];
		this.blackPieces = new ArrayList<>();
		this.whitePieces = new ArrayList<>();

		for (Piece piece : board.blackPieces) {
			Piece copy = new Piece(piece);
			this.blackPieces.add(copy);
			this.gameState[copy.getRow()][copy.getCol()] = copy;
		}

		for (Piece piece : board.whitePieces) {
			Piece copy = new Piece(piece);
			this.whitePieces.add(copy);
			this.gameState[copy.getRow()][copy.getCol()] = copy;
		}

		this.hashCalculator = board.hashCalculator;
		this.zobristHash = board.zobristHash;
	}

	public void executeMove(Move move) {
        Piece movingPiece = this.gameState[move.getSourceRow()][move.getSourceCol()];

        Piece capturedPiece = this.gameState[(move.getSourceRow() + move.getTargetRow()) / 2][(move.getSourceCol() + move.getTargetCol())
            / 2];
        if (capturedPiece != null && capturedPiece.isBlack() != movingPiece.isBlack()) {
            this.removePiece(capturedPiece);
            move.setCapturedPiece(capturedPiece);
        } else {
            throw new IllegalStateException(
                "This is an invalid move. From (" + move.getSourceRow() + "|" + move.getSourceCol() + ") to (" + move.getTargetRow() + "|"
                    + move.getTargetCol() + ") on board:\n" + getBoardRepresentation());
        }

        movingPiece.setRow(move.getTargetRow());
        movingPiece.setCol(move.getTargetCol());
        this.gameState[move.getSourceRow()][move.getSourceCol()] = null;
        this.gameState[move.getTargetRow()][move.getTargetCol()] = movingPiece;
        this.zobristHash = hashCalculator.updateHash(zobristHash, move, movingPiece);
    }

	public void revertMove(Move move) {
        Piece movingPiece = this.gameState[move.getTargetRow()][move.getTargetCol()];
        Piece capturedPiece = move.getCapturedPiece();

        this.addPiece(capturedPiece);

        movingPiece.setRow(move.getSourceRow());
        movingPiece.setCol(move.getSourceCol());
        this.gameState[move.getSourceRow()][move.getSourceCol()] = movingPiece;
        this.gameState[move.getTargetRow()][move.getTargetCol()] = null;

        this.zobristHash = hashCalculator.updateHash(this.zobristHash, move, movingPiece);
    }

	public Board negate() {
		Board board = new Board(this);

		for (int row = 0; row < board.rows; row++) {
			for (int col = 0; col < board.cols; col++) {
				Piece piece = board.gameState[row][col];
				if (piece != null) {
					piece.setIsBlack(!piece.isBlack());
				}
			}
		}

		List<Piece> temp = new ArrayList<>(board.blackPieces);
		board.blackPieces.clear();
		board.blackPieces.addAll(this.whitePieces);
		board.whitePieces.clear();
		board.whitePieces.addAll(temp);

		board.zobristHash = board.hashCalculator.calculateHash(board);
		return board;
	}

	public List<Move> getLeftOptions() {
		List<Move> moves = new ArrayList<>();

		for (Piece piece : this.blackPieces) {
			moves.addAll(this.getValidMoves(piece));
		}

		return moves;
	}

	public List<Move> getRightOptions() {
		List<Move> moves = new ArrayList<>();

		for (Piece piece : this.whitePieces) {
			moves.addAll(this.getValidMoves(piece));
		}

		return moves;
	}

	public String getBoardRepresentation() {
		String ret = "";

		for (int row = 0; row < this.rows; row++) {
			for (int col = 0; col < this.cols; col++) {
				Piece piece = this.gameState[row][col];
				if (piece != null) {
					if (piece.isBlack()) {
						ret += "B|";
					} else {
						ret += "W|";
					}
				} else {
					ret += " |";
				}
			}
			ret += "\n";
		}

		return ret;
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	private List<Move> getValidMoves(Piece piece) {
        List<Move> moves = new ArrayList<>();

        int row = piece.getRow();
        int col = piece.getCol();

        if (row - 2 >= 0 && this.gameState[row - 1][col] != null && this.gameState[row - 1][col].isBlack() != piece.isBlack()
            && this.gameState[row - 2][col] == null) {
            moves.add(new Move(row, col, row - 2, col));
        }

        if (row + 2 < this.rows && this.gameState[row + 1][col] != null && this.gameState[row + 1][col].isBlack() != piece.isBlack()
            && this.gameState[row + 2][col] == null) {
            moves.add(new Move(row, col, row + 2, col));
        }

        if (col - 2 >= 0 && this.gameState[row][col - 1] != null && this.gameState[row][col - 1].isBlack() != piece.isBlack()
            && this.gameState[row][col - 2] == null) {
            moves.add(new Move(row, col, row, col - 2));
        }

        if (col + 2 < this.cols && this.gameState[row][col + 1] != null && this.gameState[row][col + 1].isBlack() != piece.isBlack()
            && this.gameState[row][col + 2] == null) {
            moves.add(new Move(row, col, row, col + 2));
        }

        return moves;
    }

	public boolean isEndgame() {
		int pieces = this.blackPieces.size() + this.whitePieces.size();
		int endgameBoardsize = (int) ((this.cols * this.rows - 2) * 0.3);
		return pieces < Math.max(8, endgameBoardsize);
	}

	private void removePiece(Piece piece) {
		if (piece.isBlack()) {
			this.blackPieces.remove(piece);
		} else {
			this.whitePieces.remove(piece);
		}
		this.gameState[piece.getRow()][piece.getCol()] = null;
	}
	
    private void addPiece(Piece piece) {
        if (piece.isBlack()) {
            this.blackPieces.add(piece);
        } else {
            this.whitePieces.add(piece);
        }
        this.gameState[piece.getRow()][piece.getCol()] = piece;
    }

	public Piece getPieceAtPosition(int row, int col) {
		return gameState[row][col];
	}

	public long getZobristHash() {
		return zobristHash;
	}
	
    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Board board = (Board) o;

        if (rows != board.rows)
            return false;
        if (cols != board.cols)
            return false;
        if (zobristHash != board.zobristHash)
            return false;
        if (!Arrays.deepEquals(gameState, board.gameState))
            return false;
        if (blackPieces != null ? !blackPieces.containsAll(board.blackPieces) : board.blackPieces != null)
            return false;
        if (whitePieces != null ? !whitePieces.containsAll(board.whitePieces) : board.whitePieces != null)
            return false;
        return hashCalculator != null ? hashCalculator.equals(board.hashCalculator) : board.hashCalculator == null;
    }
	
    @Override public int hashCode() {
        int result = rows;
        result = 31 * result + cols;
        result = 31 * result + Arrays.deepHashCode(gameState);
        result = 31 * result + (blackPieces != null ? blackPieces.hashCode() : 0);
        result = 31 * result + (whitePieces != null ? whitePieces.hashCode() : 0);
        result = 31 * result + (hashCalculator != null ? hashCalculator.hashCode() : 0);
        result = 31 * result + (int) (zobristHash ^ (zobristHash >>> 32));
        return result;
    }
}
