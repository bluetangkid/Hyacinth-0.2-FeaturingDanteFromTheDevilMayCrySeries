package com.hyacinth.entities;

import com.badlogic.gdx.physics.box2d.World;

public class Bullet extends Entity{
    Bullet(World world){
        super(world, Constants.BULLET_RESTITUTION, Constants.BULLET_RADIUS, Constants.BULLET_DENSITY, Constants.BULLET_FRICTION);
    }
    void update() {

    }

    void draw() {

    }
}
