package com.example.cellshock;

import java.io.IOException;  
import java.io.InputStream;  
import java.util.ArrayList;  
import org.xmlpull.v1.XmlPullParser;  
import org.xmlpull.v1.XmlPullParserException;  
import org.xmlpull.v1.XmlPullParserFactory;  
   
  
public class XMLPullParserHandler {  
    private ArrayList<Cell> Cells= new ArrayList<Cell>();  
    private Cell Cell;  
    private String text;  
   
    public ArrayList<Cell> getCells() {  
        return Cells;  
    }  
   
    
    //xml pull parser for loading levels
    public ArrayList<Cell> parse(InputStream is) {  
           try {  
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();  
            factory.setNamespaceAware(true);  
            XmlPullParser  parser = factory.newPullParser();  
   
            parser.setInput(is, null);  
   
            int eventType = parser.getEventType();  
            while (eventType != XmlPullParser.END_DOCUMENT) {  
                String tagname = parser.getName();  
                switch (eventType) {  
                case XmlPullParser.START_TAG:  
                    if (tagname.equalsIgnoreCase("Cell")) {  
                        // create a new instance of Cell  
                        Cell = new Cell();  
                    }  
                    break;  
   
                case XmlPullParser.TEXT:  
                    text = parser.getText();  
                    break;  
   
                case XmlPullParser.END_TAG:  
                    if (tagname.equalsIgnoreCase("Cell")) {  
                        // add Cell object to list  
                        Cells.add(Cell); 
                        
                    }else if (tagname.equalsIgnoreCase("cellType")) {  
                        Cell.setType(Integer.parseInt(text)); 
                        
                    } else if (tagname.equalsIgnoreCase("cellActive")) {  
                        Cell.setActive(Integer.parseInt(text)); 
                        
                    }  else if (tagname.equalsIgnoreCase("xPos")) {  
                        Cell.xPos = (Integer.parseInt(text));  
                    	
                    } else if (tagname.equalsIgnoreCase("yPos")) {  
                    	Cell.yPos = (Integer.parseInt(text)); 
                    	
                    } else if (tagname.equalsIgnoreCase("xVel")) {  
                        Cell.xVel = (Integer.parseInt(text));  
                    	
                    } else if (tagname.equalsIgnoreCase("yVel")) {  
                    	Cell.yVel = (Integer.parseInt(text)); 
                    	 
                    }   
                    break;  
   
                default:  
                    break;  
                }  
                eventType = parser.next();  
            }  
   
        } catch (XmlPullParserException e) {e.printStackTrace();}   
        catch (IOException e) {e.printStackTrace();}  
   
        return Cells;  
    }  
}  
