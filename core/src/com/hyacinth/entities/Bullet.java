package com.hyacinth.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Bullet extends DynamicEntity {
    Bullet(World world, Vector2 velocity){
        super(world, Constants.BULLET_RESTITUTION, Constants.BULLET_RADIUS, Constants.BULLET_DENSITY, Constants.BULLET_FRICTION);
        this.body.setLinearVelocity(velocity);
    }
    public void update() {

    }

    public void draw() {

    }
}
