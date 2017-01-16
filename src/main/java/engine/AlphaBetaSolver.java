package engine;

import java.util.Collections;
import java.util.List;

import game.Board;
import game.Move;

public class AlphaBetaSolver extends Solver {

    private final SimpleTTEntry[] tTable = new SimpleTTEntry[(int) Math.pow(2, 24)];
    private int counter;
    private int cutoffs;
    private int cutoffsFirstMove;

    @Override public void printCounter() {

    }

    @Override public OutcomeType solve(Board board) {
        int blackOutcome = -1;
        int whiteOutcome = -1;

        List<Move> blackMoves = board.getLeftOptions();
        orderMoves(blackMoves, board, true);
        for (Move move : blackMoves) {
            board.executeMove(move);
            blackOutcome = -solve(board, false, -1, 1, 1);
            board.revertMove(move);
            if (blackOutcome == 1) {
                break;
            }
        }

        List<Move> whiteMoves = board.getRightOptions();
        orderMoves(whiteMoves, board, false);
        for (Move move : whiteMoves) {
            board.executeMove(move);
            whiteOutcome = -solve(board, true, -1, 1, 1);
            board.revertMove(move);
            if (whiteOutcome == 1) {
                break;
            }
        }
        System.out.println("Alpha-Beta solver:");
        System.out.println("Counter - nodes visited: " + counter);
        System.out.println("Cutoffs absolute: " + cutoffs);
        System.out.println("Cutoffs first move (%): " + (cutoffsFirstMove / (cutoffs + 0.0)));

        if (blackOutcome == 1 && whiteOutcome == 1) {
            return OutcomeType.FIRST;
        } else if (blackOutcome == -1 && whiteOutcome == 1) {
            return OutcomeType.WHITE;
        } else if (blackOutcome == 1 && whiteOutcome == -1) {
            return OutcomeType.BLACK;
        } else {
            return OutcomeType.SECOND;
        }
    }

    private void orderMoves(List<Move> moves, Board board, boolean blacksMove) {
        int[] possibleOpponentsMoves = new int[moves.size()];
        for (int i = 0; i < moves.size(); i++) {
            Move move = moves.get(i);
            board.executeMove(move);
            int opponentsMoves;
            if (blacksMove) {
                opponentsMoves = board.getRightOptions().size();
            } else {
                opponentsMoves = board.getLeftOptions().size();
            }
            board.revertMove(move);
            possibleOpponentsMoves[i] = opponentsMoves;
        }

        for (int i = 0; i < possibleOpponentsMoves.length; i++) {
            for (int j = i + 1; j < possibleOpponentsMoves.length; j++) {
                int opponentsMoves = possibleOpponentsMoves[i];
                int opponentsMoves2 = possibleOpponentsMoves[j];

                if (opponentsMoves > opponentsMoves2) {
                    possibleOpponentsMoves[i] = opponentsMoves2;
                    possibleOpponentsMoves[j] = opponentsMoves;

                    Collections.swap(moves, i, j);
                }
            }
        }
    }

    private int solve(Board board, boolean blackTurn, int alpha, int beta, int ply) {
        // Lookup in TT
        long boardHash = board.getZobristHash();
        SimpleTTEntry ttEntry = tTable[getIndexOfHash(boardHash)];
        if (ttEntry != null) {
            if (ttEntry.getZobristHash() == boardHash) {
                if (blackTurn && ttEntry.getLeftValue() != null) {
                    return ttEntry.getLeftValue();
                } else if (!blackTurn && ttEntry.getRightValue() != null) {
                    return ttEntry.getRightValue();
                }
            } else {
                ttEntry = null;
            }
        }
        counter++;

        List<Move> availableMoves;
        if (blackTurn) {
            availableMoves = board.getLeftOptions();
        } else {
            availableMoves = board.getRightOptions();
        }

        if (availableMoves.isEmpty()) {
            //			counter++;
            return -1;
        } else {
            orderMoves(availableMoves, board, blackTurn);
        }

        int score = Integer.MIN_VALUE;
        int value;
        int moveCounter = 0;
        for (Move move : availableMoves) {
            board.executeMove(move);
            value = -solve(board, !blackTurn, -beta, -alpha, ply + 1);
            board.revertMove(move);

            if (value > score) {
                score = value;
            }
            if (score > alpha) {
                alpha = score;
            }
            if (score >= beta) {
                cutoffs++;
                if (moveCounter == 0) {
                    cutoffsFirstMove++;
                }
                break;
            }
            moveCounter++;
        }

        if (ttEntry == null) {
            if (blackTurn) {
                tTable[getIndexOfHash(boardHash)] = new SimpleTTEntry(boardHash, score, null);
            } else {
                tTable[getIndexOfHash(boardHash)] = new SimpleTTEntry(boardHash, null, score);
            }
        } else {
            if (blackTurn) {
                ttEntry.setLeftValue(score);
            } else {
                ttEntry.setRightValue(score);
            }
        }
        return score;
    }

    /**
     * This method returns the primary hash code by using a bit mask
     *
     * @param zobristHash The complete hash
     * @return the primary hash code
     */
    private int getIndexOfHash(long zobristHash) {
        return (int) Math.abs(zobristHash & 0xFFFFFF);
    }
}