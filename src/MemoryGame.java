import java.awt.Color;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;




public class MemoryGame implements ActionListener {

	public static boolean DEBUG = true;
	private JFrame mainFrame;					// top level window
	private Container mainContentPane;			// frame that holds card field and turn counter
	private TurnsTakenCounterLabel turnCounterLabel;
	private ScoreCalculationLabel scoreLabel;
	private GameLevel difficulty;
	
	


	/**
	 * Make a JMenuItem, associate an action command and listener, add to menu
	 */
	private static void newMenuItem(String text, JMenu menu, ActionListener listener)
	{
		JMenuItem newItem = new JMenuItem(text);
		newItem.setActionCommand(text);
		newItem.addActionListener(listener);
		menu.add(newItem);
	}

	/**
	 * Default constructor loads card images, makes window
	 * @throws IOException 
	 */
	public MemoryGame () throws IOException
	{


		// Make toplevel window
		this.mainFrame = new JFrame("Passive Aggressive Memory Game");
		this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.mainFrame.setSize(800,700);
		this.mainContentPane = this.mainFrame.getContentPane();
		this.mainContentPane.setLayout(new BoxLayout(this.mainContentPane, BoxLayout.PAGE_AXIS));
		this.mainContentPane.setBackground(Color.LIGHT_GRAY);
		



		// Menu bar
		JMenuBar menuBar = new JMenuBar();
		this.mainFrame.setJMenuBar(menuBar);
		menuBar.setBackground(Color.LIGHT_GRAY);

		// Game menu
		JMenu gameMenu = new JMenu("Memory");
		menuBar.add(gameMenu);
		newMenuItem("Exit", gameMenu, this);

		// Difficulty menu
		JMenu difficultyMenu = new JMenu("New Game");
		menuBar.add(difficultyMenu);
		newMenuItem("Easy Level", difficultyMenu, this);
		newMenuItem("Equal Pair Level", difficultyMenu, this);
		newMenuItem("Same Rank Trio Level", difficultyMenu, this);
		newMenuItem("Straight Level", difficultyMenu, this);
		newMenuItem("Flush Level", difficultyMenu, this);
		newMenuItem("Combo Level", difficultyMenu, this);


		// Help menu
		JMenu helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);
		newMenuItem("How To Play", helpMenu, this);
		newMenuItem("How To Play(Advanced)", helpMenu, this);
		newMenuItem("Hand Values", helpMenu, this);
		newMenuItem("How To Play(Hardcore)", helpMenu, this);
		newMenuItem("WARNING", helpMenu, this);
		newMenuItem("About", helpMenu, this);


