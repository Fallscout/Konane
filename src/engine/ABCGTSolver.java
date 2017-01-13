package engine;

import java.util.List;

import game.Board;
import game.Move;

import java.util.Collections;
import java.util.List;

public class ABCGTSolver {

    private final TTEntry[] tTable = new TTEntry[(int) Math.pow(2, 24)];

    private static final CGTValue ZERO = new Number(0);
    private static final CGTValue ONE = new Number(1);
    private static final CGTValue NEGATIVE_ONE = new Number(-1);

    //Remark: Cannot use NegaMax, because some CGTValues cannot be negated(?)
    public OutcomeType solve(Board board) {
        CGTValue blackOutcome = null;
        CGTValue whiteOutcome = null;

        List<Move> blackMoves = board.getLeftOptions();
        moveOrdering(blackMoves, board, true);
        for (Move move : blackMoves) {
            board.executeMove(move);
            blackOutcome = solve(board, false, new Number(-100), new Number(100), 1);
            board.revertMove(move);
            if (blackOutcome.greater(ZERO)) {
                break;
            }
        }

        List<Move> whiteMoves = board.getRightOptions();
        moveOrdering(whiteMoves, board, false);
        for (Move move : whiteMoves) {
            board.executeMove(move);
            whiteOutcome = solve(board, true, new Number(-100), new Number(100), 1);
            board.revertMove(move);
            if (whiteOutcome.less(ZERO)) {
                break;
            }
        }
        CGTValue outcome = CGTValue.getOutcome(blackOutcome, whiteOutcome);
        return this.determineWinner(outcome);
    }

    private void moveOrdering(List<Move> moves, Board board, boolean blacksMove) {
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

    private CGTValue solve(Board board, boolean blackTurn, CGTValue alpha, CGTValue beta, int ply) {
        // Lookup in TT
        long boardHash = board.getZobristHash();
        TTEntry ttEntry = tTable[getIndexOfHash(boardHash)];
        if (ttEntry != null) {
            if (ttEntry.getZobristHash() == boardHash) {
                return ttEntry.getCgtValue();
            }
        }

        CGTValue returnValue = null;
        Move bestLeftOption = null;
        Move bestRightOption = null;

        List<Move> availableMoves;
        if (blackTurn) {
            availableMoves = board.getLeftOptions();
        } else {
            availableMoves = board.getRightOptions();
        }

        if (availableMoves.isEmpty()) {
            if (blackTurn) {
                returnValue = new Number(-1); //White wins
            } else {
                returnValue = new Number(1); //Black wins
            }
        }

        if (board.isEndgame()) {
            //Do CGT Stuff
            CGTValue cgtvalue = new CGTSolver().calculate(board);
            if (cgtvalue != null) {
                returnValue = cgtvalue;
            }
        }

        CGTValue value;

        if (blackTurn) {
            for (Move move : availableMoves) {
                board.executeMove(move);
                value = solve(board, false, alpha, beta, ply + 1);
                board.revertMove(move);

                if (value.greaterEqual(beta)) {
                    returnValue = beta;
                    bestLeftOption = move;
                }
                if (value.greater(alpha)) {
                    alpha = value;
                }
            }
        } else {
            for (Move move : availableMoves) {
                board.executeMove(move);
                value = solve(board, true, alpha, beta, ply + 1);
                board.revertMove(move);

                if (value.lessEquals(alpha)) {
                    returnValue = alpha;
                    bestRightOption = move;
                }
                if (value.less(beta)) {
                    beta = value;
                }
            }
        }

        tTable[getIndexOfHash(boardHash)] = new TTEntry(boardHash, bestLeftOption, bestRightOption, returnValue);

        return returnValue;
    }

    private OutcomeType determineWinner(CGTValue outcome) {
        if (outcome instanceof Number){
            double value = ((Number) outcome).getValue();
            if (value<0){
                return OutcomeType.WHITE;
            }else if(value==0){
                return OutcomeType.SECOND;
            }else{
                return OutcomeType.BLACK;
            }
        }
        return OutcomeType.FIRST;
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
