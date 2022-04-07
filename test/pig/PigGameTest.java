/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design. The course was
 * taken at Worcester Polytechnic Institute. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Copyright ©2020 Gary F. Pollice
 *******************************************************************************/

package pig;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static pig.PigGameVersion.*;

/**
 * TestCases for the PigGame
 * 
 * @version Oct 21, 2020
 */
class PigGameTest {
	private Die[] dice;

	/**
	 * This helper method is to generate an array for certain type of Die for
	 * testing purpose
	 * 
	 * @param numberOfDice Number of dice in the testing array
	 * @param DieType      The type of Die needed for the test D6: normal dice
	 *                     design for the game that will randomly generate an int
	 *                     from 1 to 6 D1: a die that will only roll to 1
	 *                     DieOfNumberTwo: a die that will only roll to 2
	 */
	private void DiceInitialzation(int numberOfDice, int DieType) {
		// Initialize different types of Die
		final Die testingDie;
		final Die D6 = () -> (int) Math.ceil(Math.random() * 6);
		final Die D1 = () -> 1;
		final Die DieOfNumberTwo = () -> 2;

		// Pick the type for testing
		dice = new Die[numberOfDice];
		if (DieType == 1) {
			testingDie = D1;
		} else if (DieType == 2) {
			testingDie = DieOfNumberTwo;
		} else if (DieType == 6) {
			testingDie = D6;
		} else {
			testingDie = null;
		}

		for (int i = 0; i < dice.length; i++) {
			dice[i] = testingDie;
		}
	}

	// =========================================TEST
	// AREA====================================
	// Test for validation of constructors:

	/*
	 * Test when the input has no dice(null)
	 */
	@Test
	void noDice() {
		// See
		// https://junit.org/junit5/docs/current/user-guide/#writing-tests-assertions
		assertThrows(PigGameException.class, () -> new PigGame(STANDARD, 10, null));
	}

	/*
	 * Test when the input has zero dice(an array of length zero)
	 */
	@Test
	void zeroDice() {
		Die[] dice = new Die[0];
		assertThrows(PigGameException.class, () -> new PigGame(STANDARD, 10, dice));
	}

	/*
	 * Test when the input has one die for STANDARD version
	 */
	@Test
	void oneDieForSTANDARDVersion() {
		DiceInitialzation(1, 6);
		assertTrue(PigGame.class.isInstance(new PigGame(STANDARD, 10, dice)));
	}

	/*
	 * Test when the input has two dice for STANDARD version
	 */
	@Test
	void twoDiceForSTANDARDVersion() {
		DiceInitialzation(2, 6);
		assertTrue(PigGame.class.isInstance(new PigGame(STANDARD, 10, dice)));
	}

	/*
	 * Test when the input has one die for ONE_DIE_DUPLICATE version
	 */
	@Test
	void oneDieForONE_DIE_DUPLICATEVersion() {
		DiceInitialzation(1, 6);
		assertTrue(PigGame.class.isInstance(new PigGame(ONE_DIE_DUPLICATE, 10, dice)));
	}

	/*
	 * Test when the input has two dice for ONE_DIE_DUPLICATE version
	 */
	@Test
	void twoDiceForONE_DIE_DUPLICATEVersion() {
		DiceInitialzation(2, 6);
		assertTrue(PigGame.class.isInstance(new PigGame(ONE_DIE_DUPLICATE, 10, dice)));
	}

	/*
	 * Test when the input has one die for TWO_DICE version
	 */
	@Test
	void oneDieForTWO_DICEVersion() {
		DiceInitialzation(1, 6);
		assertThrows(PigGameException.class, () -> new PigGame(TWO_DICE, 10, dice));
	}

	/*
	 * Test when the input has two dice for TWO_DICE version
	 */
	@Test
	void twoDiceForTWO_DICEVersion() {
		DiceInitialzation(2, 6);
		assertTrue(PigGame.class.isInstance(new PigGame(ONE_DIE_DUPLICATE, 10, dice)));
	}

	/*
	 * Test when the input has three dice for THREE_DICE version
	 */
	@Test
	void threeDiceForTWO_DICEVersion() {
		DiceInitialzation(3, 6);
		assertTrue(PigGame.class.isInstance(new PigGame(ONE_DIE_DUPLICATE, 10, dice)));
	}

	/*
	 * Test when the input has the goal score less than 1
	 */
	@Test
	void scoreNeededLessThanOne() {
		DiceInitialzation(1, 6);
		assertThrows(PigGameException.class, () -> new PigGame(STANDARD, 0, dice));
	}

	// ==========Test for roll() function:

