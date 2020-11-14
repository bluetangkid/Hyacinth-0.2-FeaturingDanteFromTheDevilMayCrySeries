package com.hyacinth.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Sign extends StaticEntity{
    String text;
    public Sign(World w, float x, float y, float width, float height, String text){
        super(w, x, y, width, height);
        this.text = text;
    }

    @Override
    public void update() {}

    public void displayText(){
        System.out.println(text);
        //TODO: display the text
    }
}
