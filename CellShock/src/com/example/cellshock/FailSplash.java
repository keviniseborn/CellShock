//Splash Screen Acitivity for when a level has been lost
package com.example.cellshock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Canvas;
import android.graphics.Bitmap; 
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class FailSplash extends ActionBarActivity {

  final String TAG = "States";
  boolean continueMusic;
  boolean valM, valS;
  int currentLevel, currentScore;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
	 Intent mIntent = getIntent();
	valM = mIntent.getBooleanExtra("valMusic", true);
	valS = mIntent.getBooleanExtra("valSound", true);
	currentLevel = mIntent.getIntExtra("level", currentLevel);
	currentScore = mIntent.getIntExtra("score", currentLevel);
	requestWindowFeature(Window.FEATURE_NO_TITLE);//Set full screen
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fail_layout);
    
    TextView textView = (TextView) findViewById(R.id.textview);
    textView.setText(String.valueOf(currentScore));
    
    if(valM == true){
        MusicManager.start(this, MusicManager.MUSIC_MENU);
        }
    
    isHighScore(currentScore, currentLevel);
  }

  @Override
  protected void onRestart() {
    super.onRestart();
   
  }

	@Override
	protected void onStart() {
		super.onStart();
	}
	@Override
	protected void onPause() {
		super.onPause();
		if(!continueMusic || !valM)
		{
			MusicManager.pause();
		}
	}
	@Override
	protected void onResume() {
		Intent mIntent = getIntent();
		valM = mIntent.getBooleanExtra("valMusic", true);
		valS = mIntent.getBooleanExtra("valSound", true); 
		super.onResume();
		continueMusic = false;
		if(valM == true)
		{
			MusicManager.start(this, MusicManager.MUSIC_MENU);
		}else{MusicManager.pause();}

	}
	@Override
	protected void onStop() {
		super.onStop();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
  

  public void Back(View view) 
  {
	  Intent intent = new Intent(this, LevelActivity.class);
      intent.putExtra("valMusic", valM);
      intent.putExtra("valSound", valS);
	  startActivity(intent);
  }
  
  public void setHighScore(int score, int level)
	{
		if(level == 1)
		{
			SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
			Editor editor = prefs.edit();
			editor.putInt("levelOne", score);
			editor.commit();
			
		}
		if(level == 2)
		{
			SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
			Editor editor = prefs.edit();
			editor.putInt("levelTwo", score);
			editor.commit();
			
		}
		if(level == 3)
		{
			SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
			Editor editor = prefs.edit();
			editor.putInt("levelThree", score);
			editor.commit();
			
		}
		if(level == 4)
		{
			SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
			Editor editor = prefs.edit();
			editor.putInt("levelFour", score);
			editor.commit();
			
		}
		if(level == 5)
		{
			SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
			Editor editor = prefs.edit();
			editor.putInt("levelFive", score);
			editor.commit();
			
		}
		
	}
	
	public boolean isHighScore(int score, int level)
	{
		int highScore = 0;
		
		if(level == 1)
		{
			SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
			highScore = prefs.getInt("levelOne", 0); //0 is the default value
			
		}
		if(level == 2)
		{
			SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
			highScore = prefs.getInt("levelTwo", 0); 
			
		}
		if(level == 3)
		{
			SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
			highScore = prefs.getInt("levelThree", 0); 
			
		}
		if(level == 4)
		{
			SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
			highScore = prefs.getInt("levelFour", 0);
		}
		if(level == 5)
		{
			SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
			highScore = prefs.getInt("levelFive", 0); 	
		}
		
		if(score > highScore){setHighScore(score, level); return true;}else{return false;}
	}
}
