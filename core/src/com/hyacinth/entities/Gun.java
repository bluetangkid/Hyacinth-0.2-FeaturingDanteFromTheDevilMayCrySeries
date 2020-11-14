package com.hyacinth.entities;

import com.badlogic.gdx.math.Vector2;

//for his neutral special, he wields a
public class Gun {
    public Gun(){
        //TODO: what are we inputting? bullet count, spread, force for each bullet? im just gonna put test variables here for now
        float bullet_count = 1f;
        float bullet_spread = 0f;
        float bullet_force = 1f;
    }
    public Vector2 fireGun(Vector2 direction){
        //directions should be the position of the mouse relative to the player
        //returns the force to be applied on the user

        return new Vector2();
    }
}
