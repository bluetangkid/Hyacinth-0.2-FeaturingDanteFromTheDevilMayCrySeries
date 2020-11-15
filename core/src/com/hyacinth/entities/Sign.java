package com.hyacinth.entities;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Sign extends StaticEntity{
    String text;
    BitmapFont font;
    SpriteBatch batch;
    GlyphLayout layout;
    public Sign(World w, float x, float y, float width, float height, String text, BitmapFont font){
        super(w, x, y, width, height);
        this.text = text;
        this.font = font;
        batch = new SpriteBatch();
        layout = new GlyphLayout(font, text);
    }

    @Override
    public void update() {}

    public void displayText(){
        batch.begin();
        font.draw(batch, text, 1280/(1.2f*2), 500);
        batch.end();
    }
}
