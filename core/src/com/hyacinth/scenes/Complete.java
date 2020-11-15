package com.hyacinth.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Complete {
    GlyphLayout layout = new GlyphLayout();

    public void render(PlayingLevel level, BitmapFont font, SpriteBatch batch, int levelId){
        double elapsedS = round(level.getElapsedTime()/1000f, 2);
        layout.setText(font, "You took " + elapsedS + "s to complete level " + levelId + 1 + ".");
        batch.begin();
        font.draw(batch, "You took " + elapsedS + "s to complete level " + levelId + 1 + ".", Gdx.graphics.getWidth()/2 - layout.width/2, 700);
        layout.setText(font, "Click to continue to Level Select");
        font.draw(batch, "Click to continue to Level Select", Gdx.graphics.getWidth()/2 - layout.width/2, 670);
        batch.end();
    }

    public static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
}
