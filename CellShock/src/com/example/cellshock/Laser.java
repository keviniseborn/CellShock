//class for the laser
package com.example.cellshock;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

public class Laser {
	
	public int xPos;				//x position of laser
	public int yPos;				//y position of laser
	public boolean firing;			//boolean if the laser is currently active
	private int colour;				//colour variable
	private int spriteHeight;		//height of sprite
	private int spriteWidth;		//width of sprite
	public int battery;				//int variable for battery left
	
	//bitmaps for the laser and battery levels
	private Bitmap laserBMP;
	private Bitmap batteryF, batteryM, batteryE, batteryD;
	//sprite for the laser
	private Sprite laserSprite;
	
	//rectangles for the crosshair 
	private Rect leftAim = new Rect();
	private Rect rightAim = new Rect();
	private Rect topAim = new Rect();
	private Rect bottomAim = new Rect();
	
	public Laser (Context context, int xPosIn, int yPosIn){

		//set battery to full
		this.battery = 100;																				
		
		//assign bitmaps and sprite sheet to relevent objects
		laserBMP = BitmapFactory.decodeResource(context.getResources(), R.drawable.lasersprite);
		batteryF = BitmapFactory.decodeResource(context.getResources(), R.drawable.battery3);
		batteryM = BitmapFactory.decodeResource(context.getResources(), R.drawable.battery2);
		batteryE = BitmapFactory.decodeResource(context.getResources(), R.drawable.battery1);
		batteryD = BitmapFactory.decodeResource(context.getResources(), R.drawable.battery0);
		//set width and height of sprite based on bmp
		spriteHeight = laserBMP.getHeight();
		spriteWidth = laserBMP.getWidth() / 4;
		
		this.xPos = xPosIn;
		this.yPos = yPosIn;
		
		//create the rectangles based on lasers x and y pos
		leftAim = new Rect(xPos - 35, yPos-2, xPos - 25, yPos + 2);
		rightAim = new Rect(xPos + 25, yPos-2, xPos + 35, yPos + 2);
		
		topAim = new Rect(xPos - 2, yPos-25, xPos +2, yPos - 35);
		bottomAim = new Rect(xPos - 2, yPos+25, xPos +2, yPos + 35);
		
		//create laser sprite with required variables
		laserSprite = new Sprite(laserBMP, xPos - (spriteWidth / 2), yPos - (spriteHeight / 2), 4, 1);
		
	}
	
	public Laser (){
		xPos = 0;
		yPos = 0;
		colour = Color.GREEN;
	}
	
	//Method to draw a rectangle on the screen
	public void drawLaser(Paint p, Canvas c){
		p.setStrokeWidth(3);
		p.setColor(Color.BLACK);
		
		//if the laser is active
		if(firing == true)
		{
			if(this.battery > 0)		//if the battery is above 0
			{
				this.battery -= 5;		//drain battery
				laserSprite.draw(c);	//draw the laser sprite
			}
		}else{
			if(this.battery < 100)		//if battery isnt firing and is under 100
			{
				this.battery += 1;		//recharge battery
			}
		c.drawCircle((float)xPos, (float)yPos, 2, p); //draw circle at aim point
		}
		
		//depending on battery level draw different battery image to screen
		if(this.battery > 60)c.drawBitmap(batteryF, 250 , 5, p);
		if(this.battery < 60 && this.battery > 30)c.drawBitmap(batteryM, 250 , 5, p);
		if(this.battery < 30 && this.battery > 0)c.drawBitmap(batteryE, 250 , 5, p);
		if(this.battery < 1)c.drawBitmap(batteryD, 250 , 5, p);
		
		//draw crosshair
		c.drawRect(leftAim, p);
		c.drawRect(rightAim, p);
		c.drawRect(topAim, p);
		c.drawRect(bottomAim, p);

		//write battery level to screen
		c.drawText(Integer.toString(this.battery), 245, 50, p);

	}
	
	public void update(){
		
		//update positions of the crosshair
		leftAim.set(xPos - 35, yPos-2, xPos - 25, yPos + 2);
		rightAim.set(xPos + 25, yPos-2, xPos + 35, yPos + 2);
		topAim.set(xPos - 2, yPos-25, xPos +2, yPos - 35);
		bottomAim.set(xPos - 2, yPos+25, xPos +2, yPos + 35);
		
		
		//update position of laser
		laserSprite.setX(xPos - (spriteWidth/2));
		laserSprite.setY(yPos - (spriteHeight/2));
	}
	
	
	//move laser left
	public void moveLeft()
	{
		this.xPos -= 2;
	}
	
	//move laser right
	public void moveRight()
	{
		this.xPos += 2;
	}
	
	//move laser down
	public void moveDown()
	{
		this.yPos += 2;
	}
	
	//move laser up
	public void moveUp()
	{
		this.yPos -= 2;
	}
	
	public int getColour(){
		return this.colour;
	}
	
	//checks if laser is colliding with a cell
	public boolean hitCell(Cell c)
	{
		int xMax = this.xPos + this.spriteWidth;
		int yMax = this.xPos + this.spriteHeight;
		int xMin = this.xPos - this.spriteWidth;
		int yMin = this.yPos - this.spriteHeight;
		boolean tempBool;
		tempBool = true;
		if(xMax < c.xPos - c.radius || xMin > c.xPos + c.radius ){tempBool = false;}
		if(yMax < c.yPos - c.radius || yMin > c.yPos + c.radius ){tempBool = false;}
		if(tempBool == true){ /*c.xPos = -200; c.yPos = -200; c.xVel = 0; c.yVel = 0;*/return true;}else{return false;}
		
	}
	
	public boolean hasBattery()
	{
		if(this.battery > 0)
		{
			return true;
		}else{ return false;}
	}
}