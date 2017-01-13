package engine;

import java.util.List;

import game.Board;
import game.Move;
import game.Piece;

public class CGTSolver {

    private final TTEntry[] tTable = new TTEntry[(int) Math.pow(2, 24)];
    private int counterPostTT = 0;
    private int counterPreTT = 0;
    /**
     * Solves a given board and returns the {@link OutcomeType}
     *
     * @param board The board that shall be solved
     * @return The {@link OutcomeType} of the game (FIRST, SECOND, BLACK, WHITE)
     */
    public OutcomeType solve(Board board) {

        CGTValue result = this.calculate(board);
        System.out.println("CGT Solver:");
        System.out.println("Counter - nodes visited: "+ counterPostTT);
        System.out.println("Counter - nodes looked up in TT: "+ (counterPreTT-counterPostTT));

        counterPreTT=0;
        counterPostTT=0;

        System.out.println("Result: " + result);

        if (result != null) {
            Class<? extends CGTValue> resultingClass = result.getClass();

            if (resultingClass == Number.class) {
                Number number = (Number) result;
                if (number.getValue() == 0) {
                    return OutcomeType.SECOND;
                } else if (number.getValue() > 0) {
                    return OutcomeType.BLACK;
                } else if (number.getValue() < 0) {
                    return OutcomeType.WHITE;
                }
            } else if (resultingClass == Nimber.class) {
                return OutcomeType.FIRST;
            } else if (resultingClass == Switch.class) {
                // TODO:
            } else if (resultingClass == Infinitesimal.class) {
                // TODO:
            } else {
                throw new IllegalStateException("The result class is not supported yet: " + resultingClass);
            }
        }

        return null;
    }

    /**
     * Returns the CGTValue of the given <code>board</code>.
     *
     * @param board The board one wants to calculate the {@link CGTValue} for.
     * @return The board's value
     */
    //TODO: https://github.com/Fallscout/Konane/issues/3
    public CGTValue calculate(Board board) {
        counterPreTT++;
        // Lookup in TT
        long boardHash = board.getZobristHash();
        TTEntry ttEntry = tTable[getIndexOfHash(boardHash)];
        if (ttEntry != null) {
            if (ttEntry.getZobristHash() == boardHash) {
                return ttEntry.getCgtValue();
            }
        }
        counterPostTT++;

        // Get the possible options of both players
        List<Move> leftOptions = board.getLeftOptions();
        List<Move> rightOptions = board.getRightOptions();
        // remark: if left's best option is positive and right's best option is
        // positive
        // left wins and vice versa (see Berlekamp et al., p. 47)
        // Adding nimbers and numbers, see Berlekamp et al., p. 59

        CGTValue cgtValue;

        Move bestLeftOption = null;
        Move bestRightOption = null;

        CGTValue leftOutcome = null;
        CGTValue rightOutcome = null;

        // Calculate values of left options
        for (Move option : leftOptions) {
            board.executeMove(option);
            CGTValue value = calculate(board);
            board.revertMove(option);

            CGTValue max = CGTValue.max(leftOutcome, value);
            if (max != leftOutcome) {
                bestLeftOption = option;
                leftOutcome = max;
            }
        }

        // Calculate values of right options
        for (Move option : rightOptions) {
            board.executeMove(option);
            CGTValue value = calculate(board);
            board.revertMove(option);

            CGTValue max = CGTValue.max(rightOutcome, value);
            if (max != rightOutcome) {
                bestRightOption = option;
                rightOutcome = max;
            }
        }

        // Get the final outcome and store it in the transposition table
        cgtValue = CGTValue.getOutcome(leftOutcome, rightOutcome);
        tTable[getIndexOfHash(boardHash)] = new TTEntry(boardHash, bestLeftOption, bestRightOption, cgtValue);

        return cgtValue;
    }

    private CGTValue LT(Piece[] row, Piece[] col) {
        return null;
    }

    private CGTValue LOT(Piece[] row, Piece[] col, int offset) {
        return null;
    }

    private CGTValue L(Piece[] row) {
        int length = 0;
        boolean lastBlack = false;
        for (Piece piece : row) {
            if (piece != null) {
                if (piece.isBlack() && !lastBlack) {
                    length++;
                    lastBlack = true;
                } else if (piece.isBlack() && lastBlack) {
                    length = 0;
                } else if (!piece.isBlack() && lastBlack) {
                    length++;
                } else if (!piece.isBlack() && !lastBlack) {
                    length = 0;
                }
            }
        }

        if (length % 2 == 1) {
            return new Number(-(length / 2));
        } else if (length % 4 == 0) {
            return new Number(0);
        } else if (length % 4 == 2) {
            return new Nimber(1);
        }

        return null;
    }

    /** This method returns the primary hash code by using a bit mask
     * @param zobristHash The complete hash
     * @return the primary hash code
     */
    private int getIndexOfHash(long zobristHash) {
        return (int) Math.abs(zobristHash & 0xFFFFFF);
    }
}
