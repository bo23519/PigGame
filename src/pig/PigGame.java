/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2020 Gary F. Pollice
 *******************************************************************************/

package pig;

/**
 * This class is the main class for the Pig Game programming assignment.
 * 
 * @version Oct 21, 2020
 */
public class PigGame {
	private int currentTurnScore; // the score for the current player in the current turn
	private int playerOneScore; // the sum score of each player has made
	private int playerTwoScore;
	private int criticalNumber; // number of the Dice that skips the turn
	private boolean playerOneTurn; // to indicate if it is playerOne's turn
	private boolean activeGame; // to indicate if the game is still active
	private Die[] dice;

	final private int goalScore;
	final private int numOfDice;
	final private PigGameVersion version;

	/**
	 * Constructor that takes in the version and dice to be used for the game
	 * version.
	 * 
	 * @param version     the game version
	 * @param scoreNeeded the score that a player must achieve (equal to this or
	 *                    greater)
	 * @param dice        an array of dice to be used for the game.
	 * @throws a PigGameException if any of the arguments are invalid (e.g.
	 *           scoreNeeded is <= 0)
	 */
	public PigGame(PigGameVersion version, int scoreNeeded, Die... dice) throws PigGameException {

		// validation checking for inputs
		if (dice == null || dice.length == 0 || (dice.length < 2 && version == PigGameVersion.TWO_DICE)) {
			throw new PigGameException("number of dice is less than the game version requires");
		}
		if (scoreNeeded < 1) {
			throw new PigGameException("the score needed can not be less than 1");
		}

		// version setting initialize here
		this.version = version;
		if (version == PigGameVersion.TWO_DICE) {
			criticalNumber = 7; // for the TWO_DICE versions, the critical number that skips turn is 7
			numOfDice = 2; // and the number of dice is 2
		} else {
			criticalNumber = 1;
			numOfDice = 1; // for other versions, the number of dice is 1
			/*
			 * For STANDARD version, the criticalNumber is set to be 1 For ONE_DIE_DUPLICATE
			 * version, the criticalNumber will change after first roll in each turn
			 */
		}

		// other variable initialization
		playerOneScore = 0;
		playerTwoScore = 0;
		currentTurnScore = 0;
		playerOneTurn = true;
		activeGame = true;
		this.dice = dice;
		goalScore = scoreNeeded;
	}

	/**
	 * This method rolls the dice for the play and returns the result.
	 * 
	 * @return the amount rolled. If this returns 0 it means that the player who
	 *         rolled the dice has pigged out and receives a 0 for the turn and the
	 *         opposing player will make the next roll. If the method returns the
	 *         scoreNeeded, it means the player who rolled wins.
	 * @throws a PigGameException if the game is over when this method is called.
	 */
	protected int roll() throws PigGameException {
		checkEndGame(); // check for if the game has ended

		// rolling dice, assuming we only need the sum of all the dice numbers instead
		// of individual ones
		int rollingResult = 0;
		for (int rolls = 0; rolls < numOfDice; rolls++) {
			rollingResult += dice[rolls].roll();
		}

		if (currentTurnScore == 0 && version == PigGameVersion.ONE_DIE_DUPLICATE) {
			criticalNumber = rollingResult; // update critical number in ONE_DIE_DUPLICATE version
		} else if (rollingResult == criticalNumber) { // check critical
			switchTurn();
			return 0; // the turn is skipped
		}

		if ((rollingResult + currentTurnScore + playerOneScore >= goalScore && playerOneTurn)
				|| ((rollingResult + currentTurnScore + playerTwoScore >= goalScore && !playerOneTurn))) { // checking																					// condition
			activeGame = false; // end game		
			return goalScore; // one player has won
		}

		currentTurnScore += rollingResult; // otherwise, just add and return the number that player rolls
		return rollingResult;
	}

	/**
	 * This method adds the turn total to the current total for the active player
	 * and switches players. A player must have rolled at least once during the turn
	 * before holding.
	 * 
	 * @throws PigGameException if the active player has not rolled at least one
	 *                          time during the turn.
	 */
	protected void hold() throws PigGameException {
		checkEndGame(); // check for if the game has ended

		if (currentTurnScore == 0) {
			throw new PigGameException("The player has not rolled at least once");
		}

		else if (playerOneTurn) {
			playerOneScore += currentTurnScore;
		} else {
			playerTwoScore += currentTurnScore;
		}

		switchTurn();
	}

	/**
	 * This method switch turn for players and reset the currentTurnScore
	 */
	private void switchTurn() {
		playerOneTurn = !playerOneTurn;
		currentTurnScore = 0;
	}

	/**
	 * This method is used to check if the game is still active
	 * 
	 * @throws a PigGameException if the game is over when this method is called.
	 */
	private void checkEndGame() throws PigGameException {
		if (!activeGame) {
			throw new PigGameException("the game is over");
		}
	}
}
