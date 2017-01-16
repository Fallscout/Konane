package engine;


import game.Board;

public abstract class Solver {

    private int counter;
    private int counterTT;

    public abstract OutcomeType solve(Board board);

    public OutcomeType solveWithTime(Board board){
        Long starttime = System.currentTimeMillis();
        OutcomeType result = solve(board);
        System.out.println("Time (ms): "+ (System.currentTimeMillis()-starttime));
        return result;
    }

    public int getCounter() {
        return counter;
    }

    public int getCounterTT() {
        return counterTT;
    }

    public abstract void printCounter();
}