		//this.leaderBoard = new ScoreLeaderBoard("EasyMode");
	}



	/**
	 * Handles menu events.  Necessary for implementing ActionListener.
	 *
	 * @param e object with information about the event
	 */
	public void actionPerformed(ActionEvent e)

	{
		dprintln("actionPerformed " + e.getActionCommand());
		try {
			if(e.getActionCommand().equals("Easy Level")) newGame("easy");
			else if(e.getActionCommand().equals("Equal Pair Level")) newGame("medium");
			else if(e.getActionCommand().equals("Same Rank Trio Level")) newGame("trio");
			else if(e.getActionCommand().equals("Flush Level")) newGame("Flush Level");
			else if(e.getActionCommand().equals("Straight Level")) newGame("Straight Level");
			else if(e.getActionCommand().equals("Combo Level")) newGame("Combo Level");
			else if(e.getActionCommand().equals("How To Play")) showInstructions();
			else if(e.getActionCommand().equals("How To Play(Advanced)")) showInstructions2();
			else if(e.getActionCommand().equals("How To Play(Hardcore)")) showInstructions3();
			else if(e.getActionCommand().equals("About")) showAbout();
			else if(e.getActionCommand().equals("Hand Values")) showHandValues();
			else if(e.getActionCommand().equals("WARNING")) showWARNING();
			else if(e.getActionCommand().equals("Exit")) System.exit(0);
		} catch (IOException e2) {
			e2.printStackTrace(); throw new RuntimeException("IO ERROR");
		}
	}




	/**
	 * Prints debugging messages to the console
	 *
	 * @param message the string to print to the console
	 */
	static public void dprintln( String message )
	{
		if (DEBUG) System.out.println( message );
	}

	public JPanel showCardDeck()
	{
		// make the panel to hold all of the cards
		JPanel panel = new JPanel(new GridLayout(difficulty.getRowsPerGrid(),difficulty.getCardsPerRow()));
		panel.setBackground(Color.DARK_GRAY);
		// this set of cards must have their own manager
		this.difficulty.makeDeck();

		for(int i= 0; i<difficulty.getGrid().size();i++){
			panel.add(difficulty.getGrid().get(i));
		}
		return panel;
	}

	/**
	 * Prepares a new game (first game or non-first game)
	 * @throws IOException 
	 */
	public void newGame(String difficultyMode) throws IOException
	{
		// reset the turn and score counter to zero
		this.turnCounterLabel = new TurnsTakenCounterLabel();
		this.scoreLabel = new ScoreCalculationLabel();

		// make a new card field with cards, and add it to the window

		if(difficultyMode.equalsIgnoreCase("easy")) {
			this.difficulty = new EasyLevel(this.turnCounterLabel,this.scoreLabel, this.mainFrame);
		}
		else if(difficultyMode.equalsIgnoreCase("medium")){
			this.difficulty = new EqualPairLevel(this.turnCounterLabel,this.scoreLabel, this.mainFrame);
		}
		else if(difficultyMode.equalsIgnoreCase("trio")){
			this.difficulty = new RankTrioLevel(this.turnCounterLabel,this.scoreLabel, this.mainFrame);
		}
				else if(difficultyMode.equalsIgnoreCase("Straight Level")){
					this.difficulty = new StraightLevel(this.turnCounterLabel,this.scoreLabel, this.mainFrame);
				}
		else if(difficultyMode.equalsIgnoreCase("Flush Level")){
			this.difficulty = new FlushLevel(this.turnCounterLabel,this.scoreLabel, this.mainFrame);
		}
		else if(difficultyMode.equalsIgnoreCase("Combo Level")){
			this.difficulty = new ComboLevel(this.turnCounterLabel,this.scoreLabel, this.mainFrame);
		}

		else {
			throw new RuntimeException("Illegal Game Level Detected");
		}

		this.turnCounterLabel.reset();
		this.scoreLabel.reset();

		// clear out the content pane (removes turn and score counter label and card field)
		this.mainContentPane.removeAll();

		this.mainContentPane.add(showCardDeck());

		// add the turn and score counter label back in again
		this.mainContentPane.add(this.turnCounterLabel);
		this.mainContentPane.add(this.scoreLabel);

		// show the window (in case this is the first game)
		this.mainFrame.setVisible(true);

	}

	public boolean gameOver() throws FileNotFoundException, InterruptedException{
		// Make toplevel window


		return difficulty.gameOver();
	}

	/**
	 * Shows an instructional dialog box to the user
	 */
	private void showInstructions()
	{
		dprintln("MemoryGame.showInstructions()");
		final String HOWTOPLAYTEXT = 
				"How To Play\r\n" +
						"\r\n" +
						"EQUAL PAIR Level\r\n"+
						"The game consists of 8 pairs of cards.  At the start of the game,\r\n"+
						"every card is face down.  The object is to find all the pairs and\r\n"+
						"turn them face up.\r\n"+
						"\r\n"+
						"Click on two cards to turn them face up. If the cards are the \r\n"+
						"same, then you have discovered a pair.  The pair will remain\r\n"+
						"turned up.  If the cards are different, they will flip back\r\n"+
						"over automatically after a short delay.  Continue flipping\r\n"+
						"cards until you have discovered all of the pairs.  The game\r\n"+
						"is won when all cards are face up.\r\n"+
						"\r\n"+
						"SAME RANK TRIO Level\r\n"+
						"The game consists of a grid of distinct cards.  At the start of the game,\r\n"+
						"every card is face down.  The object is to find all the trios \r\n"+
						"of cards with the same rank and turn them face up.\r\n"+
						"\r\n"+
						"Click on three cards to turn them face up. If the cards have the \r\n"+
						"same rank, then you have discovered a trio.  The trio will remain\r\n"+
						"turned up.  If the cards are different, they will flip back\r\n"+
						"over automatically after a short delay.  Continue flipping\r\n"+
						"cards until you have discovered all of the pairs.  The game\r\n"+
						"is won when all cards are face up.\r\n"+
						"\r\n"+
						"Each time you flip two cards up, the turn counter will\r\n"+
						"increase.  Try to win the game in the fewest number of turns!";

		JOptionPane.showMessageDialog(this.mainFrame, HOWTOPLAYTEXT
				, "How To Play", JOptionPane.PLAIN_MESSAGE);
	}

	private void showInstructions2()
	{
		dprintln("MemoryGame.showInstructions2()");
		final String HOWTOPLAYTEXT = 
				"How To Play(Advanced)\r\n"+ 
						"\r\n" +
						"Straight Level\r\n"+
						"The game consists of a grid of distinct cards.  At the start of the game,\r\n"+
						"every card is face down.  The object is to find all the straights \r\n"+
						"and turn them face up.\r\n"+
						"\r\n"+
						"Click on five cards to turn them face up. If the cards are numerically \r\n"+
						"ascending or decending, then you have discovered a straight.  The straight\r\n"+
						"will remain turned up.  If the cards are not continous, they will flip back\r\n"+
						"over automatically after a short delay.  Continue flipping\r\n"+
						"cards until you have discovered all of straights.  The game\r\n"+
						"is won when all available straights are face up.\r\n"+
						"\r\n"+
						"Flush Level\r\n"+
						"The game consists of a grid of distinct cards.  At the start of the game,\r\n"+
						"every card is face down.  The object is to find all five cards \r\n"+
						"with the same suit and turn them face up.\r\n"+
						"\r\n"+
						"Click on five cards to turn them face up. If the cards have the \r\n"+
						"same suit, then you have discovered a flush.  The flush will remain\r\n"+
						"turned up.  If the cards have different suits, they will flip back\r\n"+
						"over automatically after a short delay.  Continue flipping\r\n"+
						"cards until you have discovered all of the flush available.  The game\r\n"+
						"is won when all available flush combinations are face up.\r\n"+
						"\r\n"+
						"Each time you flip five cards up, the turn counter will\r\n"+
						"increase.  Try to win the game in the fewest number of turns!";

		JOptionPane.showMessageDialog(this.mainFrame, HOWTOPLAYTEXT
				, "How To Play(Advanced)", JOptionPane.PLAIN_MESSAGE);
	}
	private void showInstructions3()
    {
        dprintln("MemoryGame.showInstructions3()");
        final String HOWTOPLAYTEXT =
                "How To Play(Hardcore)\r\n"+
                        "Combo Level\r\n"+
                        "\r\n"+
                        "This level is a combination of the previous two levels,\r\n"+
                        "plus the addition of the 'Two Step Straight' and the 'Straight Flush' hands.\r\n"+
                        "The 'Two Step Straight' consist of a normal straight, but with a one rank\r\n"+
                        "gap between the cards and the 'Straight Flush' is a combination of a 'Flush'\r\n"+
                        " and a 'Straight.\r\n"+
                        "\r\n"+
                        "Click on five cards to turn them face up. If the cards make one \r\n"+
                        "of the previously mentioned combinations, then you will have the chance to\r\n"+
                        "pick which combination to use or pass the turn to pick other cards.\r\n"+
                        "If a combination is picked, then the cards will remain turned up. If the cards\r\n"+
                        "do not form a combinatuon or you pass, they will flip back\r\n"+
                        "over automatically after a short delay.  Continue flipping\r\n"+
                        "cards until you have discovered all possible combinations. The game\r\n"+
                        "is won when all available combinations are face up.\r\n";


        JOptionPane.showMessageDialog(this.mainFrame, HOWTOPLAYTEXT
                , "How To Play(Hardcore)", JOptionPane.PLAIN_MESSAGE);
    }
	/**
	 * Shows an dialog box with information about the program
	 */
	private void showAbout()
	{
		dprintln("MemoryGame.showAbout()");
		final String ABOUTTEXT = "Game Customized at UPRM by Kevin Santiago and Juan Agosto. Originally written by Mike Leonhard";

		JOptionPane.showMessageDialog(this.mainFrame, ABOUTTEXT
				, "About Memory Game", JOptionPane.PLAIN_MESSAGE);
	}
//shows obtainable points from hands (some are not used)
	private void showHandValues() {
		dprintln("MemoryGame.showInstructions()");
		final String HANDVALUES =
				"Hand Values\r\n"+
						"Pair:                        10\r\n"+
						"Two Pair:               15\r\n"+
						"Three of a Kind:   20\r\n"+
						"Straight:                25\r\n"+
						"Flush:                     30\r\n"+
						"Full House:            40\r\n"+
						"Four of a Kind:     125\r\n"+
						"Straight Flush:     250\r\n"+
						"Royal Flush:          500";

		JOptionPane.showMessageDialog(this.mainFrame, HANDVALUES
				, "Hand Values", JOptionPane.PLAIN_MESSAGE);
	}
	//Warning for CHEATERS!!!
	private void showWARNING(){
		final String WARNING =
		"WARNING\r\n"+
				"If you have selected a combination which you did not get,\r\n"+
				"you will lose the picked cards and a 20 point deduction will be\r\n"+
				"applied to your score.";

JOptionPane.showMessageDialog(this.mainFrame, WARNING
		, "WARNING", JOptionPane.PLAIN_MESSAGE);
	}

}




