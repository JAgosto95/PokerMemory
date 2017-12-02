import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class FlushLevel extends EqualPairLevel {

	// FLUSH LEVEL: The goal is to find, on each turn, five cards with the same
	// suit

	protected FlushLevel(TurnsTakenCounterLabel validTurnTime, ScoreCalculationLabel scoreLabel, JFrame mainFrame) {
		super(validTurnTime, scoreLabel, mainFrame);
		super.turnsTakenCounter.setDifficultyModeLabel("Flush Level");
		cardsToTurnUp = 5;
		cardsPerRow = 10;
		rowsPerGrid = 5;
		
		//changes music for the level
		GameManager.audioClip.close();
		GameManager.audioFile = new File("audio/musicFlush.wav");
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

	@Override
	protected boolean addToTurnedCardsBuffer(Card card) {
		// add the card to the list
		this.turnedCardsBuffer.add(card);
		if(this.turnedCardsBuffer.size() == getCardsToTurnUp())
		{
			// We are uncovering the last card in this turn
			// Record the player's turn
			this.turnsTakenCounter.increment();
			// get the other card (which was already turned up)
			Card otherCard1 = (Card) this.turnedCardsBuffer.get(0);
			Card otherCard2 = (Card) this.turnedCardsBuffer.get(1);
			Card otherCard3 = (Card) this.turnedCardsBuffer.get(2);
			Card otherCard4 = (Card) this.turnedCardsBuffer.get(3);
			
			//Determines if hand is a flush
			if(isFlushWin(card, otherCard1, otherCard2, otherCard3, otherCard4 )) {
				// Five cards match, so remove them from the list (they will remain face up)
				this.turnedCardsBuffer.clear();
				//Add score
				GameLevel.currentScore.scoreIncrement3();
				
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

	//Method that determines whether the hand is a winning hand or not
	public static boolean isFlushWin(Card card, Card otherCard1, Card otherCard2, Card otherCard3, Card otherCard4){
		return card.getSuit().equals(otherCard1.getSuit()) && (card.getSuit().equals(otherCard2.getSuit())) && (card.getSuit().equals(otherCard3.getSuit())) && (card.getSuit().equals(otherCard4.getSuit()));
	}

}