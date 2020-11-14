package com.hyacinth.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;

//for his neutral special, he wields a
public class Gun {
    World world;
    float bullet_count, bullet_force, bullet_spread;
    ArrayList<Bullet> bullets;
    public Gun(World w){
        //TODO: what are we inputting? bullet count, spread, force for each bullet? repeats? im just gonna put test variables here for now
        this.world = w;
        this.bullet_count = 10;
        this.bullet_spread = 20f;
        this.bullet_force = .2f;
        this.bullets = new ArrayList<>();
    }
    public Vector2 fireGun(Vector2 direction, Vector2 position) {
        //directions should be the position of the mouse relative to the player
        //returns the force to be applied on the user
        //get the unit vector of the force (which is just opposite the direction)
        Vector2 forceCenterDirection = new Vector2(-direction.x, direction.y).nor();
        //create some bullets and counter-forces, sum them up
        Vector2 bullets_total_force = new Vector2();
        for (int i = 0; i < bullet_count; i++) {
            float spread = ((float) Math.random() * this.bullet_spread) - (this.bullet_spread / 2);
            Vector2 centerClone = new Vector2(forceCenterDirection);
            //System.out.println(spread);
            Vector2 thisDirection = centerClone.rotateDeg(spread);
            Vector2 thisForce = new Vector2(thisDirection).scl(this.bullet_force);
            bullets_total_force.add(thisForce);
            //then make the bullet for real
            this.bullets.add(new Bullet(this.world,
                    (thisDirection).scl(-1),
                    new Vector2(position).add(thisDirection.nor().scl(Constants.PLAYER_RADIUS))));
        }
        return bullets_total_force;
    }
}
