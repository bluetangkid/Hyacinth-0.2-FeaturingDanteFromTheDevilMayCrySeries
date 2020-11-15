package com.hyacinth.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class LevelSelect {
    BitmapFont subTitleFont;
    SpriteBatch spriteCranberry;
    GlyphLayout how;
    PlayingLevel[] levels;
    Texture button;

    public LevelSelect(FreeTypeFontGenerator generator, PlayingLevel[] levels) {
        FreeTypeFontGenerator.FreeTypeFontParameter sub = new FreeTypeFontGenerator.FreeTypeFontParameter();
        sub.size = 18;
        sub.kerning = false;
        sub.color = Color.WHITE;
        subTitleFont = generator.generateFont(sub);
        how = new GlyphLayout();
        how.setText(subTitleFont, "Featuring Dante from the Devil May Cry series");
        spriteCranberry = new SpriteBatch();
        button = new Texture(Gdx.files.internal("data/textures/button.png"));
        this.levels = levels;
    }

    public int draw(Camera c) {
        c.position.x = 0;
        c.position.y = 0;
        c.position.z = 0;
        spriteCranberry.setProjectionMatrix(c.combined);
        spriteCranberry.begin();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 6; j++){
                spriteCranberry.draw(button, j*120 - 5*60 - 32, (4-i)*150 - 6*75 - 32, 64, 64);
                how.setText(subTitleFont, Integer.toString((2-i)*6 + j));
                subTitleFont.draw(spriteCranberry, Integer.toString(i*6 + j + 1), j*120 - 5*60 - how.width/2, (4-i)*150 - 6*75 + 5);
                if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
                    if(Math.sqrt(Math.pow(-1280/2 + Gdx.input.getX()*1280/1920 - (j*120 - 5*60 - 32), 2) + Math.pow((-720/2 + Gdx.input.getY()*720/1080) - ((3-i)*190 -390), 2)) < 45){
                        spriteCranberry.end();
                        return (2-i)*6 + j;
                    }
                }
            }
        }
        spriteCranberry.end();
        return -1;
    }
}
