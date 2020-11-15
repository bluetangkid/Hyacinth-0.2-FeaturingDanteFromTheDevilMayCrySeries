package com.hyacinth.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Button {
    SpriteBatch batch;
    Vector2 pos;
    Texture tex;
    String text;
    BitmapFont font;
    float textLen;

    public Button(Texture tex, String text, BitmapFont font, Vector2 pos){
        this.tex = tex;
        this.text = text;
        this.font = font;
        this.pos = pos;
        GlyphLayout layout = new GlyphLayout(font, text);
        textLen = layout.width;
        batch = new SpriteBatch();
    }

    public boolean doHe(){
        batch.begin();
        batch.draw(tex, pos.x - tex.getWidth()*2, pos.y + tex.getHeight()*2, tex.getWidth() * 4, tex.getHeight() * 4);
        font.draw(batch, text,pos.x - textLen/2, pos.y + tex.getHeight()*5);
        Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.graphics.getDisplayMode().height - Gdx.input.getY());
        batch.end();
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && mousePos.x - (pos.x - tex.getWidth()*2) > 0 && mousePos.x - (pos.x - tex.getWidth()*2) < tex.getWidth()*4 && mousePos.y - (pos.y + tex.getHeight()*2) > 0 && mousePos.y - (pos.y + tex.getHeight()*2) < tex.getHeight()*4){
            System.out.println(text);
            return true;
        }
        return false;
    }
}
