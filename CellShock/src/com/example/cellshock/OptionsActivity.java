package com.example.cellshock;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.widget.Toast;
import android.support.v7.app.ActionBarActivity;
import android.widget.ToggleButton;
import android.widget.Switch;


@SuppressLint("NewApi") public class OptionsActivity extends ActionBarActivity {
	/** Called when the activity is first created. */
	

	boolean valM;
	boolean valS;
	 boolean continueMusic;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);//Set full screen
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options);
		Toast.makeText(this, "onCreate()", Toast.LENGTH_LONG).show();
		//valM = true;
		//valS = true;
	}
	
	
	@Override
	protected void onStart() {
		//the activity is become visible.
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
		super.onResume();
		 continueMusic = false;
		 if(valM == true)
		 {
			MusicManager.start(this, MusicManager.MUSIC_MENU);
		 }else{MusicManager.pause();}
	}
	@Override
	protected void onStop() {
	//the activity is no longer visible.
		super.onStop();
	}
	@Override
	protected void onDestroy() {
	//The activity is about to be destroyed.
		super.onDestroy();
	}
	

	public void toggleMusic(View view) 
	  {
	
		boolean on = ((Switch) view).isChecked();
	    
	    if (on) {
	    	valM = true;
	    	MusicManager.start(this, MusicManager.MUSIC_MENU);
	    } else {
	    	valM = false;
	    	MusicManager.pause();
	    }

	  }
	public void toggleSound(View view) 
	  {
		//((Switch) view).setChecked(valS);

		boolean on = ((Switch) view).isChecked();
	    
	    if (on) {
	    	valS = true;
	    	
	    } else {
	    	valS = false;
	    }

	  }
	public void mainMenu(View view) 
	  {
	      Intent intent = new Intent(this, MenuActivity.class);
	      intent.putExtra("valMusic", valM);
	      intent.putExtra("valSound", valS);
	      startActivity(intent);
	  }
	
}