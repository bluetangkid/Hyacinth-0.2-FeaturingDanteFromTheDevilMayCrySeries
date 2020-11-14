package com.hyacinth.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Player extends DynamicEntity {
    private Gun gun;
    Body groundCheck;
    int onGround;
    short jumpTimer;

    public Player(World world, Vector2 spawn){
        super(world, Constants.PLAYER_RESTITUTION, Constants.PLAYER_RADIUS, Constants.PLAYER_DENSITY, Constants.PLAYER_FRICTION, spawn);
        this.getBody().setFixedRotation(true);
        this.gun = new Gun(world);
        BodyDef def = new BodyDef();
        def.position.set(this.getBody().getPosition());
        def.type = BodyDef.BodyType.DynamicBody;
        groundCheck = world.createBody(def);
        CircleShape shape = new CircleShape();
        shape.setRadius(1);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        groundCheck.createFixture(fixtureDef);
        this.isPlayer = true;
    }

    public void update() {
        Vector2 pos = this.getBody().getPosition();
        if(Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            this.getBody().applyLinearImpulse(-Constants.PLAYER_IMPULSE_MUL, 0, pos.x, pos.y, true);
            this.capSpeed(Constants.PLAYER_MAX_SPEED);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            this.getBody().applyLinearImpulse(Constants.PLAYER_IMPULSE_MUL, 0, pos.x, pos.y, true);
            this.capSpeed(Constants.PLAYER_MAX_SPEED);
        }
        if((Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.W)) && onGround > 0 && jumpTimer == 0){
            this.getBody().setTransform(pos.x, pos.y + 1, 0);
            this.getBody().applyLinearImpulse(0, Constants.PLAYER_JUMP_FORCE*Constants.PLAYER_IMPULSE_MUL, pos.x, pos.y, true);
            jumpTimer = 20;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
            this.getBody().applyLinearImpulse(0, -Constants.PLAYER_FASTFALL_SPEED*Constants.PLAYER_IMPULSE_MUL, pos.x, pos.y, true);
        }
        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && !(this.gun == null)){
            Vector2 gunForce = this.gun.fireGun(new Vector2(Gdx.input.getX() - (float)Gdx.graphics.getWidth()/2, Gdx.input.getY() - (float)Gdx.graphics.getHeight()/2), this.getBody().getPosition()).scl(Constants.GUN_FORCE_STATIC_MULT);
            //System.out.println((Gdx.input.getX() - Gdx.graphics.getWidth()/2) + " " + (Gdx.input.getY() - Gdx.graphics.getHeight()/2));
            this.getBody().applyLinearImpulse(gunForce.x, gunForce.y, pos.x, pos.y, true);
        }
        groundCheck.setTransform(pos.x, pos.y - Constants.PLAYER_RADIUS - 1, 0);
        //System.out.println(onGround);
        if(jumpTimer > 0) jumpTimer--;
    }

    public void draw() {

    }

    private void capSpeed(float speed){
        // cap the horizontal player speed ONLY IF HOLDING LEFT OR RIGHT so movement is cooler
        if(this.getBody().getLinearVelocity().x > Constants.PLAYER_MAX_SPEED){
            this.getBody().setLinearVelocity(this.getBody().getLinearVelocity().set(speed, this.getBody().getLinearVelocity().y));
        }
        if(this.getBody().getLinearVelocity().x < -Constants.PLAYER_MAX_SPEED){
            this.getBody().setLinearVelocity(this.getBody().getLinearVelocity().set(-speed, this.getBody().getLinearVelocity().y));
        }
    }

    public void addGround(int bruh){
        this.onGround += bruh;
    }
    public void createGun(int bulletCount, float bulletSpread, float bulletForce){
        this.gun = new Gun(body.getWorld(), bulletCount, bulletSpread, bulletForce);
    }
}