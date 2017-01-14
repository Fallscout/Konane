package engine;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class CGTValueTest {

	/*
	 * ------------------------------------------------- 
	 * getOutcome()
	 * -------------------------------------------------
	 */

	@Test
	public void getOutcome_null_negNumber() {
		CGTValue outcome = CGTValue.getOutcome(null, new Number(-1));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(-2)));
	}

	@Test
	public void getOutcome_null_posNumber() {
		CGTValue outcome = CGTValue.getOutcome(null, new Number(2));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(1)));
	}

	@Test
	public void getOutcome_null_zeroNumber() {
		CGTValue outcome = CGTValue.getOutcome(null, new Number(0));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(-1)));
	}

	@Test(expected = IllegalStateException.class)
	public void getOutcome_null_Nimber() {
		CGTValue.getOutcome(null, new Nimber(1));
	}

	@Test(expected = IllegalStateException.class)
	public void getOutcome_null_Switch() {
		CGTValue.getOutcome(null, new Switch(new Number(1), new Number(0)));
	}

	@Test(expected = IllegalStateException.class)
	public void getOutcome_null_Infinitesimal() {
		CGTValue.getOutcome(null, new Infinitesimal(1));
	}

	@Test
	public void getOutcome_zeroNumber_null() {
		CGTValue outcome = CGTValue.getOutcome(new Number(0), null);
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(1)));
	}

	@Test
	public void getOutcome_posNumber_null() {
		CGTValue outcome = CGTValue.getOutcome(new Number(1), null);
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(2)));
	}

	@Test
	public void getOutcome_negNumber_null() {
		CGTValue outcome = CGTValue.getOutcome(new Number(-1), null);
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(0)));
	}

	@Test(expected = IllegalStateException.class)
	public void getOutcome_Nimber_null() {
		CGTValue.getOutcome(new Nimber(1), null);
	}

	@Test(expected = IllegalStateException.class)
	public void getOutcome_Switch_null() {
		CGTValue.getOutcome(new Switch(new Number(1), new Number(0)), null);
	}

	@Test(expected = IllegalStateException.class)
	public void getOutcome_Infinitesimal_null() {
		CGTValue.getOutcome(new Infinitesimal(1), null);
	}

	@Test
	public void getOutcome_number_negNumber_posNumber() {
		CGTValue outcome = CGTValue.getOutcome(new Number(-10), new Number(1));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(0)));
	}

	@Test
	public void getOutcome_number_negNumber_negNumber() {
		CGTValue outcome = CGTValue.getOutcome(new Number(-20), new Number(-1));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(-2)));
	}

	@Test
	public void getOutcome_number_posNumber_posNumber() {
		CGTValue outcome = CGTValue.getOutcome(new Number(2), new Number(20));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(3)));
	}

	@Test
	public void getOutcome_number_zeroNumber_posNumber() {
		CGTValue outcome = CGTValue.getOutcome(new Number(0), new Number(20));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(20)));
	}

	@Test
	public void getOutcome_number_negNumber_zeroNumber() {
		CGTValue outcome = CGTValue.getOutcome(new Number(-3), new Number(0));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(-3)));
	}

	@Test
	public void getOutcome_number_zeroNumber_zeroNumber() {
		CGTValue outcome = CGTValue.getOutcome(new Number(0), new Number(0));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Nimber(1)));
	}

	@Test
	public void getOutcome_number_equal() {
		CGTValue outcome = CGTValue.getOutcome(new Number(2), new Number(2));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(2)));
	}

	@Test
	public void getOutcome_number_numberOneBigger() {
		CGTValue outcome = CGTValue.getOutcome(new Number(1), new Number(2));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(1.5)));
	}

	@Test
	public void getOutcome_number_leftBigger() {
		CGTValue outcome = CGTValue.getOutcome(new Number(3), new Number(2));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Switch(new Number(3), new Number(2))));
	}

	@Test
	public void getOutcome_number_posNumber_Nimber() {
		CGTValue outcome = CGTValue.getOutcome(new Number(3), new Nimber(2));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(3)));
	}

	@Test
	public void getOutcome_number_zeroNumber_Nimber() {
		CGTValue outcome = CGTValue.getOutcome(new Number(0), new Nimber(2));
		// TODO: check that
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Infinitesimal(1)));
	}

	@Test
	public void getOutcome_number_negNumber_Nimber() {
		CGTValue outcome = CGTValue.getOutcome(new Number(-3), new Nimber(2));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(0)));
	}

	@Test public void getOutcome_number_posNumber_posSwitch() {
		CGTValue outcome = CGTValue.getOutcome(new Number(2), new Switch(new Number(5), new Number(4)));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(2)));
	}

	@Test public void getOutcome_number_negNumber_posSwitch() {
		CGTValue outcome = CGTValue.getOutcome(new Number(-2), new Switch(new Number(5), new Number(4)));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(0)));
	}

	@Test public void getOutcome_number_zeroNumber_posSwitch() {
		CGTValue outcome = CGTValue.getOutcome(new Number(0), new Switch(new Number(5), new Number(4)));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Infinitesimal(1)));
	}

	@Test public void getOutcome_number_posNumber_negSwitch() {
		CGTValue outcome = CGTValue.getOutcome(new Number(2), new Switch(new Number(-2), new Number(-4)));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Nimber(1)));
	}

	@Test public void getOutcome_number_negNumber_negSwitch() {
		CGTValue outcome = CGTValue.getOutcome(new Number(-2), new Switch(new Number(-2), new Number(-4)));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(-2)));
	}

	@Test public void getOutcome_number_zeroNumber_negSwitch() {
		CGTValue outcome = CGTValue.getOutcome(new Number(0), new Switch(new Number(-2), new Number(-4)));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Nimber(1)));
	}

	@Test public void getOutcome_number_posNumber_mixedSwitch() {
		CGTValue outcome = CGTValue.getOutcome(new Number(2), new Switch(new Number(2), new Number(-2)));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(2)));
	}

	@Test public void getOutcome_number_negNumber_mixedSwitch() {
		CGTValue outcome = CGTValue.getOutcome(new Number(-2), new Switch(new Number(2), new Number(-2)));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(0)));
	}

	@Test public void getOutcome_number_zeroNumber_mixedSwitch() {
		CGTValue outcome = CGTValue.getOutcome(new Number(0), new Switch(new Number(2), new Number(-2)));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Infinitesimal(1)));
	}

	@Test public void getOutcome_number_posNumber_posInf() {
		CGTValue outcome = CGTValue.getOutcome(new Number(2), new Infinitesimal(1));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(2)));
	}

	@Test public void getOutcome_number_negNumber_posInf() {
		CGTValue outcome = CGTValue.getOutcome(new Number(-2), new Infinitesimal(1));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(0)));
	}

	@Test public void getOutcome_number_zeroNumber_posInf() {
		CGTValue outcome = CGTValue.getOutcome(new Number(0), new Infinitesimal(1));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Infinitesimal(1)));
	}

	@Test public void getOutcome_number_posNumber_negInf() {
		CGTValue outcome = CGTValue.getOutcome(new Number(2), new Infinitesimal(-1));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Nimber(1)));
	}

	@Test public void getOutcome_number_negNumber_negInf() {
		CGTValue outcome = CGTValue.getOutcome(new Number(-2), new Infinitesimal(-1));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(-2)));
	}

	@Test public void getOutcome_number_zeroNumber_negInf() {
		CGTValue outcome = CGTValue.getOutcome(new Number(0), new Infinitesimal(-1));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Nimber(1)));
	}

	@Test public void getOutcome_nimber_posNumber() {
		CGTValue outcome = CGTValue.getOutcome(new Nimber(1), new Number(2));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Nimber(1)));
	}

	@Test public void getOutcome_nimber_zeroNumber() {
		CGTValue outcome = CGTValue.getOutcome(new Nimber(1), new Number(0));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Infinitesimal(-1)));
	}

	@Test public void getOutcome_nimber_negNumber() {
		CGTValue outcome = CGTValue.getOutcome(new Nimber(1), new Number(-2));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(-2)));
	}

	@Test public void getOutcome_nimber_nimber() {
		CGTValue outcome = CGTValue.getOutcome(new Nimber(12), new Nimber(5));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Nimber(9)));
	}

	@Test public void getOutcome_posSwitch_posNumber() {
		CGTValue outcome = CGTValue.getOutcome(new Switch(new Number(4), new Number(2)), new Number(2));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(2)));
	}

	@Test public void getOutcome_posSwitch_zeroNumber() {
		CGTValue outcome = CGTValue.getOutcome(new Switch(new Number(4), new Number(2)), new Number(0));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Nimber(1)));
	}

	@Test public void getOutcome_posSwitch_negNumber() {
		CGTValue outcome = CGTValue.getOutcome(new Switch(new Number(4), new Number(2)), new Number(-2));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Nimber(1)));
	}

	@Test public void getOutcome_negSwitch_posNumber() {
		CGTValue outcome = CGTValue.getOutcome(new Switch(new Number(-2), new Number(-4)), new Number(2));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(0)));
	}

	@Test public void getOutcome_negSwitch_zeroNumber() {
		CGTValue outcome = CGTValue.getOutcome(new Switch(new Number(-2), new Number(-4)), new Number(0));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Infinitesimal(-1)));
	}

	@Test public void getOutcome_negSwitch_negNumber() {
		CGTValue outcome = CGTValue.getOutcome(new Switch(new Number(-2), new Number(-4)), new Number(-2));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(-2)));
	}

	@Test public void getOutcome_mixedSwitch_posNumber() {
		CGTValue outcome = CGTValue.getOutcome(new Switch(new Number(2), new Number(-4)), new Number(2));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(0)));
	}

	@Test public void getOutcome_mixedSwitch_zeroNumber() {
		CGTValue outcome = CGTValue.getOutcome(new Switch(new Number(2), new Number(-4)), new Number(0));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Infinitesimal(-1)));
	}

	@Test public void getOutcome_mixedSwitch_negNumber() {
		CGTValue outcome = CGTValue.getOutcome(new Switch(new Number(2), new Number(-4)), new Number(-2));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(-2)));
	}

	@Test public void getOutcome_posInf_posNumber() {
		CGTValue outcome = CGTValue.getOutcome(new Infinitesimal(1), new Number(2));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(2)));
	}

	@Test public void getOutcome_posInf_zeroNumber() {
		CGTValue outcome = CGTValue.getOutcome(new Infinitesimal(1), new Number(0));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Nimber(1)));
	}

	@Test public void getOutcome_posInf_negNumber() {
		CGTValue outcome = CGTValue.getOutcome(new Infinitesimal(1), new Number(-2));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Nimber(1)));
	}

	@Test public void getOutcome_negInf_posNumber() {
		CGTValue outcome = CGTValue.getOutcome(new Infinitesimal(-1), new Number(2));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(0)));
	}

	@Test public void getOutcome_negInf_zeroNumber() {
		CGTValue outcome = CGTValue.getOutcome(new Infinitesimal(-1), new Number(0));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Infinitesimal(-1)));
	}

	@Test public void getOutcome_negInf_negNumber() {
		CGTValue outcome = CGTValue.getOutcome(new Infinitesimal(-1), new Number(-2));
		Assert.assertThat(outcome, CoreMatchers.equalTo(new Number(-2)));
	}

	/*
	 * ------------------------------------------------- max()
	 * -------------------------------------------------
	 */
	@Test
	public void max_negNumber_Number() {
		CGTValue max = CGTValue.max(new Number(-3), new Number(2), true);
		Assert.assertThat(max, CoreMatchers.equalTo(new Number(2)));

		max = CGTValue.max(new Number(-3), new Number(2), false);
		Assert.assertThat(max, CoreMatchers.equalTo(new Number(-3)));
	}

	@Test
	public void max_Number_negNumber() {
		CGTValue max = CGTValue.max(new Number(2), new Number(-3),  true);
		Assert.assertThat(max, CoreMatchers.equalTo(new Number(2)));

		max = CGTValue.max(new Number(2), new Number(-3), false);
		Assert.assertThat(max, CoreMatchers.equalTo(new Number(-3)));
	}
	
	@Test
	public void max_negNumber_negNumber() {
		CGTValue max = CGTValue.max(new Number(-2), new Number(-3),  true);
		Assert.assertThat(max, CoreMatchers.equalTo(new Number(-2)));

		max = CGTValue.max(new Number(-2), new Number(-3), false);
		Assert.assertThat(max, CoreMatchers.equalTo(new Number(-3)));
	}
	
	@Test
	public void max_Nimber_Number() {
		//Black
		CGTValue max = CGTValue.max(new Nimber(1), new Number(-3),  true);
		Assert.assertThat(max, CoreMatchers.equalTo(new Nimber(1)));
		
		max = CGTValue.max(new Nimber(1), new Number(3), true);
		Assert.assertThat(max, CoreMatchers.equalTo(new Number(3)));
		//White
		max = CGTValue.max(new Nimber(1), new Number(-3), false);
		Assert.assertThat(max, CoreMatchers.equalTo(new Number(-3)));
		
		max = CGTValue.max(new Nimber(1), new Number(3), false);
		Assert.assertThat(max, CoreMatchers.equalTo(new Nimber(1)));
	}
	
	@Test
	public void max_Number_Nimber() {
		//Black
		CGTValue max = CGTValue.max(new Number(-3), new Nimber(1),  true);
		Assert.assertThat(max, CoreMatchers.equalTo(new Nimber(1)));
		
		max = CGTValue.max(new Number(3), new Nimber(1),  true);
		Assert.assertThat(max, CoreMatchers.equalTo(new Number(3)));

		//White
		max = CGTValue.max(new Number(-3), new Nimber(1), false);
		Assert.assertThat(max, CoreMatchers.equalTo(new Number(-3)));
		
		max = CGTValue.max(new Number(3), new Nimber(1), false);
		Assert.assertThat(max, CoreMatchers.equalTo(new Nimber(1)));
	}

	@Test
	public void max_negNumber_Switch() {
		// Always prefer number? For both black and white??
		Number first = new Number(2);
		Number second = new Number(-3);
		
		CGTValue max = CGTValue.max(new Number(-3), new Switch(first, second), true);
		Assert.assertThat(max, CoreMatchers.equalTo(new Number(-3)));

		max = CGTValue.max(new Number(-3), new Switch(first, second), false);
		Assert.assertThat(max, CoreMatchers.equalTo(new Number(-3)));
	}
	
	@Test
	public void max_zeroNumber_Switch() {
		// Always prefer number? For both black and white??
		Number first = new Number(2);
		Number second = new Number(-3);
		
		CGTValue max = CGTValue.max(new Number(0), new Switch(first, second), true);
		Assert.assertThat(max, CoreMatchers.equalTo(new Number(0)));

		max = CGTValue.max(new Number(0), new Switch(first, second), false);
		Assert.assertThat(max, CoreMatchers.equalTo(new Number(0)));
	}
	
	@Test
	public void max_Switch_negNumber() {
		// Always prefer number? For both black and white??
		Number first = new Number(2);
		Number second = new Number(-3);
		
		CGTValue max = CGTValue.max(new Switch(first, second), new Number(-3), true);
		Assert.assertThat(max, CoreMatchers.equalTo(new Number(-3)));

		max = CGTValue.max(new Switch(first, second), new Number(-3), false);
		Assert.assertThat(max, CoreMatchers.equalTo(new Number(-3)));
	}
	
	@Test
	public void max_Switch_zeroNumber() {
		// Always prefer number? For both black and white??
		Number first = new Number(2);
		Number second = new Number(-3);
		
		CGTValue max = CGTValue.max(new Switch(first, second), new Number(0), true);
		Assert.assertThat(max, CoreMatchers.equalTo(new Number(0)));

		max = CGTValue.max(new Switch(first, second), new Number(0), false);
		Assert.assertThat(max, CoreMatchers.equalTo(new Number(0)));
	}
	
	@Test
	public void max_negNumber_Infinitesimal() {
		//TODO: Always prefer number? For both black and white??
		CGTValue max = CGTValue.max(new Number(-3), new Infinitesimal(1), true);
		Assert.assertThat(max, CoreMatchers.equalTo(new Number(-3)));

		max = CGTValue.max(new Number(-3), new Infinitesimal(1), false);
		Assert.assertThat(max, CoreMatchers.equalTo(new Number(-3)));
	}
	
	@Test
	public void max_zeroNumber_Infinitesimal() {
		//TODO: Always prefer number? For both black and white??
		CGTValue max = CGTValue.max(new Number(0), new Infinitesimal(1), true);
		Assert.assertThat(max, CoreMatchers.equalTo(new Number(-3)));

		max = CGTValue.max(new Number(0), new Infinitesimal(1), false);
		Assert.assertThat(max, CoreMatchers.equalTo(new Number(-3)));
	}
	
	@Test
	public void max_Infinitesimal_negNumber() {
		//TODO:Always prefer number? For both black and white??
		CGTValue max = CGTValue.max(new Number(-3), new Infinitesimal(1), true);
		Assert.assertThat(max, CoreMatchers.equalTo(new Number(-3)));

		max = CGTValue.max(new Number(-3), new Infinitesimal(1), false);
		Assert.assertThat(max, CoreMatchers.equalTo(new Number(-3)));
	}
	
	@Test
	public void max_Infinitesimal_zeroNumber() {
		//TODO: Always prefer number? For both black and white??
		CGTValue max = CGTValue.max(new Number(0), new Infinitesimal(1), true);
		Assert.assertThat(max, CoreMatchers.equalTo(new Number(-3)));

		max = CGTValue.max(new Number(0), new Infinitesimal(1), false);
		Assert.assertThat(max, CoreMatchers.equalTo(new Number(-3)));
	}
	
	@Test
	public void max_Nimber_Infinitesimal() {
		//TODO:Always prefer nimber? For both black and white??
		CGTValue max = CGTValue.max(new Nimber(1), new Infinitesimal(1), true);
		Assert.assertThat(max, CoreMatchers.equalTo(new Nimber(1)));

		max = CGTValue.max(new Nimber(1), new Infinitesimal(1), false);
		Assert.assertThat(max, CoreMatchers.equalTo(new Nimber(1)));
	}
	
	@Test
	public void max_Infinitesimal_Nimber() {
		//TODO: Always prefer nimber? For both black and white??
		CGTValue max = CGTValue.max(new Infinitesimal(1), new Nimber(1), true);
		Assert.assertThat(max, CoreMatchers.equalTo(new Nimber(1)));

		max = CGTValue.max(new Infinitesimal(1), new Nimber(1),  false);
		Assert.assertThat(max, CoreMatchers.equalTo(new Nimber(1)));
	}
	
	@Test
	public void max_Nimber_Switch() {
		//TODO:Always prefer Switch? For both black and white??
		Number first = new Number(2);
		Number second = new Number(-3);
		
		CGTValue max = CGTValue.max(new Nimber(1), new Switch(first, second), true);
		Assert.assertThat(max, CoreMatchers.equalTo(new Nimber(1)));

		max = CGTValue.max(new Nimber(1), new Switch(first, second), false);
		Assert.assertThat(max, CoreMatchers.equalTo(new Nimber(1)));
	}
	
	@Test
	public void max_Switch_Nimber() {
		//TODO: Always prefer Switch? For both black and white??
		Number first = new Number(2);
		Number second = new Number(-3);
		
		CGTValue max = CGTValue.max(new Switch(first, second), new Nimber(1), true);
		Assert.assertThat(max, CoreMatchers.equalTo(new Nimber(1)));

		max = CGTValue.max(new Switch(first, second), new Nimber(1),  false);
		Assert.assertThat(max, CoreMatchers.equalTo(new Nimber(1)));
	}
	
	/*
	 * ------------------------------------------------- Equal methods
	 * -------------------------------------------------
	 */
	
	@Test
	public void equal_Number_Number() {
		boolean equal = new Number(5).equals(new Number(5));
		Assert.assertThat(equal, CoreMatchers.equalTo(true));

		equal = new Number(5).equals(new Number(4));
		Assert.assertThat(equal, CoreMatchers.equalTo(false));
	}
	
	@Test
	public void equal_Number_Nimber() {
		boolean equal = new Number(5).equals(new Nimber(1));
		Assert.assertThat(equal, CoreMatchers.equalTo(false));

		equal = new Nimber(1).equals(new Number(5));
		Assert.assertThat(equal, CoreMatchers.equalTo(false));
	}
	
	@Test
	public void equal_Number_Infinitesimal() {
		boolean equal = new Number(5).equals(new Infinitesimal(1));
		Assert.assertThat(equal, CoreMatchers.equalTo(false));

		equal = new Infinitesimal(1).equals(new Number(5));
		Assert.assertThat(equal, CoreMatchers.equalTo(false));
	}
	
	@Test
	public void equal_Nimber_Nimber() {
		boolean equal = new Nimber(1).equals(new Nimber(1));
		Assert.assertThat(equal, CoreMatchers.equalTo(true));

		equal = new Nimber(1).equals(new Nimber(2));
		Assert.assertThat(equal, CoreMatchers.equalTo(false));
	}
	
	@Test
	public void equal_Nimber_Infinitesimal() {
		boolean equal = new Nimber(1).equals(new Infinitesimal(1));
		Assert.assertThat(equal, CoreMatchers.equalTo(false));

		equal = new Infinitesimal(1).equals(new Nimber(2));
		Assert.assertThat(equal, CoreMatchers.equalTo(false));
	}
	
	@Test
	public void equal_Infinitesimal_Infinitesimal() {
		boolean equal = new Infinitesimal(1).equals(new Infinitesimal(1));
		Assert.assertThat(equal, CoreMatchers.equalTo(true));

		equal = new Infinitesimal(1).equals(new Infinitesimal(2));
		Assert.assertThat(equal, CoreMatchers.equalTo(false));
	}
	
	@Test
	public void equal_Number_Switch() {
		Number first = new Number(2);
		Number second = new Number(-3);
		boolean equal = new Number(1).equals(new Switch(first, second));
		Assert.assertThat(equal, CoreMatchers.equalTo(false));

		equal = new Switch(first, second).equals(new Number(1));
		Assert.assertThat(equal, CoreMatchers.equalTo(false));
	}
	
	@Test
	public void equal_Switch_Switch() {
		Number first = new Number(2);
		Number second = new Number(-3);
		boolean equal = new Switch(first, second).equals(new Switch(first, second));
		Assert.assertThat(equal, CoreMatchers.equalTo(true));

		first = new Number(2);
		second = new Number(-6);
		equal = new Switch(first, second).equals(new Switch(first, second));
		Assert.assertThat(equal, CoreMatchers.equalTo(false));
	}
	
	@Test
	public void equal_Nimber_Switch() {
		Number first = new Number(2);
		Number second = new Number(-3);
		boolean equal = new Nimber(1).equals(new Switch(first, second));
		Assert.assertThat(equal, CoreMatchers.equalTo(false));

		equal = new Switch(first, second).equals(new Nimber(1));
		Assert.assertThat(equal, CoreMatchers.equalTo(false));
	}
	
	@Test
	public void equal_Infinitesimal_Switch() {
		Number first = new Number(2);
		Number second = new Number(-3);
		boolean equal = new Infinitesimal(1).equals(new Switch(first, second));
		Assert.assertThat(equal, CoreMatchers.equalTo(false));

		equal = new Switch(first, second).equals(new Infinitesimal(1));
		Assert.assertThat(equal, CoreMatchers.equalTo(false));
	}
}
