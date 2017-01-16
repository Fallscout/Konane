package engine;


import game.Board;

public abstract class Solver {

    private int counter;
    private int counterTT;

    public abstract OutcomeType solve(Board board);

    public int getCounter() {
        return counter;
    }

    public int getCounterTT() {
        return counterTT;
    }

    public abstract void printCounter();
}
