package com.example.cellshock;

import java.io.IOException;
import java.io.InputStream;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.Window;
import android.view.GestureDetector;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class MainActivity extends ActionBarActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener{
	
		private static final int LOW_DPI_STATUS_BAR_HEIGHT = 19;
	    private static final int MEDIUM_DPI_STATUS_BAR_HEIGHT = 25;
	    private static final int HIGH_DPI_STATUS_BAR_HEIGHT = 38;
	     
	    private GestureDetectorCompat mDetector; 
	    
		boolean valM, valS;
		
	    private ScreenView sv;
	    boolean continueMusic;
	    private int width;
	    private int height;
	     
	    private float xPos = 0; 
	    private float yPos = 0;
	    private int statusBarHeight = 0;
	     
	    private static final String DEBUG_TAG = "Gestures";
	     
	    private VelocityTracker mVelocityTracker = null;
	     
	    private static final int SINGLE_PRESS = 1;
	    private static final int DOUBLE_PRESS = 2;
	    private static final int FLING = 3;
	    private static final int LONG_PRESS = 4;
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent mIntent = getIntent();
		int levelValue = mIntent.getIntExtra("levelNum", 0);
		valM = mIntent.getBooleanExtra("valMusic", true);
		valS = mIntent.getBooleanExtra("valSound", true);
		/* Make sure that import android.view.Window;
		import android.view.WindowManager; have been added so 
		the below will work*/
		requestWindowFeature(Window.FEATURE_NO_TITLE);//Set full screen
		super.onCreate(savedInstanceState);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
        //Get the height and width of the screen
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
         
        width = metrics.widthPixels;
        height = metrics.heightPixels;

		sv = new ScreenView(this, width, height, levelValue, valM, valS);
        setContentView(sv);
        
        // Instantiate the gesture detector with the
        // application context and an implementation of
        // GestureDetector.OnGestureListener
        mDetector = new GestureDetectorCompat(this,this);
         
        // Set the gesture detector as the double tap
        // listener.
        mDetector.setOnDoubleTapListener(this);
        


	}
	
	public boolean onTouchEvent(MotionEvent event) {
        int index = event.getActionIndex();
        int action = event.getActionMasked();
        int pointerId = event.getPointerId(index);
         
        int eventaction = event.getAction();
         
        this.mDetector.onTouchEvent(event);
        
        switch (eventaction) {
            case MotionEvent.ACTION_DOWN: 
                if(mVelocityTracker == null) {
                    // Retrieve a new VelocityTracker object to watch the velocity of a motion.
                    mVelocityTracker = VelocityTracker.obtain();
                    // finger touches the screen
                    xPos = event.getX();
                    yPos = event.getY();
                    //Log.d("Finger Position:",String.valueOf(xPos)+ " " +String.valueOf(yPos));
                    // tell the system that we handled the event and no further processing is required
                    sv.updatePos((int) (xPos), (int) (yPos));
                    sv.toggleOptions((int) (xPos), (int) (yPos));
              
                }
                else {
                    // Reset the velocity tracker back to its initial state.
                    mVelocityTracker.clear();
                }
                // Add a user's movement to the tracker.
                mVelocityTracker.addMovement(event);
                
               // Log.d("ADebugTag", "this.xPos: " + Float.toString(event.getX()));
               // Log.d("ADebugTag", "this.yPos: " + Float.toString(event.getY()));
                // finger touches the screen
                xPos = event.getX();
                yPos = event.getY();
                //Log.d("Finger Position:",String.valueOf(xPos)+ " " +String.valueOf(yPos));
                // tell the system that we handled the event and no further processing is required
                

                // else if (event.getAction()==MotionEvent.ACTION_UP )  { 
        		//	
            	//}
   
                sv.updatePos((int) (xPos), (int) (yPos));
               // sv.checkJoystick();
                break;
 
            case MotionEvent.ACTION_MOVE:
                mVelocityTracker.addMovement(event);
                // When you want to determine the velocity, call 
                // computeCurrentVelocity(). Then call getXVelocity() 
                // and getYVelocity() to retrieve the velocity for each pointer ID. 
                mVelocityTracker.computeCurrentVelocity(1000);
                // Log velocity of pixels per second
                // Best practice to use VelocityTrackerCompat where possible.
              
                 /*
                sv.setVelocity(VelocityTrackerCompat.getXVelocity(mVelocityTracker, 
                        pointerId), VelocityTrackerCompat.getYVelocity(mVelocityTracker,
                        pointerId));
                 */
                // finger moves on the screen
                //xPos = event.getX();
                //yPos = event.getY();
                //Log.d("Finger Position:",String.valueOf(xPos)+ " " +String.valueOf(yPos));
       
                sv.updatePos((int) (xPos), (int) (yPos));
                sv.toggleOptions((int) (xPos), (int) (yPos));
                break;
 
            case MotionEvent.ACTION_UP:
    
            	sv.updatePos((int) (xPos), (int) (yPos));
                sv.setFalse();
                break;
             
            case MotionEvent.ACTION_CANCEL:
                // Return a VelocityTracker object back to be re-used by others.
                mVelocityTracker.recycle();
                break;
                 
        }
         
 
         
        // Be sure to call the superclass implementation
        //return super.onTouchEvent(event); 
        return true;
    }
	
    @Override
    public boolean onDoubleTap(MotionEvent event) {
       // Log.d(DEBUG_TAG, "onDoubleTap: " + event.toString());
        return true;
    }
 
    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
       // Log.d(DEBUG_TAG, "onDoubleTapEvent: " + event.toString());
        //sv.setPress(this.DOUBLE_PRESS);
        return true;
    }
 
    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
       // Log.d(DEBUG_TAG, "onSingleTapConfirmed: " + event.toString());
        //sv.setPress(this.SINGLE_PRESS);
        return true;
    }
 
    @Override
    public boolean onDown(MotionEvent event) {
      //  Log.d(DEBUG_TAG,"onDown: " + event.toString()); 
        xPos = event.getX();
        yPos = event.getY();
        
      //  sv.updatePos((int) (xPos), (int) (yPos));
        return true;
    }
 
    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2, 
            float velocityX, float velocityY) {
      // Log.d(DEBUG_TAG, "onFling: " + event1.toString()+event2.toString());
       // sv.setPress(this.FLING);
        return true;
    }
 
    @Override
    public void onLongPress(MotionEvent event) {
       // Log.d(DEBUG_TAG, "onLongPress: " + event.toString());
        xPos = event.getX();
        yPos = event.getY();
        
      //  sv.updatePos((int) (xPos), (int) (yPos));
        //sv.setPress(this.LONG_PRESS);
    }
 
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
            float distanceY) {
       // Log.d(DEBUG_TAG, "onScroll: " + e1.toString()+e2.toString());
        return true;
    }
 
    @Override
    public void onShowPress(MotionEvent event) {
       // Log.d(DEBUG_TAG, "onShowPress: " + event.toString());
    }
 
    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        //Log.d(DEBUG_TAG, "onSingleTapUp: " + event.toString());
        return true;
    }

	
	@Override
    protected void onPause(){
        super.onPause();
        sv.pause();
        if(!continueMusic)
		{
			MusicManager.pause();
		}
    }
     
    @Override
    protected void onResume(){
        super.onResume();
        sv.resume();
        continueMusic = false;
        if(valM == true)
        {
        	MusicManager.start(this, MusicManager.MUSIC_GAME);
        }
    }
    
    public void setPress(int i){
    	
    }
    

}