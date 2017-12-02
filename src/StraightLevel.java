import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class StraightLevel extends EqualPairLevel {

	// STRAIGHT LEVEL: The goal is to find, on each turn, five cards in
	// ascending order that have at least two different suits

	protected StraightLevel(TurnsTakenCounterLabel validTurnTime, ScoreCalculationLabel scoreLabel, JFrame mainFrame) {
		super(validTurnTime, scoreLabel, mainFrame);
		super.turnsTakenCounter.setDifficultyModeLabel("Straight Level");
		cardsToTurnUp = 5;
		cardsPerRow = 10;
		rowsPerGrid = 5;
		
		//changes music for the level
		GameManager.audioClip.close();
		GameManager.audioFile = new File("audio/musicStraight.wav");
		try {
			GameManager.audioStream = AudioSystem.getAudioInputStream(GameManager.audioFile);
			GameManager.audioClip.open(GameManager.audioStream);
			GameManager.audioClip.start();
			GameManager.audioClip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void makeDeck() {
		// In Flush level the grid consists of distinct cards, no repetitions
		ImageIcon cardIcon[] = this.loadCardIcons();

		// back card
		ImageIcon backIcon = cardIcon[TotalCardsPerDeck];

		int cardsToAdd[] = new int[getRowsPerGrid() * getCardsPerRow()];
		for (int i = 0; i < (getRowsPerGrid() * getCardsPerRow()); i++) {
			cardsToAdd[i] = i;
		}

		// randomize the order of the deck
		this.randomizeIntArray(cardsToAdd);

		// make each card object
		for (int i = 0; i < cardsToAdd.length; i++) {
			// number of the card, randomized
			int num = cardsToAdd[i];
			// make the card object and add it to the panel
			String rank = cardNames[num].substring(0, 1);
			String suit = cardNames[num].substring(1, 2);
			this.grid.add(new Card(this, cardIcon[num], backIcon, num, rank, suit));
		}


	}
	public boolean isAWin = false;

	@Override
	protected boolean addToTurnedCardsBuffer(Card card) {
		// add the card to the list
		this.turnedCardsBuffer.add(card);
		if (this.turnedCardsBuffer.size() == getCardsToTurnUp()) {
			// We are uncovering the last card in this turn
			// Record the player's turn
			this.turnsTakenCounter.increment();
			// get the other card (which was already turned up)
			Vector<Card> t = this.turnedCardsBuffer;
			//Determines if the hand is a winning hand
			isAWin = straightWinningHand(transformToInts(t));

			if(isAWin) {
				// Five cards match, so remove them from the list (they will remain face up)
				this.turnedCardsBuffer.clear();
				//Increases score
				GameLevel.currentScore.scoreIncrement4();
				//Determines when the game ends
				i=i+1;
				if(i==8){
					JOptionPane.showMessageDialog(mainFrame, "Congratulation!!!\r\n"+
							"You've just beaten a game\r\n"+
							"that was mediocre at best.\r\n"+
							"\r\n"+
							"If you want to play again, press\r\n"+
							"the OK button and then click the\r\n"+
							"New Game tab on the top menu.");
				}
			}
			else 
			{
				// The cards do not match, so start the timer to turn them down
				GameLevel.turnDownTimer.start();
				//Decreases score
				GameLevel.currentScore.scoreDecrease();
			}

		}


		return true; 
	}
	//sorts assay of ints to ascending order
	public static int[] BubbleSortAscending(int[] arr){
		int temp;
		for(int i=0; i < arr.length-1; i++){

			for(int j=1; j < arr.length-i; j++){
				if(arr[j-1] > arr[j]){
					temp=arr[j-1];
					arr[j-1] = arr[j];
					arr[j] = temp;
				}
			}

		}
		return arr;
	}

	//transforms an array of strings to an array of ints
	public static int[] transformToInts(Vector<Card> t) {
		int rank[] = { 0, 0, 0, 0, 0};

		for (int i = 0; i < t.size(); i++) {
			switch (t.get(i).getRank()) {

			case "2":
				rank[i] = 2;
				break;

			case "3":
				rank[i] = 3;
				break;

			case "4":
				rank[i] = 4;
				break;

			case "5":
				rank[i] = 5;
				break;

			case "6":
				rank[i] = 6;
				break;

			case "7":
				rank[i] = 7;
				break;

			case "8":
				rank[i] = 8;
				break;

			case "9":
				rank[i] = 9;
				break;

			case "t":
				rank[i] = 10;
				break;

			case "j":
				rank[i] = 11;
				break;

			case "q":
				rank[i] = 12;
				break;

			case "k":
				rank[i] = 13;
				break;

			case "a":
				rank[i] = 14;
				break;

			}

		}
		rank  = BubbleSortAscending( rank );

		return rank;
	}
	//Determines if the hand is capable of winning
	public static boolean straightWinningHand(int[] arr){
		int k = 0;
		for( int i = 0; i < arr.length - 1; i++){
			if(arr[i] + 1 == arr[i + 1]){
				k++;
			}
			else if( arr[3] + 9 == 14 && arr[3] != arr[2] && arr[3] != arr[4] && arr[4]== 14){
				k++;
			}
			else{
				return false;
			}
		}
		if( k == 4){
			return true;
		}
		else{
			return false;
		}
	}


}
