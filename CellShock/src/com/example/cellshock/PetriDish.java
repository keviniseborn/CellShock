//class for the petri dish
package com.example.cellshock;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.R.string;

public class PetriDish{
	
	public float radius;			//petri dish radius
	public float xPos, yPos;		//petri dish x and y pos
	private int colour;				//petri dish colour
	private int health;				//petri dish health value
	private String stringHealth;	//string for the health to display to screen

	//constructor for petridish
	public PetriDish (float radiusIn, float xPosIn, float yPosIn,  int c, int healthIn){
		this.radius = radiusIn;
		this.xPos = xPosIn;
		this.yPos = yPosIn;
		this.xPos = this.xPos - this.radius;
    	this.yPos = this.yPos - this.radius;
    	this.health = healthIn;
		this.colour = c;

	}
	
	//constructor
	public PetriDish (){
		this.radius = 0;
		this.xPos = 0;
		this.yPos = 0;
		this.colour = Color.argb(50,50,50,10);
	}
	
	//Method to draw a circle on screen
	public void drawPetriDish(Paint p, Canvas c){
		p.setStrokeWidth(3);
	    p.setColor(colour);
		c.drawCircle((float)xPos, (float)yPos, radius, p);
	}
	

	
	
	//Get the x position of the ball
	public float getxPos (){
		return this.xPos;
	}
	
	//Get the y position of the ball
	public float getyPos (){
			return this.yPos;
	}
	
	//Get the colour of the ball as an int 
	public int getColour (){
		return this.colour;
	}
	
	public int getHealth(){
		return this.health;
	}
	
	public void setHealth(int h){
		this.health = h;
	}
	
	public void deductHealth(int i){
		this.health = this.health - i;
	}	
	
	public void addHealth(){
		this.health = this.health + 2;
	}	
	
	public String getHealthString(){
		
		return this.stringHealth = Integer.toString(health);
		}
	
	//checks collisions between petridish and cell
	public boolean petriCollision(Cell cell){

		float cX1, cY1;
		float cX2, cY2;
		
		cX1 =  this.xPos;
		cY1 =  this.yPos;
		cX2 =  cell.xPos;
		cY2 =  cell.yPos;
		
		float dist = 0;
		
		//calculates distance based on petri dish and cells x and y positions.
		if(cX1 <= cX2 && cY1 <= cY2)
		{
			dist = (float) Math.sqrt(((cX2 - cX1) * (cX2 - cX1)) + ((cY2 - cY1) * (cY2 - cY1)));
		}
		
		if(cX1 >= cX2 && cY1 <= cY2)
		{
			dist = (float) Math.sqrt(((cX1 - cX2) * (cX1 - cX2)) + ((cY2 - cY1) * (cY2 - cY1)));
		}
		
		if(cX1 <= cX2 && cY1 >= cY2)
		{
			dist = (float) Math.sqrt(((cX2 - cX1) * (cX2 - cX1)) + ((cY1 - cY2) * (cY1 - cY2)));
		}
		
		if(cX1 >= cX2 && cY1 >= cY2)
		{
			dist = (float) Math.sqrt(((cX1 - cX2) * (cX1 - cX2)) + ((cY1 - cY2) * (cY1 - cY2)));
		}
								
		float radiusSum = (this.radius);
		//if(dist == 0){dist = 2000;}

		//if distance is greater than combined radius, collision has happened
		if(dist >= radiusSum)
		{
			cell.xVel = -cell.xVel;
			cell.yVel = -cell.yVel;
			
			cell.xPos = cell.xPos + (cell.xVel *5);
			cell.yPos = cell.yPos + (cell.yVel *5);
			return true;
		}else{ 
		
			return false;}
		
	}
}
