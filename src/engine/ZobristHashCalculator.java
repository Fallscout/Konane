package engine;

import java.security.SecureRandom;

import game.Move;
import game.Piece;

public class ZobristHashCalculator {
	private static long[][][] Randoms;
	private static long RedPlayerRandom;
	
	public static void Init(int rows, int cols) {
		Randoms = new long[rows][cols][2];
		SecureRandom random = new SecureRandom();
		for(int board = 0; board < 4; board++) {
			for(int row = 0; row < rows; row++) {
				for(int col = 0; col < cols; col++) {
					Randoms[row][col][board] = random.nextLong();
				}
			}
		}

		RedPlayerRandom = random.nextLong();
	}
	
//	public static long CalculateHash(Piece[][] board, PieceColor currentPlayer) {
//		long ret = 0;
//
//		for (int i = 0; i < board.length; i++) {
//			for (int j = 0; j < board[i].length; j++) {
//				Piece piece = board[i][j];
//				if (piece != null) {
////					ret ^= Randoms[i][j][GetFactor(piece)];
//				}
//			}
//		}
//		
//		if(currentPlayer == PieceColor.WHITE) {
//			ret ^= RedPlayerRandom;
//		}
//
//		return ret;
//	}
	
	public static long UpdateHash(long hash, Piece movingPiece, Move move) {
		long ret = hash;

//		ret ^= RedPlayerRandom;
//
//		ret ^= Randoms[move.getSourceRow()][move.getSourceCol()][GetFactor(movingPiece)];
//		ret ^= Randoms[move.getTargetRow()][move.getTargetCol()][GetFactor(movingPiece)];
//
//		for (Piece removedPiece : move.getCapturedPieces()) {
//			ret ^= Randoms[removedPiece.getRow()][removedPiece.getCol()][GetFactor(removedPiece)];
//		}

		return ret;
	}
}
