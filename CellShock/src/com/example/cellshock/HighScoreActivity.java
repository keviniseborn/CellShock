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

public class HighScoreActivity extends ActionBarActivity {

  final String TAG = "States";
  boolean continueMusic;
  boolean valM, valS;
  int highScoreOne, highScoreTwo, highScoreThree, highScoreFour, highScoreFive;
  @Override
  public void onCreate(Bundle savedInstanceState) {
	 Intent mIntent = getIntent();
	valM = mIntent.getBooleanExtra("valMusic", true);
	valS = mIntent.getBooleanExtra("valSound", true);
	
	getScore();
	
	requestWindowFeature(Window.FEATURE_NO_TITLE);//Set full screen
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_highscore);
    setText();
    
    if(valM == true){
        MusicManager.start(this, MusicManager.MUSIC_MENU);
        }
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
  

  
  //button to go back to menu
  public void Back(View view) 
  {
	  Intent intent = new Intent(this, MenuActivity.class);
      intent.putExtra("valMusic", valM);
      intent.putExtra("valSound", valS);
	  startActivity(intent);
  }
  
  //button to go back to menu
  public void clearScore(View view) 
  {
	  SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putInt("levelOne", 0);
		editor.putInt("levelTwo", 0);
		editor.putInt("levelThree", 0);
		editor.putInt("levelFour", 0);
		editor.putInt("levelFive", 0);
		editor.commit();
		getScore();
		setText();
  }
  
  public void setText()
  {
	  TextView textView = (TextView) findViewById(R.id.scoreOne);
	    textView.setText(String.valueOf(highScoreOne));
	    
	    textView = (TextView) findViewById(R.id.scoreTwo);
	    textView.setText(String.valueOf(highScoreTwo));
	    
	    textView = (TextView) findViewById(R.id.scoreThree);
	    textView.setText(String.valueOf(highScoreThree));
	    
	    textView = (TextView) findViewById(R.id.scoreFour);
	    textView.setText(String.valueOf(highScoreFour));
	    
	    textView = (TextView) findViewById(R.id.scoreFive);
	    textView.setText(String.valueOf(highScoreFive));  
	  
  }
  
  public void getScore()
  {
		SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
		
		highScoreOne = prefs.getInt("levelOne", 0); //0 is the default value
		highScoreTwo = prefs.getInt("levelTwo", 0); //0 is the default value
		highScoreThree = prefs.getInt("levelThree", 0); //0 is the default value
		highScoreFour = prefs.getInt("levelFour", 0); //0 is the default value
		highScoreFive = prefs.getInt("levelFive", 0); //0 is the default value
  }
}
