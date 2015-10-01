package com.example.cellshock;

import android.app.Activity;
import android.content.Intent;
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

public class MenuActivity extends ActionBarActivity {

  final String TAG = "States";
  boolean continueMusic;
  boolean valM, valS;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
	  Intent mIntent = getIntent();
		valM = mIntent.getBooleanExtra("valMusic", true);
		valS = mIntent.getBooleanExtra("valSound", true); 
	  
	requestWindowFeature(Window.FEATURE_NO_TITLE);//Set full screen
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_menu);
    Log.d(TAG, "MenuActivity: onCreate()");
    
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
  
  public void gameStart(View view) 
  {
      Intent intent = new Intent(this, LevelActivity.class);
      intent.putExtra("valMusic", valM);
      intent.putExtra("valSound", valS);
      startActivity(intent);
  }
  public void gameOptions(View view) 
  {
	  Intent intent = new Intent(this, OptionsActivity.class);
      intent.putExtra("valMusic", valM);
      intent.putExtra("valSound", valS);
      startActivity(intent);
  }
  
  public void highScores(View view)
  {
	  Intent intent = new Intent(this, HighScoreActivity.class);
      intent.putExtra("valMusic", valM);
      intent.putExtra("valSound", valS);
	  startActivity(intent);
	  
  }
  public void Exit(View view) 
  {
	  onDestroy();
	  finish();
	  System.exit(0);
  }
}
