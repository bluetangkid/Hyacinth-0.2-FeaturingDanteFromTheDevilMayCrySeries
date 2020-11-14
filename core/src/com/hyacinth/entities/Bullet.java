package com.hyacinth.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Bullet extends DynamicEntity {
    int timer;
    Bullet(World world, Vector2 velocity, Vector2 position){
        super(world, Constants.BULLET_RESTITUTION, Constants.BULLET_RADIUS, Constants.BULLET_DENSITY, Constants.BULLET_FRICTION, position);
        this.getBody().setLinearVelocity(velocity.x, velocity.y);
        this.getBody().setBullet(true);
        this.timer = 0;
    }
    public void update() {
        this.timer++;
        if(timer > Constants.BULLET_MAX_TIME){
            this.getBody().setActive(false);
        }
    }

    public void draw() {

    }
}
