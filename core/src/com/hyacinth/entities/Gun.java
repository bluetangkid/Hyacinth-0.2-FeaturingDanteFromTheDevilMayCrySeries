package com.hyacinth.entities;

import com.badlogic.gdx.math.Vector2;

//for his neutral special, he wields a
public class Gun {
    float bullet_count, bullet_force, bullet_spread;
    public Gun(){
        //TODO: what are we inputting? bullet count, spread, force for each bullet? im just gonna put test variables here for now
        this.bullet_count = 1f;
        this.bullet_spread = 0f;
        this.bullet_force = 1f;
    }
    public Vector2 fireGun(Vector2 direction){
        //directions should be the position of the mouse relative to the player
        //returns the force to be applied on the user
        //get the unit vector of the force (which is just opposite the direction)
        Vector2 forceCenterDirection = new Vector2(-direction.x, direction.y).nor();

        return forceCenterDirection.scl(this.bullet_force);
    }
}
