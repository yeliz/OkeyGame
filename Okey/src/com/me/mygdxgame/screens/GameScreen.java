/**************************************************************************
 * GameScreen is a screen of the game which consists UI elements, logic and
 * stage of the game.
 * - Creates UI components
 * - Create logic of the game
 * - Create stage and stage nodes (Racks and board)
 *************************************************************************/
package com.me.mygdxgame.screens;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.me.mygdxgame.Assets;
import com.me.mygdxgame.Constants;
import com.me.mygdxgame.GameLogic;
import com.me.mygdxgame.MyGdxGame;
import com.me.mygdxgame.objects.DrawableObject;
import com.me.mygdxgame.objects.Rack;


public class GameScreen implements Screen{
	private MyGdxGame game;

	private int width;
	private int height;
	
	public DrawableObject board;
	public GameLogic logic;

	private Table table;

	public GameScreen (MyGdxGame game) {
		this.game = game;

		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();

		board = new DrawableObject(Assets.board, 2, -2);
		board.setOrigin(Constants.PADDING, Constants.PADDING);
		logic = new GameLogic(game);
		
		game.stage.addActor(board);
		
		Iterator<Rack> it = logic.racks.iterator();
		while(it.hasNext())
		{
			Rack obj = it.next();
			game.stage.addActor(obj);
		}
		
		createUI();
						
	}
	
	private void createUI(){
		
		table = new Table( Assets.getSkin());
		table.setPosition(-width/2+180, 150);
		table.setFillParent( true );
		table.setWidth(200);
		 
        game.stage.addActor( table );
        
        table.add( "Select Menu" ).spaceBottom( 10 );
        table.row();

        // register the button "Restart game"
        TextButton restartButton = new TextButton( "Restart", Assets.getSkin() );
        restartButton.addListener(new ChangeListener() {
    		public void changed (ChangeEvent event, Actor actor) {
    			Assets.playSound(Assets.clickSound);
    			
    			logic.resetRack();
    		}
    	});
        table.add( restartButton ).size( 200, 60 ).uniform().spaceBottom( 10 );
        table.row();
        
        // register the button "Make series"
        TextButton seriesButton = new TextButton( "Series", Assets.getSkin() );
        seriesButton.addListener(new ChangeListener() {
    		public void changed (ChangeEvent event, Actor actor) {
    			Assets.playSound(Assets.clickSound);
    			
    			logic.makeSeries();
    		}
    	});
        table.add( seriesButton ).size( 200, 60 ).uniform().spaceBottom( 10);
        table.row();
        
     // register the button "Make pairs"
        TextButton pairButton = new TextButton( "Pair", Assets.getSkin() );
        pairButton.addListener(new ChangeListener() {
    		public void changed (ChangeEvent event, Actor actor) {
    			Assets.playSound(Assets.clickSound);
    			
    			logic.makePairs();
    		}
    	});
        table.add( pairButton ).size( 200, 60 ).uniform().spaceBottom( 10 );
        table.row();
                
	}
	
	// get methods
	public float getRackWidth(){return logic.racks.get(0).getRegionWidth();}
	public float getRackHeight(){return logic.racks.get(0).getRegionHeight();}
		
	public void render (float delta) {	
	
	}
	
	@Override
	public void resize (int width, int height) {
	
	}
	
	@Override
	public void show () {
	}
	
	@Override
	public void hide () {
	}
	
	@Override
	public void pause () {
	
	}
	
	@Override
	public void resume () {
	}
	
	@Override
	public void dispose () {
		logic.dispose();	
	}

}
