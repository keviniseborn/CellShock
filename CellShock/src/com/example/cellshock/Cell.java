package com.example.cellshock;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.lang.Math;


public class Cell{
	public enum cellType{RED, GREEN, BLUE, WHITE};
	public int collisionZone;
	public cellType type;
	public boolean active;
	public int radius;
	public int mass;
	public int xPos, yPos;
	public int xVel, yVel;
	public int points;
	private int colour;
	private int screenWidth;
	private int screenHeight;
	
	
	public Cell (int radiusIn, int xPosIn, int yPosIn, int xVelocity, int yVelocity, int c, int m){
		this.radius = radiusIn;
		this.xPos = xPosIn;
		this.yPos = yPosIn;
		this.xPos = this.xPos - this.radius;
    	this.yPos = this.yPos - this.radius;
		
		this.xVel = xVelocity;
		this.yVel = yVelocity;
		this.colour = c;
		this.mass = m;
		active = true;
	}
	
	public Cell (){
		this.radius = 0;
		this.xPos = 0;
		this.yPos = 0;
		this.mass = 0;
		this.xVel = 0;
		this.yVel = 0;
		this.colour = Color.GREEN;
		this.active = true;
	}
	
	//Method to draw a circle on screen
	public void drawCell(Paint p, Canvas c){
		
		if(this.active == true)
		{
			p.setStrokeWidth(3);
	    	p.setColor(colour);
	    	c.drawCircle((float)xPos, (float)yPos, radius, p);
		}
	}
	
	// Detect collision and update the position of the cell.
    public void update() {
    	//Set the speed of the cell
    	this.xPos = this.xPos + this.xVel;
    	this.yPos = this.yPos + this.yVel;
    	
    	//limit speed function
    	this.limitSpeed();
    	
    	//detect collision zone for individual cell
    	this.checkZone();
    	

    }
    //set type of cell and attributes (type is specified in xml)
	public void setType(int type)
	{
		switch (type) {
        case 1:
        	this.type = cellType.RED;
        	this.colour = Color.RED;
        	this.points = 10;
        	this.radius = 6;
        	this.mass = 1;
            break;
                
        case 2:
        	this.type = cellType.GREEN;
        	this.colour = Color.GREEN;
        	this.points = 20;
        	this.radius = 6;
        	this.mass = 1;
            break;
                     
        case 3: 
        	this.type = cellType.BLUE;
        	this.colour = Color.BLUE;
        	this.points = 10;
        	this.radius = 6;
        	this.mass = 1;
        	break;
        case 4:
        	this.type = cellType.WHITE;
        	this.colour = Color.WHITE;
        	this.points = 0;
        	this.radius = 9;
        	this.mass = 1;
            break;
                    
        default:
            
            break;
    }
	}
    
    public cellType getType()
    {
    	return this.type;
    }
	
	//Set the colour of the cell
	public void setColour (int c){
		this.colour = c;
	}
	
	
	//Set the screen width
	public void setScreenWidth(int w){
		this.screenWidth = w;
	}
	
