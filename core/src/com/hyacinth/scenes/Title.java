package com.hyacinth.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class Title {
    BitmapFont titleFont;
    BitmapFont subTitleFont;
    BitmapFont disclaimer;
    SpriteBatch spriteCranberry;
    Matrix4 baseMatrix;
    Matrix4 rotation;
    float scale = 1;
    boolean scaleDir = true;
    GlyphLayout how;
    GlyphLayout titleLayout;

    public Title(FreeTypeFontGenerator generator) {
        FreeTypeFontParameter title = new FreeTypeFontParameter();
        title.size = 110;
        title.kerning = false;
        titleFont = generator.generateFont(title);
        FreeTypeFontParameter sub = new FreeTypeFontParameter();
        titleLayout = new GlyphLayout();
        titleLayout.setText(titleFont, "Hyacinth 0.2");
        sub.size = 18;
        sub.kerning = false;
        sub.color = Color.GOLD;
        subTitleFont = generator.generateFont(sub);
        how = new GlyphLayout();
        how.setText(subTitleFont, "Featuring Dante from the Devil May Cry series");
        spriteCranberry = new SpriteBatch();
        rotation = new Matrix4();
        //rotation.trn(-how.width/2, -how.height/2, 0);
        //rotation.rotate(new Vector3(0, 0, 1), -50);
        baseMatrix = spriteCranberry.getTransformMatrix().cpy();
    }

    public boolean draw(Camera c) {
        spriteCranberry.begin();
        titleFont.draw(spriteCranberry, "Hyacinth 0.2", 1920/2 - titleLayout.width/2, 1080/1.35f - titleLayout.height/2);
        spriteCranberry.end();

        rotation.trn(1350, 670, 0);
        rotation.scale(scale, scale, scale);
        rotation.rotate(new Vector3(0, 0, 1), 20);
        spriteCranberry.setTransformMatrix(rotation);
        spriteCranberry.begin();
        subTitleFont.draw(spriteCranberry, "Featuring Dante from the Devil May Cry Series", -how.width/2, -how.height/2);
        spriteCranberry.end();
        spriteCranberry.setTransformMatrix(baseMatrix);
        rotation.rotate(new Vector3(0, 0, 1), -20);
        rotation.trn(-1350, -670, 0);
        rotation.scale(1f/scale, 1f/scale, 1f/scale);

        scale += scaleDir ? -.003 : .003;
        if(scale < .85 || scale > 1.15) scaleDir = !scaleDir;

        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            return true;
        }
        return false;
    }
}
