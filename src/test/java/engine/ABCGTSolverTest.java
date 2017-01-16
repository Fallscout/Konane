package engine;

import org.junit.Test;

public class ABCGTSolverTest {

    @Test(expected = IllegalArgumentException.class) public void determineWinner_withDoubleNullValue() {
        CABSolver.determineWinner(null, null);
    }

    @Test(expected = IllegalArgumentException.class) public void determineWinner_withLeftNullValue() {
        CABSolver.determineWinner(null, new Number(1));
    }

    @Test(expected = IllegalArgumentException.class) public void determineWinner_withRightNullValue() {
        CABSolver.determineWinner(new Number(1), null);
    }
}