	//Set the screen height
	public void setScreenHeight(int h){
		this.screenHeight = h;
	}
	
	
	//check for collision between this cell and other cell
	public boolean circleCircleCollision(Cell cellOther){

		
			float cX1, cY1;
			float cX2, cY2;
		
		//assign position of cells to variables for ease of use
		cX1 =  this.xPos;
		cY1 =  this.yPos;
		cX2 =  cellOther.xPos;
		cY2 =  cellOther.yPos;
		
		//distance variable (between cells)
		int dist;
		dist = 2000;
		
		//if this cells position is less than other cell in both x and y
		if(cX1 <= cX2 && cY1 <= cY2)
		{
			dist = (int) Math.sqrt(((cX2 - cX1) * (cX2 - cX1)) + ((cY2 - cY1) * (cY2 - cY1)));
			
		}else 
		
		
		//if this cells position is greater than other cells x but less in y
		if(cX1 >= cX2 && cY1 <= cY2)
		{
			dist = (int) Math.sqrt(((cX1 - cX2) * (cX1 - cX2)) + ((cY2 - cY1) * (cY2 - cY1)));
			
		}else
		
		//if this cells position is greater than other cells y but less in x
		if(cX1 <= cX2 && cY1 >= cY2)
		{
			dist = (int) Math.sqrt(((cX2 - cX1) * (cX2 - cX1)) + ((cY1 - cY2) * (cY1 - cY2)));
			
		}else
		
		//if other cells position is less than this cell in both x and y
		if(cX1 >= cX2 && cY1 >= cY2)
		{
			dist = (int) Math.sqrt(((cX1 - cX2) * (cX1 - cX2)) + ((cY1 - cY2) * (cY1 - cY2)));
			
		}
		
		if(dist == 0){dist = 2000;}
		
		//radius of both cells combined
		float radiusSum = (this.radius + cellOther.radius);
		
		//if distance is less than combined radius, collision has happened
		if(dist <= radiusSum)
		{
			//resolve function for calculating new velocities of cells after collision
			//move cell slightly from collision point
			this.xPos = this.xPos - (this.xVel);
			this.yPos = this.yPos - (this.yVel);
			
			this.resolve(cellOther);
			return true;
		}else{ 
			
			return false;}

	
	}
	
	
	//calculate new velocities dependant on cells mass and current velocity
	public void resolve(Cell cellOther){
		
		//calculate new velocities
		int newVelX1 = (this.xVel * (this.mass - cellOther.mass) + (2 * cellOther.mass * cellOther.xVel)) / (this.mass + cellOther.mass);
		int newVelY1 = (this.yVel * (this.mass - cellOther.mass) + (2 * cellOther.mass * cellOther.yVel)) / (this.mass + cellOther.mass);
		int newVelX2 = (cellOther.xVel * (cellOther.mass - this.mass) + (2 * this.mass * this.xVel)) / (this.mass + cellOther.mass);
		int newVelY2 = (cellOther.yVel * (cellOther.mass - this.mass) + (2 * this.mass * this.yVel)) / (this.mass + cellOther.mass);
		
		//set new velocities
		this.xVel =  newVelX1;
		this.yVel = newVelY1;
		cellOther.xVel =  newVelX2;
		cellOther.yVel = newVelY2;
		
		//move cell slightly to avoid constant collision
		this.xPos = this.xPos + (newVelX1);
		this.yPos = this.yPos + (newVelY1);
		cellOther.xPos = cellOther.xPos + (newVelX2);
		cellOther.yPos = cellOther.yPos + (newVelY2);
		
	}
	
	//checks what collision zone the cell is in
	public void checkZone()
	{
		if(this.xPos >10 && this.xPos < 96)
		{
			if(this.yPos > 80 && this.yPos < 184)
			{
				this.collisionZone = 1;
			}
			if(this.yPos > 142 && this.yPos < 288)
			{
				this.collisionZone = 2;
			}
			if(this.yPos > 204 && this.yPos < 392)
			{
				this.collisionZone = 3;
			}
		
		}
		
		if(this.xPos >96 && this.xPos < 192)
		{
			if(this.yPos > 80 && this.yPos < 184)
			{
				this.collisionZone = 4;
			}
			if(this.yPos > 142 && this.yPos < 288)
			{
				this.collisionZone = 5;
			}
			if(this.yPos > 204 && this.yPos < 392)
			{
				this.collisionZone = 6;
			}
		}
		
		if(this.xPos >192 && this.xPos < 300)
		{
			if(this.yPos > 80 && this.yPos < 184)
			{
				this.collisionZone = 7;
			}
			if(this.yPos > 142 && this.yPos < 288)
			{
				this.collisionZone = 8;
			}
			if(this.yPos > 204 && this.yPos < 392)
			{
				this.collisionZone = 9;
			}
		
		}
		
	}
	
	public void limitSpeed()
	{
		if(this.type !=  this.type.GREEN)
		{
			if(this.xVel >= 1) this.xVel = 1;
			if(this.xVel <= -1) this.xVel = -1;
			if(this.yVel >= 1) this.yVel = 1;
			if(this.yVel <= -1) this.yVel = -1;
		}else{
			if(this.xVel >= 2) this.xVel = 2;
			if(this.xVel <= -2) this.xVel = -2;
			if(this.yVel >= 2) this.yVel = 2;
			if(this.yVel <= -2) this.yVel = -2;
		}
	}
	
	public void setActive(int i)
	{
		if(i == 0){ this.active = false; }
		if(i == 1){this.active = true; }
	}
	
	
	//function for mutating blue cells
	public boolean mutateCell(Cell cellOther)
	{
		//if both cells are of type BLUE
		if(this.type == this.type.BLUE && cellOther.type == cellOther.type.BLUE)
		{
			
				//get radius of other cell and multiply by 2
				int cellROne = (int) (this.radius * 2);
			
					
					//interpolate radius value from original value to cellR
					for(int i = (int) (this.radius); i < cellROne; i++)
					{
						this.radius += 1;
						
					}
				
					cellOther.active = false;
					cellOther.xVel = 0;
					cellOther.yVel = 0;
					cellOther.xPos = - 2000;
					cellOther.yPos = - 2000;
					
					this.points = this.points * 2;
					return true;	
		}
		return false;
	}
}
