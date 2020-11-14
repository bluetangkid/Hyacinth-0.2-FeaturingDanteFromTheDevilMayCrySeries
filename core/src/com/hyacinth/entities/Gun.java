package com.hyacinth.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;

//for his neutral special, he wields a
public class Gun {
    World world;
    int bulletCount, clipSize, reloadTimer, clip;
    float bulletForce, bulletSpread, reloadTime;
    ArrayList<Bullet> bullets;
    public Gun(World w, int bulletCount, float bulletSpread, float bulletForce, int clipSize, float reloadTime){
        //TODO: repeater firing?
        this.world = w;
        this.bulletCount = bulletCount;
        this.bulletSpread = bulletSpread;
        this.bulletForce = bulletForce;
        this.clipSize = clipSize;
        this.clip = this.clipSize;
        this.reloadTimer = 0;
        this.reloadTime = reloadTime;
        this.bullets = new ArrayList<>();
    }
    public Vector2 fireGun(Vector2 direction, Vector2 position) {
        //directions should be the position of the mouse relative to the player
        //returns the force to be applied on the user
        //get the unit vector of the force (which is just opposite the direction)
        if(clip > 0) {
            Vector2 forceCenterDirection = new Vector2(-direction.x, direction.y).nor();
            //create some bullets and counter-forces, sum them up
            Vector2 bullets_total_force = new Vector2();
            for (int i = 0; i < bulletCount; i++) {
                float spread = ((float) Math.random() * this.bulletSpread) - (this.bulletSpread / 2);
                Vector2 centerClone = new Vector2(forceCenterDirection);
                //System.out.println(spread);
                Vector2 thisDirection = centerClone.rotateDeg(spread);
                Vector2 thisForce = new Vector2(thisDirection).scl(this.bulletForce);
                bullets_total_force.add(thisForce);
                //then make the bullet for real
                this.bullets.add(new Bullet(this.world,
                        (thisDirection).scl(-1),
                        new Vector2(position).add(thisDirection.nor().scl(Constants.PLAYER_RADIUS))));
            }
            clip--;
            return bullets_total_force;
        }
        return new Vector2();
    }
    public void update(){
        //System.out.println(reloadTimer + "///" + clip);
        if(clip == 0 && reloadTimer <= 0){
            reloadTimer = (int)Math.ceil(reloadTime / Gdx.graphics.getDeltaTime());
        }
        if(reloadTimer > 0){
            reloadTimer--;
        }
        if(reloadTimer == 0){
            reloadTimer--;
            clip = clipSize;
        }
    }
}
