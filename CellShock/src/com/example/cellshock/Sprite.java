package com.example.cellshock;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite {
	
	private int x, y;								//x and y pos of sprite
	private int spriteHeight, spriteWidth;			//sprite widht and height
	private Bitmap b;								//sprite bitmap image
	private int rowCounter = 0;						//row counter for cycling through sprite sheet
	private int columnCounter = 0;					//column counter for cycling through sprite sheet
	private Rect sourceRect;						//rectangle object
	private Rect dst;								//rectangle object
	private int row;								//row variable for number of rows in sprite sheet
	private int column;								//column variable for number of columns in sprite sheet
	
	//sprite constructor
	public Sprite (Bitmap b, int x, int y, int c, int r){
		this.b = b;
		this.row = r;
		this.column = c;
		this.spriteHeight = b.getHeight() / this.row;  //Get the height of individual sprites
		this.spriteWidth = b.getWidth() / this.column; //Get the width of individual sprites
		this.x = x - (this.spriteWidth / 2); //Position of sprite
		this.y = y - (this.spriteHeight / 2);

		sourceRect = new Rect (0,0, spriteWidth, spriteHeight);
	}
	
	//update function for sprite
	public void update(){
		
		//if counter is greater than number of columns, set counter back to start
		if (columnCounter >= this.column){
			columnCounter = 0;
		}
		
		//if counter is greater than number of rows, set counter back to start
		if (rowCounter >= this.row){
			rowCounter = 0;
		}
		
		//Position of rectangle on spritesheet
		this.sourceRect.left = columnCounter * spriteWidth;
		this.sourceRect.right = this.sourceRect.left + spriteWidth;
		this.sourceRect.top =  rowCounter * spriteHeight;
		this.sourceRect.bottom = this.sourceRect.top + spriteHeight ;
			
		//Counters to iterate over spritesheet
		columnCounter++;
		rowCounter++;
	}
	
	@SuppressLint("DrawAllocation")
	public void draw(Canvas canvas){
		update();
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//Rectangle that defines where sprite appears on canvas
		dst = new Rect(this.x,this.y,this.x+spriteWidth, this.y+ spriteHeight); //Scales the picture
		
		//Output of bitmap
		canvas.drawBitmap(b, sourceRect, dst, null);
	}
	
	//Get rectangle position of sprite on canvas
	public Rect getDest(){
		return this.dst;
	}
	
	//set x position
	public void setX(int x){
		this.x = x;
	}
	
	//set y position
		public void setY(int y){
			this.y = y;
		}

}