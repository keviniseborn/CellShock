//score class to track players score
package com.example.cellshock;

import android.R.string;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Score {

	private int currentScore;		//int variable for score
	private String stringScore;		//string variable to output score to screen
	
	//constructor
	public Score (int startScore){
		this.currentScore = startScore;
	}
	
	//set score
	public void setScore(int n){
		
		this.currentScore = n;
	}
	
	//add score to current score
	public void addScore(int n){
		
		this.currentScore += n;
	}
	
	//subtract score from current score
	public void subScore(int n){
	
	this.currentScore -= n;
	}
	
	//returns the score as a string variable
	public String getScoreString(){
		
		return this.stringScore = Integer.toString(currentScore);
		}
	
	//get score as an int
	public int getScoreInt(){
		
		return this.currentScore;
		}
	
	
}
