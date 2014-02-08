/**************************************************************************
 * DrawableObject is an actor that can be added to the stage
 * It has a texture and x and y coordinates
 *
 *************************************************************************/
package com.me.mygdxgame.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class DrawableObject extends Actor{
	
	protected TextureRegion region;
	
	public DrawableObject(TextureRegion region, int x, int y){
	
		this.region = region;
		setWidth(this.region.getRegionWidth());
		setHeight(this.region.getRegionHeight());
		super.setTouchable(Touchable.enabled);
	
		this.setX(x);
		this.setY(y);
	}

	public void draw(SpriteBatch batcher, float parentAlpha){
		batcher.draw(this.region, this.getX(), this.getY());
	}

}
