package engine;

import org.junit.Test;

public class ABCGTSolverTest {

    @Test(expected = IllegalArgumentException.class) public void determineWinner_withDoubleNullValue() {
        ABCGTSolver.determineWinner(null, null);
    }

    @Test(expected = IllegalArgumentException.class) public void determineWinner_withLeftNullValue() {
        ABCGTSolver.determineWinner(null, new Number(1));
    }

    @Test(expected = IllegalArgumentException.class) public void determineWinner_withRightNullValue() {
        ABCGTSolver.determineWinner(new Number(1), null);
    }
}
