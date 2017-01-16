package engine;

import org.junit.Test;

public class ABCGTSolverTest {

    @Test(expected = IllegalArgumentException.class) public void determineWinner_withDoubleNullValue() {
        CABSolverSerial.determineWinner(null, null);
    }

    @Test(expected = IllegalArgumentException.class) public void determineWinner_withLeftNullValue() {
        CABSolverSerial.determineWinner(null, new Number(1));
    }

    @Test(expected = IllegalArgumentException.class) public void determineWinner_withRightNullValue() {
        CABSolverSerial.determineWinner(new Number(1), null);
    }
}
