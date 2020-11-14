package com.hyacinth.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Player extends DynamicEntity {

    public Player(World world){
        super(world, Constants.PLAYER_RESTITUTION, Constants.PLAYER_RADIUS, Constants.PLAYER_DENSITY, Constants.PLAYER_FRICTION);
        this.getBody().setFixedRotation(true);
    }

    public void update() {
        Vector2 pos = this.getBody().getPosition();
        if(Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT) && this.getBody().getLinearVelocity().len() < Constants.PLAYER_MAX_SPEED){
            this.getBody().applyLinearImpulse(-1*Constants.PLAYER_IMPULSE_MUL, 0, pos.x, pos.y, true);
        }
        if((Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) && this.getBody().getLinearVelocity().len() < Constants.PLAYER_MAX_SPEED){
            this.getBody().applyLinearImpulse(1*Constants.PLAYER_IMPULSE_MUL, 0, pos.x, pos.y, true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.W) && this.getBody().getLinearVelocity().len() < Constants.PLAYER_MAX_SPEED){
            this.getBody().applyLinearImpulse(0, 1*Constants.PLAYER_IMPULSE_MUL, pos.x, pos.y, true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
            this.getBody().applyLinearImpulse(0, -.2f*Constants.PLAYER_IMPULSE_MUL, pos.x, pos.y, true);
        }
    }

    public void draw() {

    }
}