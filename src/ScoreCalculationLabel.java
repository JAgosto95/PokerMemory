import java.util.Vector;

import javax.swing.JLabel;

public class ScoreCalculationLabel extends JLabel{

	//a[i]=Integer.parseString(a[i]);
	private static final long serialVersionUID = 1L;
	private int score= 0;
	private  String DESCRIPTION2;
	protected Vector<Card> turnedCardsBuffer;

	public ScoreCalculationLabel(){
		super();
		reset();
	}


	public void setDifficultyModeLabel2(String difficultyMode){
		DESCRIPTION2 = "Current Score: ";
		setHorizontalTextPosition(JLabel.LEFT);
	}

	public int getScore(){
		return this.score;
	}

	
	//Updates the score label
	private void update(){
		setText (DESCRIPTION2 + Integer.toString(this.score));
		setHorizontalTextPosition(JLabel.LEFT);
	}
	
	//Score counter starts at 0
	
	//Add Pair value to score
	public void scoreIncrement(){
		this.score= this.score + 10;
		update();
	}

	//Add Two Pair value to score
	public void scoreIncrement2(){
		this.score= this.score + 15;
		update();
	}
	//Add Three of a Kind value to score
	public void scoreIncrement3() {
		this.score= this.score + 20;
		update();

	}
	//Add Straight value to score
	public void scoreIncrement4(){
		this.score= this.score + 25;
		update();
	}
	//Add Flush value to score
	public void scoreIncrement5(){
		this.score= this.score + 30;
		update();
	}
	
	//Add Full House value to score
	public void scoreIncrement6(){
		this.score= this.score + 40;
		update();
	}
	//Add Four of a Kind value to score
	public void scoreIncrement7(){
		this.score= this.score + 125;
		update();
	}
	//Add Straight Flush value to score
	public void scoreIncrement8(){
		this.score= this.score + 250;
		update();
	}
	//Add Royal Flush value to score
	public void scoreIncrement9(){
		this.score= this.score + 500;
		update();
	}
	//Add Two Step Straight value to score
	public void scoreIncrement10(){
		this.score= this.score + 60;
		update();
	}
	//Subtract from score for missed turn
	public void scoreDecrease(){
		this.score= this.score -2;
		update();
	}
	//Subtract from score for Cheating
	public void scoreDecrease2(){
		this.score= this.score -20;
		update();
	}
	//Resets the score label
	void reset() {
		this.score=0;
		update();
	}
}