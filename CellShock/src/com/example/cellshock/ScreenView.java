package com.example.cellshock;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Canvas;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;


@SuppressLint("WrongCall")//Needed to suppress the lint error
public class ScreenView extends SurfaceView implements Runnable{
	

	private boolean collided;
	
	boolean valM, valS;
	 int screenWidth, screenHeight;
	 int currentAction;
	//x and y positions of the "finger"
	int mousePosX, mousePosY;
	
	@SuppressWarnings("deprecation")
	SoundPool sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);				//soundpool object for in game sounds
	int soundLaser = sp.load(getContext(), R.raw.lasersound, 1);				//laser sound
	int soundCell = sp.load(getContext(), R.raw.ballsound, 1);					//cell sound
	
	
	Bitmap background = BitmapFactory.decodeResource(getResources(), R.drawable.background);		//bitmap image for background
	
	//bitmaps for buttons, N = not pressed, P = pressed
	Bitmap leftArrowP = BitmapFactory.decodeResource(getResources(), R.drawable.leftpressed);		//left pressed
	Bitmap leftArrowN = BitmapFactory.decodeResource(getResources(), R.drawable.leftnotpressed);	//left not pressed
	Bitmap rightArrowP = BitmapFactory.decodeResource(getResources(), R.drawable.rightpressed);		//right pressed
	Bitmap rightArrowN = BitmapFactory.decodeResource(getResources(), R.drawable.rightnotpressed);	//right not pressed
	Bitmap downArrowP = BitmapFactory.decodeResource(getResources(), R.drawable.downpressed);		//down pressed
	Bitmap downArrowN = BitmapFactory.decodeResource(getResources(), R.drawable.downnotpressed);	//down not pressed
	Bitmap upArrowP = BitmapFactory.decodeResource(getResources(), R.drawable.uppressed);			//up pressed
	Bitmap upArrowN = BitmapFactory.decodeResource(getResources(), R.drawable.upnotpressed);		//up not pressed
	
	Bitmap laserP = BitmapFactory.decodeResource(getResources(), R.drawable.laserpressed);			//fire button pressed
	Bitmap laserN = BitmapFactory.decodeResource(getResources(), R.drawable.lasernotpressed);		//fire button not pressed
	
	//booleans to keep track of lasers movement and firing
	private boolean isLeft, isRight, isUp, isDown, isFiring;
	
	SurfaceHolder holder; //Surface holder for the canvas
	private boolean ok = false; //Boolean to control pause and resume
	
	//context to change activity in screenview
	Context contextChange;
	int currentLevel;
	
	PetriDish petriDish = new PetriDish();			//petri dish object
	Laser laser = new Laser();						//laser object
	
	Thread t = null; //Thread for the game logic

	Cell[] ballArray = new Cell[2]; //array of balls
	Cell e1, e2;					//temp variables for gets in collision detection

	private Score score = new Score(0); //score variable - start at 0
	Random generator = new Random(); 	//random generator
	Paint paint = new Paint();			//paint object
	ArrayList<Cell> cells = null;		//array list of cells in game
	int cellCounter;						//counter for cells left
	
	public ScreenView(Context context, int w, int h, int levelNum, boolean music, boolean sound) {
		
		super(context);
		contextChange = context;
		holder = getHolder();//Used for the screenview
			  
		valM = music;
		valS = sound;
		
		loadCells(levelNum);	//load cells function
		currentLevel = levelNum;
		
		screenWidth = w;
		screenHeight = h;
		 for(int i = 0; i<cells.size(); i++) //set screen width and height of all cells
		     {
		        this.cells.get(i).setScreenWidth(w);
		        this.cells.get(i).setScreenHeight(h);
		     }
		        
		 petriDish = new PetriDish(145, 300, 324, Color.argb(50,50,50,10), 100); //create petri dish - hard coded as it won't change
		 laser = new Laser(context,250, 250);									 //create laser 
		 

	}
	
	 
	
	protected void onDraw(Canvas canvas){

		paint.setColor(Color.RED);
		canvas.drawBitmap(background, 0, 0, paint); //draw background first
		petriDish.drawPetriDish(paint, canvas);		//draw petridish second
		 laser.update();
		
		//update position for all cells in array
		for(int i = 0; i<cells.size(); i++)	
		{
			this.cells.get(i).update();
		}
		
		//draw all cells in array
		for(int i = 0; i<cells.size(); i++)
		{
			this.cells.get(i).drawCell(paint, canvas);
		}
		
		
		//check if laser is colliding with cells and if the laser is firing
		if(laser.hasBattery() == true)
		{
			for(int i = 0; i<cells.size(); i++)
			{
				e1 = (Cell)cells.get(i);
					if(e1.active == true)
					{
						if(isFiring == true && laser.hitCell(cells.get(i)) == true)
						{
							score.addScore(e1.points);		//add score depending on cells point worth
							if(e1.type != e1.type.WHITE){cellCounter -= 1;}
							this.cells.get(i).active = false;
							this.cells.remove(i);
						}
					}
				
			}
		}
		
		for(int i = 0; i<cells.size(); ++i)
		{
			e1 = (Cell)cells.get(i);
			if(e1.active == true)
			{
				//check if cells have collided with petri dish
				if(petriDish.petriCollision(e1) == true)
				{
					//if the cell type is white add health, otherwise deduct health
				
					if(e1.type.name() == "WHITE")
					{
							petriDish.addHealth();
					}else{ petriDish.deductHealth(e1.radius);}
				
				}
			}
		}
		
		//checks if cells collide with each other and calculate new velocity based on collision 
		for(int i = 0; i<cells.size(); i++)
		{
			for(int j = 0; j<cells.size(); j++)
			{
				Cell e1 = (Cell)cells.get(i);
				Cell e2 = (Cell)cells.get(j);
				if(e1.collisionZone == e2.collisionZone)
				{
					collided = true;
					
					if(e1.circleCircleCollision(e2) == true)
					{
						if(valS == true){sp.play(soundCell, 1, 1, 1, 0, 1);}
						if(this.cells.get(i).mutateCell(e2) == true)
						{ 
							if(e1.radius >= e2.radius)
							{
								this.cells.remove(j);
							}else{this.cells.remove(i);} //if cells are blue type - mutate
							cellCounter = cellCounter - 1; 
						}
					}
				}
			}
		}
	
		
		checkLoss();
		checkWin();
		 
		 drawControls(canvas, paint);
		 moveLaser();
		
		 laser.drawLaser(paint, canvas);
		 paint.setColor(Color.BLACK);
		 canvas.drawText("Score:", 10, 20, paint);
			canvas.drawText("Petri Life:", 10, 40, paint);
			 canvas.drawText(score.getScoreString(), 60, 20, paint);
			 canvas.drawText(petriDish.getHealthString(), 65, 40, paint);
    }
	
	public void run() {
		//Remove conflict between the UI thread and the game thread.
		android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
		
		while (ok == true){
			//perform canvas drawing
			if(!holder.getSurface().isValid()){//if surface is not valid
				continue;//skip anything below it
			}
			Canvas c = holder.lockCanvas(); //Lock canvas, paint canvas, unlock canvas
			this.onDraw(c);
			holder.unlockCanvasAndPost(c);
		}
	}
	
	public void pause(){
		ok = false;
		while(true){
			try{
				t.join();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			break;
		}
		t = null;
	}
	
	public void resume(){
		ok = true;
		t = new Thread(this);
		t.start();
	}
	
	//function for moving laser, if touch is within an area set specific action to true
	public void updatePos(int xPos, int yPos){
		if(xPos > 5 && xPos < 36 && yPos >395 && yPos < 430)
		{
			isLeft = true;
		}
		if(xPos > 80 && xPos < 110 && yPos >395 && yPos < 430)
		{
			isRight = true;
		}
		if(xPos > 40 && xPos < 75 && yPos >395 && yPos < 430)
		{
			isDown = true;
		}
		if(xPos > 50 && xPos < 60 && yPos >365 && yPos < 394)
		{
			isUp = true;
		}
		
		if(xPos > 250 && xPos < 315 && yPos >365 && yPos < 430)
		{
			isFiring = true;
			laser.firing = true;
		}
		
		mousePosX = (int) xPos;
		mousePosY = (int) yPos;
	}
	
	//move laser function
	public void moveLaser()
	{
		if(isLeft == true)
		{
			laser.moveLeft();
		}
		if(isRight == true)
		{
			laser.moveRight();
		}
		if(isDown == true)
		{
			laser.moveDown();
		}
		if(isUp == true)
		{
			laser.moveUp();
		}
		
	}
	
	//set all touch variables to false if not touching screen
	public void setFalse(){
		isLeft = false;
		isRight = false;
		isUp = false;
		isDown = false;
		isFiring = false;
		laser.firing = false;
	}
	
	//draw the controls on the screen
	public void drawControls(Canvas c, Paint p)
	{
		
		//if left is pressed
		if(isLeft == true)
		{
			c.drawBitmap(leftArrowP, 5, 375, p);
		}else{ c.drawBitmap(leftArrowN, 5, 375, p);}
		
		//if right is pressed
		if(isRight == true)
		{
			c.drawBitmap(rightArrowP, 80, 375, p);
		}else{ c.drawBitmap(rightArrowN, 80, 375, p);}
		
		//if up is pressed
		if(isUp == true)
		{
			c.drawBitmap(upArrowP, 40, 340, p);
		}else{ c.drawBitmap(upArrowN, 40, 340, p);}
		
		//if down is pressed
		if(isDown == true)
		{
			c.drawBitmap(downArrowP, 40, 375, p);
		}else{ c.drawBitmap(downArrowN, 40, 375, p);}
		
		//if fire button is pressed
		if(isFiring == true)
		{
			if(valS == true){sp.play(soundLaser, 1, 1, 1, 0, 1);}
			c.drawBitmap(laserP, 250, 345, p);
		}else{ c.drawBitmap(laserN, 250, 345, p); sp.pause(soundLaser);}
	}
	

	
	//function for loading cells view xml
	public void loadCells(int levelNum)
	{
		
		try {  
            XMLPullParserHandler parser = new XMLPullParserHandler(); 
            InputStream is = null;
            is=contextChange.getAssets().open("levelone.xml"); 
            
            switch(levelNum){
            case 1:
            	is=contextChange.getAssets().open("levelone.xml");  
            	cellCounter = 4;
            	break;
            case 2:
            	is=contextChange.getAssets().open("leveltwo.xml");
            	cellCounter = 3;
            	break;
            case 3:
            	is=contextChange.getAssets().open("levelthree.xml");
            	cellCounter = 4;
            	break;
            case 4:
            	is=contextChange.getAssets().open("levelfour.xml");
            	cellCounter = 4;
            	break;
			case 5:
				is=contextChange.getAssets().open("levelfive.xml");
				cellCounter = 7;
				break;

            }
            
            cells = parser.parse(is);  
            
            for (int i = 0; i < cells.size(); i++){
            	Log.d("Cell", String.valueOf(cells.get(i).xPos));
            }
            
              
        } catch (IOException e) {e.printStackTrace();}  
          
    
	
	}
	
	//checks if petri dish health is below 0 and changes activity to a loss splash screen
	public void checkLoss()
	{
	
		if(petriDish.getHealth() <= 0)
		{
			int currentScore = score.getScoreInt();
			currentScore = currentScore + petriDish.getHealth();
			Intent intent = new Intent(contextChange, FailSplash.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    intent.putExtra("valMusic", valM);
		    intent.putExtra("valSound", valS);
			intent.putExtra("score", currentScore);
			intent.putExtra("level", currentLevel);
			contextChange.startActivity(intent);

		}
	}
	
	//checks if all cells are destroyed and take used to win splash screen
	public void checkWin()
	{
		if(cellCounter == 0)
		{
			int currentScore = score.getScoreInt();
			currentScore = currentScore + petriDish.getHealth();
			Intent intent = new Intent(contextChange, CompleteSplash.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    intent.putExtra("valMusic", valM);
		    intent.putExtra("valSound", valS);
			intent.putExtra("score", currentScore);
			intent.putExtra("level", currentLevel);
			contextChange.startActivity(intent);
			
		}
	}

	public void toggleOptions(int xPos, int yPos)
	{
		

			if(xPos > 5 && xPos < 35 && yPos >63 && yPos < 85)
			{
				if(valM != true){valM = true;}else{valM = false;}
				
				Log.d("music: ", String.valueOf(valM));
			}
			if(xPos > 38 && xPos < 75 && yPos >63 && yPos < 85)
			{
				if(valS != true){valS = true;}else{valS = false;}
				Log.d("sound", String.valueOf(valS));
				
			}
		
	}
}