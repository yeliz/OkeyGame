/**************************************************************************
 * Rack is a drawable object that is the minor draggable component of the game
 * - column/row: used for positioning the rack on the board
 * - number/color: every rack has a number and color
 * - old position: is used for making racks in order to attach racks on the board.
 * - position: is the x and y coordinates on the stage
 * - selected: is used for selecting different rack while getting 15 random
 * racks from the rack pack
 *************************************************************************/
package com.me.mygdxgame.objects;
import java.util.Iterator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.me.mygdxgame.Assets;
import com.me.mygdxgame.GameLogic;
import com.me.mygdxgame.OkeyInputListener;
import com.me.mygdxgame.Position;

public class Rack extends DrawableObject implements Comparable<Rack>{
	private int row;
	private int column;
	
	public int number;
	public int okIndex = 0;
	public boolean selected = false;
	public Color color;
	public boolean moving = false;
	public Position oldPosition;
	
	public Position position;
		
	public Rack(GameLogic logic, Color color, int number, boolean debugable){
		super(Assets.rack, 0, 0);

		this.color = color;
		this.number = number;
		
		setWidth(this.region.getRegionWidth());
		setHeight(this.region.getRegionHeight());
		
		//setting bound of the actor
		setBounds(0, 0, getWidth(), getHeight());
		setTouchable(Touchable.enabled);
		
		oldPosition = new Position(0, 0);
		position = new Position(0, 0);
		Assets.font.setColor(color);
		
		addListener(new OkeyInputListener(logic));
	}
	
	// Get methods
	public int getRegionHeight(){return this.region.getRegionHeight();}
	public int getRegionWidth(){return this.region.getRegionWidth();}
	public int getRow(){return this.row;}
	public int getColumn(){return this.column;}
	public Position getPosition(){return position;}
	
	// Set methods
	public void setColumn(int column){
		this.column = column;
		// automatically setting x coordinate and y coordinate of rack
		setPosition((25 * (column+1) + 47*column), this.region.getRegionHeight() * (1 - row));
	}
	
	public void setPosition(float x, float y){
		position.x = x;
		position.y = y;
		setBounds(position.x, position.y, getWidth(), getHeight());
	}
	
	public void setRow(int row){
		this.row = row;
		setPosition((25 * (column+1) + 47*column), this.region.getRegionHeight() * (1 - row));
	}
		
	/*********************************************************************************************
	 * As Rack are comparable with their colors & number, this method is used for sorting Rack 
	 * arrays or array collections.
	 * @author yy
	 ********************************************************************************************/
	public int compareTo(Rack compareObj) {
		int compareNumber = compareObj.number + compareObj.color.toIntBits();
		return this.number  + color.toIntBits() - compareNumber; 
	}
	
	/*********************************************************************************************
	 * Equals method compare two racks by their color and number.
	 * returns true if they are equal, otherwise false.
	 * @author yy
	 ********************************************************************************************/
	public boolean equals(Rack obj){
		return (obj.color == this.color) && (obj.number == this.number);
	}
	
	public void draw(SpriteBatch batcher, float parentAlpha){
		
		batcher.draw(this.region, (float)position.x, (float)position.y);
		
		// Draw Font
		Assets.font.setColor(color);
		
		// For Fake OK rack
		if(number > 13){
			Assets.font.draw(batcher, "*", position.x + this.getWidth()/2-10, 
					position.y + this.getHeight()/2 + 20);
		}
		else if(number > 9){ // For 2-digit numbered racks
			Assets.font.draw(batcher, ""+number, position.x + this.getWidth()/2-18, 
					position.y + this.getHeight()/2 + 30);
		}else{
			Assets.font.draw(batcher, ""+number, position.x + this.getWidth()/2-10, 
					position.y + this.getHeight()/2 + 30);
		}
	}
	
	public void act(float delta){
		for(Iterator<Action> iter = this.getActions().iterator(); iter.hasNext();)
			iter.next().act(delta);
	}
	
	public String toString(){
		return "" + this.number;
	}
}
