package com.hyacinth.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Bullet extends DynamicEntity {
    int timer;
    SpriteBatch batch;
    static Texture image;
    OrthographicCamera camera;

    Bullet(World world, Vector2 velocity, Vector2 position, OrthographicCamera camera){
        super(world, Constants.BULLET_RESTITUTION, Constants.BULLET_RADIUS, Constants.BULLET_DENSITY, Constants.BULLET_FRICTION, position);
        this.getBody().setBullet(true);
        this.getBody().setLinearVelocity(velocity.x * Constants.BULLET_SPEED_SCALE, velocity.y * Constants.BULLET_SPEED_SCALE);
        this.getBody().applyTorque(5, true);
        //System.out.println(this.getBody().getLinearVelocity().x + " " + this.getBody().getLinearVelocity().y);
        this.timer = 0;
        batch = new SpriteBatch();
        image = new Texture(Gdx.files.internal("data/textures/cannonball.png"));
        this.camera = camera;
    }
    public void update() {
        this.timer++;
        if(this.getBody().getLinearVelocity().len() < Constants.BULLET_MIN_VELOCITY || this.getBody().getPosition().y < -5000){
            this.getBody().setActive(false);
        }
        Vector2 pos = this.getBody().getPosition();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(image, pos.x, pos.y, 4, 4);
        batch.end();
    }

    public void draw() {

    }
}
