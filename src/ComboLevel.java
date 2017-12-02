import java.util.Vector;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * @author Juan Agosto & Kevin Santiago
 * 
 *
 */
public class ComboLevel extends EqualPairLevel {

	// COMBO LEVEL: The goal is to find, on each turn, five cards in
	// ascending order where each card has a difference of 2. There are at least
	// two different suits in the hand.
	// The 'A' card will always have a value of '1' except for any hand that has
	// a Straight, where it will have the value of either '1' or '14'.

	// initializes the booleans that determine a winning hand
	public boolean isAWin = false;
	public boolean isAWinFlush = false;
	public boolean isAWinStraight = false;
	public static boolean test = false;

	// creates the RadioButton variables for the frame
	JRadioButton tSS;
	JRadioButton sS;
	JRadioButton f;
	JRadioButton sF;
	JRadioButton pass;
	JButton ok;
	JFrame frame;
	int i = 0;

	protected ComboLevel(TurnsTakenCounterLabel validTurnTime,
			ScoreCalculationLabel scoreLabel, JFrame mainFrame) {
		super(validTurnTime, scoreLabel, mainFrame);
		super.turnsTakenCounter.setDifficultyModeLabel("Combo Level");
		GameLevel.currentScore.setDifficultyModeLabel2("Combo Level");
		cardsToTurnUp = 5;
		cardsPerRow = 10;
		rowsPerGrid = 5;
		
		GameManager.audioClip.close();
		GameManager.audioFile = new File("audio/musicCombo.wav");
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
			this.grid.add(new Card(this, cardIcon[num], backIcon, num, rank,
					suit));
		}

	}

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
			// Cards for flush
			Card otherCard1 = (Card) this.turnedCardsBuffer.get(0);
			Card otherCard2 = (Card) this.turnedCardsBuffer.get(1);
			Card otherCard3 = (Card) this.turnedCardsBuffer.get(2);
			Card otherCard4 = (Card) this.turnedCardsBuffer.get(3);

			// determines whether a hand is a winning hand or not
			isAWin = NewGameWinningHand(transformToInts(t));
			isAWinFlush = FlushLevel.isFlushWin(card, otherCard1, otherCard2,
					otherCard3, otherCard4);
			isAWinStraight = StraightLevel.straightWinningHand(StraightLevel
					.transformToInts(t));

			// creates a frame when a possible winning hand is detected
			if (isAWin || isAWinFlush || isAWinStraight) {
				frame = new JFrame("Scoring Selection");
				frame.add(createPanel());
				frame.setSize(400, 250);
				frame.setLocation(500, 300);
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.setVisible(true);
				i = i + 1;
				if (i == 7) {

					JOptionPane.showMessageDialog(mainFrame,
							"Congratulation!!!\r\n"
									+ "You've just beaten a game\r\n"
									+ "that was mediocre at best.\r\n" + "\r\n"
									+ "If you want to play again, press\r\n"
									+ "the OK button and then click the\r\n"
									+ "New Game tab on the top menu.");
				}

			} else {
				// The cards do not match, so start the timer to turn them down
				GameLevel.turnDownTimer.start();
				GameLevel.currentScore.scoreDecrease();
			}

		}

		return true;
	}

	/**
	 * Clear cards in order for them to stay face up
	 */
	private void clearCards() {
		this.turnedCardsBuffer.clear();
	}

	// creates and groups the RadioButtons
	public JPanel createPanel() {
		JPanel panel = new JPanel();
		ButtonGroup group = new ButtonGroup();

		tSS = new JRadioButton("Two Step Straight");
		sS = new JRadioButton("Straight");
		f = new JRadioButton("Flush");
		sF = new JRadioButton("Straight Flush");
		pass = new JRadioButton("Pass");
		ok = new JButton("OK");

		group.add(tSS);
		group.add(sS);
		group.add(f);
		group.add(sF);
		group.add(pass);

		panel.add(tSS);
		panel.add(f);
		panel.add(sS);
		panel.add(sF);
		panel.add(pass);

		ok.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {

				// does the shown method when the button is selected...
				if (f.isSelected() && isAWinFlush) {
					ComboLevel.test = true;
					ComboLevel.incrementScore();
					clearCards();

					frame.dispose();
				}

				else if (f.isSelected()
						&& (isAWinStraight || isAWin || (isAWinStraight && isAWinFlush))) {
					ComboLevel.test = true;
					ComboLevel.decreaseScore2();
					JOptionPane.showMessageDialog(mainFrame, "Cheater!!!\r\n"
							+ "You have been caught trying to cheat.\r\n"
							+ "For more information read the 'WARNING' tab\r\n"
							+ "in the Help Menu.");
					clearCards();

					frame.dispose();
				}

				else if (sS.isSelected() && isAWinStraight) {
					ComboLevel.test = true;
					ComboLevel.incrementScore2();
					clearCards();

					frame.dispose();
				}

				else if (sS.isSelected()
						&& (isAWinFlush || isAWin || (isAWin && isAWinFlush))) {
					ComboLevel.test = true;
					ComboLevel.decreaseScore2();
					JOptionPane.showMessageDialog(mainFrame, "Cheater!!!\r\n"
							+ "You have been caught trying to cheat.\r\n"
							+ "For more information read the 'WARNING' tab\r\n"
							+ "in the Help Menu.");
					clearCards();

					frame.dispose();
				}

				else if (tSS.isSelected() && isAWin) {
					ComboLevel.test = true;
					ComboLevel.incrementScore3();
					clearCards();

					frame.dispose();
				}

				else if (tSS.isSelected()
						&& (isAWinStraight || isAWinFlush || (isAWinStraight && isAWinFlush))) {
					ComboLevel.test = true;
					ComboLevel.decreaseScore2();
					JOptionPane.showMessageDialog(mainFrame, "Cheater!!!\r\n"
							+ "You have been caught trying to cheat.\r\n"
							+ "For more information read the 'WARNING' tab\r\n"
							+ "in the Help Menu.");
					clearCards();

					frame.dispose();
				}

				else if (sF.isSelected() && isAWinFlush && isAWinStraight) {
					ComboLevel.test = true;
					ComboLevel.incrementScore4();
					clearCards();

					frame.dispose();
				}

				else if (sF.isSelected()
						&& (isAWin || isAWinStraight || isAWinFlush)) {
					ComboLevel.test = true;
					ComboLevel.decreaseScore2();
					JOptionPane.showMessageDialog(mainFrame, "Cheater!!!\r\n"
							+ "You have been caught trying to cheat.\r\n"
							+ "For more information read the 'WARNING' tab\r\n"
							+ "in the Help Menu.");
					clearCards();
					frame.dispose();
				}

				else if (pass.isSelected()) {
					ComboLevel.test = true;
					ComboLevel.decreaseScore();
					GameLevel.turnDownTimer.start();
					frame.dispose();

				}

			}

		});

		panel.add(ok);

		return panel;

	}

	// Increment scores give adds or decreases the score of the combination that
	// is picked
	// Flush
	public static void incrementScore() {
		if (test) {
			GameLevel.currentScore.scoreIncrement5();
		}
	}

	// Straight
	public static void incrementScore2() {
		if (test) {
			GameLevel.currentScore.scoreIncrement4();
		}
	}

	// Two Step Straight
	public static void incrementScore3() {
		if (test) {
			GameLevel.currentScore.scoreIncrement10();
		}
	}

	// Straight Flush
	public static void incrementScore4() {
		if (test) {
			GameLevel.currentScore.scoreIncrement8();
		}
	}

	// Pass
	public static void decreaseScore() {
		if (test) {
			GameLevel.currentScore.scoreDecrease();
			GameLevel.turnDownTimer.start();
		}
	}

	// Cheat decrease
	public static void decreaseScore2() {
		if (test) {
			GameLevel.currentScore.scoreDecrease2();
			GameLevel.turnDownTimer.start();
		}
	}

	// sorts an array of ints in ascending order
	public static int[] BubbleSortAscending(int[] arr) {
		int temp;
		for (int i = 0; i < arr.length - 1; i++) {

			for (int j = 1; j < arr.length - i; j++) {
				if (arr[j - 1] > arr[j]) {
					temp = arr[j - 1];
					arr[j - 1] = arr[j];
					arr[j] = temp;
				}
			}

		}
		return arr;
	}

	// turns array of strings into array of ints, and sorts them in ascending
	// order
	public static int[] transformToInts(Vector<Card> t) {
		int rank[] = { 0, 0, 0, 0, 0 };

		for (int i = 0; i < t.size(); i++) {
			switch (t.get(i).getRank()) {

			case "a":
				rank[i] = 1;
				break;

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

			}

		}
		rank = BubbleSortAscending(rank);

		return rank;
	}

	// determines if the current hand is a winning hand in Two Step Straight
	public static boolean NewGameWinningHand(int[] arr) {
		int k = 0;
		for (int i = 0; i < arr.length - 1; i++) {
			if (arr[i] + 2 == arr[i + 1]) {
				k++;
			} else {
				return false;
			}
		}
		if (k == 4) {
			return true;
		} else {
			return false;
		}
	}
}