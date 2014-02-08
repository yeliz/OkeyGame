/**************************************************************************
 * Main game class
 * 1- User has 15 racks
 * 2- User can drag/drop racks in order to re-place them.
 * 3- User can make series
 * 4- User can regenerate the racks
 * 5- User can make pairs
 * 6- User can insert a rack between two racks but he/she should place the
 * rack exactly the middle of two racks
 * 7- User can change place of same colored rack series
 * @author yy
 *
 *************************************************************************/
package com.me.mygdxgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.me.mygdxgame.screens.GameScreen;

public class MyGdxGame extends Game{
		
	public Stage stage;
	public boolean debugMode = false;
	public GameScreen gameScreen;
	
	@Override
	public void create() {	
	
		// load resources
		Assets.loadResources();
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		
		// create game screen
		gameScreen = new GameScreen(this);
		setScreen(gameScreen);
		
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void dispose() {
		super.dispose();
		
		stage.dispose();
		
		getScreen().dispose();
		
		Assets.dispose();
	}

	@Override
	public void render() {	
		super.render();
		
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		Table.drawDebug(stage);

	}
	/*********************************************************************************************
	 * Necessary getters/setters of the game
	 * Get and set board height and width, rack height and width
	 * @author yy
	 ********************************************************************************************/
	public float getBoardHeight(){return gameScreen.board.getHeight();}

	public float getBoardWidth(){return gameScreen.board.getWidth();}
	
	public float getRackHeight(){return gameScreen.getRackHeight();}
	
	public float getRackWidth(){return gameScreen.getRackWidth();}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, false);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
