/**************************************************************************
 * OkeyInputListener is an input listener in order to make racks draggable.
 *************************************************************************/

package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.me.mygdxgame.objects.Rack;

public class OkeyInputListener extends InputListener{
	private GameLogic logic;
	private boolean seriesSelected = false;
	
	public OkeyInputListener(GameLogic logic){
		super();
		this.logic = logic;
		
	}
	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
		Rack actor = (Rack)event.getTarget(); 
		//System.out.println("touch down "+actor.number);
        //System.out.println("touch down "+" actor:"+ x+" "+y);
        // Check if it is selected as series or not
        // check if it is right up or left up selected
       
        if(y > logic.game.getRackHeight() - 30){
        	if(x < 20 || x > logic.game.getRackWidth()-15){
        		seriesSelected = logic.haveSeries(actor.okIndex);
        		//System.out.println("SERIES "+seriesSelected);
        	}
        }
        
        return true;
    } 
    
    public void touchDragged(InputEvent event, float x, float y,int pointer) {
    	Rack actor = (Rack)event.getTarget();
    	
    	if(!seriesSelected){
    		// While moving rack on the stage, drag method
        	logic.move(actor.okIndex, Gdx.input.getX()-20,  Gdx.graphics.getHeight() - Gdx.input.getY()-20);
    	}else{
    		logic.moveSeries(Gdx.input.getX()-20,  Gdx.graphics.getHeight() - Gdx.input.getY()-20);
    	}
    	
    	//System.out.println("touch dragged  "+Gdx.input.getX()+" "+Gdx.input.getY() +" index ");	            
    	super.touchDown(event, x, y, pointer, pointer);
    }
    
	public void touchUp(InputEvent event, float x, float y,int pointer, int button) {
		Assets.playSound(Assets.rackSound);
		Rack actor = (Rack)event.getTarget();
		// While put the rack on stage.
		if(!seriesSelected)
			logic.place(actor.okIndex, Gdx.input.getX(), Gdx.input.getY());
		else{
			logic.placeSeries(Gdx.input.getX(), Gdx.input.getY());
		}
		//System.out.println("touch up"+" "+x+" "+y );	
		super.touchUp(event, x, y, pointer, button);
		
		seriesSelected = false;
	}

}
