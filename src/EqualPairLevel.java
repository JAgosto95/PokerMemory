import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class EqualPairLevel extends EasyLevel {
	int i=0;
	protected EqualPairLevel(TurnsTakenCounterLabel validTurnTime, ScoreCalculationLabel scoreLabel, JFrame mainFrame) {
		super(validTurnTime,scoreLabel, mainFrame);
		super.turnsTakenCounter.setDifficultyModeLabel("Medium Level");
		GameLevel.currentScore.setDifficultyModeLabel2("Medium Level");
		
		//changes music for the level
		GameManager.audioClip.close();
		GameManager.audioFile = new File("audio/musicEqualPair.wav");
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
	protected boolean addToTurnedCardsBuffer(Card card) {
		this.turnedCardsBuffer.add(card);
		if(this.turnedCardsBuffer.size() == getCardsToTurnUp())
		{
			// there are two cards faced up
			// record the player's turn
			this.turnsTakenCounter.increment();
			// get the other card (which was already turned up)
			Card otherCard = (Card) this.turnedCardsBuffer.get(0);
			// the cards match, so remove them from the list (they will remain face up)
			if( otherCard.getNum() == card.getNum())
			{
				//Determines when the game has ended
				i=i+1;
				if(i==8){
					
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(mainFrame, "Congratulation!!!\r\n"+
							"You've just beaten a game\r\n"+
							"that was mediocre at best.\r\n"+
							"\r\n"+
							"If you want to play again, press\r\n"+
							"the OK button and then click the\r\n"+
							"New Game tab on the top menu.");
				}
			}
			//Increases or decreases score after each turn
			if( otherCard.getNum() == card.getNum()){
				GameLevel.currentScore.scoreIncrement();
				this.turnedCardsBuffer.clear();
			}

			// the cards do not match, so start the timer to turn them down
			else{
				GameLevel.currentScore.scoreDecrease();
				GameLevel.turnDownTimer.start();
			}
		}
		return true;
	}


	@Override
	protected boolean turnUp(Card card) {
		// the card may be turned
		if(this.turnedCardsBuffer.size() < getCardsToTurnUp()) 
		{
			return this.addToTurnedCardsBuffer(card);
		}
		// there are already the number of EasyMode (two face up cards) in the turnedCardsBuffer
		return false;
	}

	@Override
	protected String getMode() {
		// TODO Auto-generated method stub
		return "MediumMode";
	}



}