	/*
	 * Test roll() for ONE_DIE_DUPLICATE version
	 */
	@Test
	void rollTestInONE_DIE_DUPLICATEVersion() {
		DiceInitialzation(1, 1);
		PigGame game = new PigGame(ONE_DIE_DUPLICATE, 10, dice);
		assertEquals(game.roll(), 1);
	}

	/*
	 * Test roll() for STANDARD version
	 */
	@Test
	void rollTestInSTANDARDVersion() {
		DiceInitialzation(1, 2);
		PigGame game = new PigGame(STANDARD, 10, dice);
		assertEquals(game.roll(), 2);
	}

	/*
	 * Test roll() for TWO_DICE version
	 */
	@Test
	void rollTestInTWO_DICEVersion() {
		DiceInitialzation(2, 1);
		PigGame game = new PigGame(TWO_DICE, 10, dice);
		assertEquals(game.roll(), 2); // if a two D1s get roll, we should get 2
	}

	/*
	 * Test if roll() that produces 1 skips the turn in STANDARD version this also
	 * tests if skipping turn is prior to win in this version
	 */
	@Test
	void rollTestWhenSkippingTurnInSTANDARDVersion() {
		DiceInitialzation(1, 1);
		PigGame game = new PigGame(STANDARD, 1, dice);
		assertEquals(game.roll(), 0);
	}

	/*
	 * Test if roll() that produces repeat numbers skips the turn in STANDARD
	 * version this also tests if skipping turn is prior to win in this version
	 */
	@Test
	void rollTestWhenSkippingTurnInONE_DIE_DUPLICATEVersion() {
		DiceInitialzation(1, 1);
		PigGame game = new PigGame(ONE_DIE_DUPLICATE, 2, dice);
		game.roll();
		assertEquals(game.roll(), 0);
	}

	/*
	 * Test if roll() that produces 7 skips the turn in TWO_DICE version this also
	 * tests if skipping turn is prior to win in this version
	 */
	@Test
	void rollTestWhenSkippingTurnInTWO_DICEVersion() {
		dice = new Die[2]; // specialize dice to hit number 7
		dice[0] = () -> 3;
		dice[1] = () -> 4;
		PigGame game = new PigGame(TWO_DICE, 1, dice);
		assertEquals(game.roll(), 0);
	}

	/*
	 * Test if roll() return the goal score if wining condition is met this also
	 * tests if the roll() stack up the number rolled in the turn
	 */
	@Test
	void rollTestWhenWining() {
		DiceInitialzation(1, 2);
		PigGame game = new PigGame(STANDARD, 10, dice);

		for (int i = 0; i < 4; i++) {
			game.roll();// the current turn score sums up to 8 here
		}

		assertEquals(game.roll(), 10);
	}

	/*
	 * Test if roll() can still be called after a game ends
	 */
	@Test
	void rollAfterGameEnds() {
		DiceInitialzation(1, 6);
		PigGame game = new PigGame(ONE_DIE_DUPLICATE, 1, dice);
		game.roll();
		assertThrows(PigGameException.class, () -> game.roll());
	}

	// ==========Test for hold() function:

	/*
	 * Test if hold() can be called before roll() in a turn
	 */
	@Test
	void holdBeforeRollInTurn() {
		DiceInitialzation(1, 6);
		PigGame game = new PigGame(ONE_DIE_DUPLICATE, 1, dice);
		assertThrows(PigGameException.class, () -> game.hold());
	}

	/*
	 * Test if hold() can be called after the game ends
	 */
	@Test
	void holdAfterGameEnds() {
		DiceInitialzation(1, 6);
		PigGame game = new PigGame(ONE_DIE_DUPLICATE, 1, dice);
		game.roll();
		assertThrows(PigGameException.class, () -> game.roll());
	}

	/*
	 * Test if hold() can switch to the next player
	 */
	@Test
	void holdToSwitchPlayerTest() {
		DiceInitialzation(2, 1);
		PigGame game = new PigGame(TWO_DICE, 4, dice);
		game.roll();
		game.hold(); // when first player roll() and hold(), it should have score of 2
		game.roll(); // then switch to the second player with score of 0
		assertEquals(game.roll(), 4);
	}

	/*
	 * Test if hold() can save the score after switching back and forth
	 */
	@Test
	void holdToSaveScoreTest() {
		DiceInitialzation(1, 1);
		PigGame game = new PigGame(ONE_DIE_DUPLICATE, 2, dice);

		// with each player roll() and hold(), playerOne should have score of 2
		for (int i = 0; i < 2; i++) {
			game.roll();
			game.hold();
		}

		assertEquals(game.roll(), 2);
	}

}
