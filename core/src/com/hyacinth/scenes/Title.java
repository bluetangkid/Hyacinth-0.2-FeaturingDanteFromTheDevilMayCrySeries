package com.hyacinth.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.hyacinth.GameState;
import com.hyacinth.entities.Button;

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
    Button controls, credits, play, exit;


    public Title(FreeTypeFontGenerator generator) {
        FreeTypeFontParameter title = new FreeTypeFontParameter();
        title.color = Color.DARK_GRAY;
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
        sub.color = Color.WHITE;
        disclaimer = generator.generateFont(sub);
        how = new GlyphLayout();
        how.setText(subTitleFont, "Featuring Dante from the Devil May Cry series");
        spriteCranberry = new SpriteBatch();
        rotation = new Matrix4();
        //rotation.trn(-how.width/2, -how.height/2, 0);
        //rotation.rotate(new Vector3(0, 0, 1), -50);
        baseMatrix = spriteCranberry.getTransformMatrix().cpy();
        Texture button = new Texture(Gdx.files.internal("data/textures/long_button.png"));
        controls = new Button(button, "Controls", disclaimer, new Vector2(1920/2, 350));
        credits = new Button(button, "Credits", disclaimer, new Vector2(1920-(56*2) - 50, 30));
        play = new Button(button, "Play", disclaimer, new Vector2(1920/2, 500));
        exit = new Button(button, "Exit", disclaimer, new Vector2(50 + (56*2), 30));
    }

    public GameState draw(Camera c) {
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

        if(controls.doHe()) return GameState.CONTROLS;
        credits.doHe();

        if(play.doHe()) return GameState.LEVEL_SELECT;
        if(exit.doHe()) System.exit(0);

        scale += scaleDir ? -.003 : .003;
        if(scale < .85 || scale > 1.15) scaleDir = !scaleDir;

        return GameState.DEFAULT;
    }
}
