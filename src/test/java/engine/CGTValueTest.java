package engine;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class CGTValueTest {

    @Test public void getOutcome_null_negNumber() {
        CGTValue outcome = CGTValue.getOutcome(null, new Number(-1));
        Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(-2)));
    }

    @Test public void getOutcome_null_posNumber() {
        CGTValue outcome = CGTValue.getOutcome(null, new Number(2));
        Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(1)));
    }

    @Test public void getOutcome_null_zeroNumber() {
        CGTValue outcome = CGTValue.getOutcome(null, new Number(0));
        Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(-1)));
    }

    @Test(expected = IllegalStateException.class) public void getOutcome_null_Nimber() {
        CGTValue.getOutcome(null, new Nimber(1));
    }

    @Test(expected = IllegalStateException.class) public void getOutcome_null_Switch() {
        CGTValue.getOutcome(null, new Switch(new Number(1), new Number(0)));
    }

    @Test(expected = IllegalStateException.class) public void getOutcome_null_Infinitesimal() {
        CGTValue.getOutcome(null, new Infinitesimal(1));
    }

    @Test public void getOutcome_zeroNumber_null() {
        CGTValue outcome = CGTValue.getOutcome(new Number(0), null);
        Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(1)));
    }

    @Test public void getOutcome_posNumber_null() {
        CGTValue outcome = CGTValue.getOutcome(new Number(1), null);
        Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(2)));
    }

    @Test public void getOutcome_negNumber_null() {
        CGTValue outcome = CGTValue.getOutcome(new Number(-1), null);
        Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(0)));
    }

    @Test(expected = IllegalStateException.class) public void getOutcome_Nimber_null() {
        CGTValue.getOutcome(new Nimber(1), null);
    }

    @Test(expected = IllegalStateException.class) public void getOutcome_Switch_null() {
        CGTValue.getOutcome(new Switch(new Number(1), new Number(0)), null);
    }

    @Test(expected = IllegalStateException.class) public void getOutcome_Infinitesimal_null() {
        CGTValue.getOutcome(new Infinitesimal(1), null);
    }

    @Test public void getOutcome_number_negNumber_posNumber() {
        CGTValue outcome = CGTValue.getOutcome(new Number(-10), new Number(1));
        Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(0)));
    }

    @Test public void getOutcome_number_negNumber_negNumber() {
        CGTValue outcome = CGTValue.getOutcome(new Number(-20), new Number(-1));
        Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(-2)));
    }

    @Test public void getOutcome_number_posNumber_posNumber() {
        CGTValue outcome = CGTValue.getOutcome(new Number(2), new Number(20));
        Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(3)));
    }

    @Test public void getOutcome_number_zeroNumber_posNumber() {
        CGTValue outcome = CGTValue.getOutcome(new Number(0), new Number(20));
        Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(1)));
    }

    @Test public void getOutcome_number_negNumber_zeroNumber() {
        CGTValue outcome = CGTValue.getOutcome(new Number(-3), new Number(0));
        Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(-1)));
    }

    @Test public void getOutcome_number_zeroNumber_zeroNumber() {
        CGTValue outcome = CGTValue.getOutcome(new Number(0), new Number(0));
        Assert.assertThat(outcome, CoreMatchers.equalTo(new Nimber(1)));
    }

    @Test public void getOutcome_number_equal() {
        CGTValue outcome = CGTValue.getOutcome(new Number(2), new Number(2));
        Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(2)));
    }

    @Test public void getOutcome_number_numberOneBigger() {
        CGTValue outcome = CGTValue.getOutcome(new Number(1), new Number(2));
        Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(1.5)));
    }

    @Test public void getOutcome_number_leftBigger() {
        CGTValue outcome = CGTValue.getOutcome(new Number(3), new Number(2));
        Assert.assertThat(outcome, CoreMatchers.equalTo(new Switch(new Number(3), new Number(2))));
    }

    @Test public void getOutcome_number_posNumber_Nimber() {
        CGTValue outcome = CGTValue.getOutcome(new Number(3), new Nimber(2));
        Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(3)));
    }

    @Test public void getOutcome_number_zeroNumber_Nimber() {
        CGTValue outcome = CGTValue.getOutcome(new Number(0), new Nimber(2));
        //TODO: check that
        Assert.assertThat(outcome, CoreMatchers.equalTo(new Infinitesimal(1)));
    }

    @Test public void getOutcome_number_negNumber_Nimber() {
        CGTValue outcome = CGTValue.getOutcome(new Number(-3), new Nimber(2));
        Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(0)));
    }

    //TODO: number vs switch
    //TODO: number vs infinitesimal
    //TODO: nimber vs number
    //TODO: nimber vs nimber
    //TODO: switch vs number
    //TODO: infinitesimal vs number
}
