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

public class LevelActivity extends ActionBarActivity {

  final String TAG = "States";
  boolean continueMusic;
  boolean valM, valS;
  int highScoreOne, highScoreTwo, highScoreThree, highScoreFour, highScoreFive, levelUnlocked;
  @Override
  public void onCreate(Bundle savedInstanceState) {
	 Intent mIntent = getIntent();
	valM = mIntent.getBooleanExtra("valMusic", true);
	valS = mIntent.getBooleanExtra("valSound", true);
	
	SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
	
	highScoreOne = prefs.getInt("levelOne", 0); //0 is the default value
	highScoreTwo = prefs.getInt("levelTwo", 0); //0 is the default value
	highScoreThree = prefs.getInt("levelThree", 0); //0 is the default value
	highScoreFour = prefs.getInt("levelFour", 0); //0 is the default value
	highScoreFive = prefs.getInt("levelFive", 0); //0 is the default value
	levelUnlocked = prefs.getInt("levelUnlock", 1);
	requestWindowFeature(Window.FEATURE_NO_TITLE);//Set full screen
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_level);
    
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
  
	//button to load level one
  public void levelOne(View view) 
  {
      Intent intent = new Intent(this, MainActivity.class);
      intent.putExtra("levelNum", 1);
      intent.putExtra("valMusic", valM);
      intent.putExtra("valSound", valS);
      startActivity(intent);
  }
  
  //button to load level two
  public void levelTwo(View view) 
  {
	  if(levelUnlocked >= 2)
	  {
	  Intent intent = new Intent(this, MainActivity.class);
	  intent.putExtra("levelNum", 2);
      intent.putExtra("valMusic", valM);
      intent.putExtra("valSound", valS);
	  startActivity(intent);
	  }
  }
  
  //button to load level three
  public void levelThree(View view) 
  {
	  if(levelUnlocked >= 3)
	  {
	  Intent intent = new Intent(this, MainActivity.class);
	  intent.putExtra("levelNum", 3);
      intent.putExtra("valMusic", valM);
      intent.putExtra("valSound", valS);
      startActivity(intent);
	  }
  }
  
  //button to load level four
  public void levelFour(View view) 
  {
	  if(levelUnlocked >= 4)
	  {
	  Intent intent = new Intent(this, MainActivity.class);
	  intent.putExtra("levelNum", 4);
      intent.putExtra("valMusic", valM);
      intent.putExtra("valSound", valS);
      startActivity(intent);
	  }
  }
  
  //button to load level five
  public void levelFive(View view) 
  {
	  if(levelUnlocked >= 4)
	  {
	  Intent intent = new Intent(this, MainActivity.class);
	  intent.putExtra("levelNum", 5);
      intent.putExtra("valMusic", valM);
      intent.putExtra("valSound", valS);
      startActivity(intent);
	  }
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
  public void newGame(View view) 
  {
	  SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putInt("levelUnlock", 1);
		levelUnlocked = 1;
		editor.commit();
  }
}
