import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class EasyLevel extends GameLevel {
	int i=0;
	protected EasyLevel(TurnsTakenCounterLabel validTurnTime, ScoreCalculationLabel scoreLabel, JFrame mainFrame) {
		super(validTurnTime, scoreLabel, 2, mainFrame);
		this.turnsTakenCounter.setDifficultyModeLabel("Easy Level");
		GameLevel.currentScore.setDifficultyModeLabel2 ("Easy Level");
		this.cardsPerRow = 4;
		this.rowsPerGrid = 4;
		this.cardsToTurnUp = 2;
		this.totalUniqueCards = rowsPerGrid * cardsPerRow;
	}

	@Override
	protected void makeDeck() {
		// Creates a deck to fill the grid.  Each card appears twice in random places.
		ImageIcon cardIcon[] = this.loadCardIcons();
		ImageIcon backIcon = cardIcon[TotalCardsPerDeck];

		// make an array of card numbers: 0, 0, 1, 1, 2, 2, ..., 7, 7
		// duplicate the image in as many cards as the input imageClones
		int totalCardsInGrid = getRowsPerGrid() * getCardsPerRow();
		int totalUniqueCards = totalCardsInGrid/2;

		// Generate one distinct random card number for each unique card	
		int cardsToAdd[] = new int[totalCardsInGrid];
		boolean cardChosen[] = new boolean[TotalCardsPerDeck];

		int chosenCount = 0;
		Random rand = new Random();
		while (chosenCount < totalUniqueCards)
		{
			int nextCardNo = rand.nextInt(TotalCardsPerDeck);
			if (!cardChosen[nextCardNo]) {
				cardChosen[nextCardNo] = true;
				cardsToAdd[2*chosenCount] = nextCardNo;
				cardsToAdd[2*chosenCount + 1] = nextCardNo;
				chosenCount++;
			}
		}

		// randomize the order of the cards
		this.randomizeIntArray(cardsToAdd);

		// make each card object and add it to the game grid
		for(int i = 0; i < cardsToAdd.length; i++)
		{
			// number of the card, randomized
			int num = cardsToAdd[i];
			// make the card object and add it to the panel
			String rank = cardNames[num].substring(0, 1);
			String suit = cardNames[num].substring(1, 2);
			this.grid.add( new Card(this, cardIcon[num], backIcon, num, rank, suit));
		}
	}

	@Override
	protected boolean addToTurnedCardsBuffer(Card card) {
		// add the card to the list
		this.turnedCardsBuffer.add(card);
		// there are two cards
		if(this.turnedCardsBuffer.size() == getCardsToTurnUp()-1) 
		{
			// Were are turning up the last card
			// record the player's turn
			//Adds to the score
			this.turnsTakenCounter.increment();
			GameLevel.currentScore.scoreIncrement();
			this.turnedCardsBuffer.clear();

			//Determines when the game has ended
			i=i+1;
			if(i==16){
				JOptionPane.showMessageDialog(mainFrame, "Congratulation!!!\r\n"+
						"You've just beaten a game\r\n"+
						"that was mediocre at best.\r\n"+
						"\r\n"+
						"If you want to play again, press\r\n"+
						"the OK button and then click the\r\n"+
						"New Game tab on the top menu.");
			}

			// In easy mode nothing to be done here
		}
		return true;
	}
	// In easy mode nothing to be done here



	@Override
	protected boolean turnUp(Card card) {
		// the card may be turned
		if(this.turnedCardsBuffer.size() < 1) 
		{
			return this.addToTurnedCardsBuffer(card);
		}
		// there are already the number of EasyMode (two face up cards) in the turnedCardsBuffer
		return false;
	}

	@Override
	protected String getMode() {
		// TODO Auto-generated method stub
		return "EasyMode";
	}
}









