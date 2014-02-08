
/**************************************************************************
 * Assets class load necessary textures, texture atlas, sounds of the game
 * 
 * @author yy
 *
 *************************************************************************/

package com.me.mygdxgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {
	
	public static TextureAtlas atlas;
	
	public static TextureRegion rack;
	public static TextureRegion board;
	public static TextureRegion playButton;
	
	public static Sound clickSound;
	public static Sound rackSound;
	public static BitmapFont font;
	public static Skin skin;
	
	/*********************************************************************************************
	 * Assets class load necessary textures, texture atlas, sounds, UI skin, fonts of the game
	 * Return type: void
	 * @author yy
	 ********************************************************************************************/
	public static void loadResources() {
		
		// Texture atlas is created with TexturePacker (http://www.codeandweb.com/texturepacker)
		atlas = new TextureAtlas(Gdx.files.internal("atlas.txt"));
		
		rack = atlas.findRegion("tile-rack"); 
		board = atlas.findRegion("tile-board");
		
		playButton = atlas.findRegion("welcome_playButton");

		clickSound = Gdx.audio.newSound(Gdx.files.internal("click.wav"));
		rackSound = Gdx.audio.newSound(Gdx.files.internal("rack.mp3"));
		
		font = new BitmapFont(Gdx.files.internal("calibri.fnt"));
		
		FileHandle skinFile = Gdx.files.internal( "uiskin.json" );
		skin = new Skin( skinFile );		
	}
	

	/*********************************************************************************************
	 * Returns skin of the UI components
	 * @author yy
	 ********************************************************************************************/
	public static Skin getSkin(){

		return skin;
	}
	
	/*********************************************************************************************
	 * Play specific sound from the loaded resources
	 * @author yy
	 ********************************************************************************************/
	public static void playSound (Sound sound) {
		sound.play(1);
	}
	
	public static void dispose(){
		atlas.dispose();
		clickSound.dispose();
		font.dispose();
		skin.dispose();
	}

}
