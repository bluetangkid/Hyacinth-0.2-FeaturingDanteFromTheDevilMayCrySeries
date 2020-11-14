package com.hyacinth.entities;

import com.badlogic.gdx.physics.box2d.*;

public class Player extends Entity{

    public Player(World world){
        super(world, Constants.PLAYER_RESTITUTION, Constants.PLAYER_RADIUS, Constants.PLAYER_DENSITY, Constants.PLAYER_FRICTION);
    }

    public void update() {

    }

    void draw() {

    }
}