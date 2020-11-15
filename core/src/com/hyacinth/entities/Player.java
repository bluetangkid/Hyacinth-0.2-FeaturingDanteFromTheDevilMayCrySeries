package com.hyacinth.entities;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;

public class Player extends DynamicEntity {
    private Gun gun;
    Body groundCheck;
    int onGround;
    short jumpTimer;
    int tileWidth;
    ArrayList<StaticEntity> collidingEntities;
    Animation<TextureRegion> running;
    Animation<TextureRegion> jump;
    Animation<TextureRegion> idle;
    TextureAtlas atlas;
    float animTime;
    SpriteBatch batch;
    PlayerState state;
    private static final float animSpeed = .17f;
    boolean direction;
    Body lArm, rArm;
    OrthographicCamera camera;
    Sound gunshot, grunt;

    public Player(World world, Vector2 spawn, int tileWidth, OrthographicCamera camera){
        super(world, Constants.PLAYER_RESTITUTION, Constants.PLAYER_RADIUS, Constants.PLAYER_DENSITY, Constants.PLAYER_FRICTION, spawn);
        this.getBody().setUserData(this);
        this.getBody().setFixedRotation(true);
        this.isPlayer = true;
        BodyDef def = new BodyDef();
        def.position.set(this.getBody().getPosition());
        def.type = BodyDef.BodyType.DynamicBody;
        groundCheck = world.createBody(def);
        CircleShape shape = new CircleShape();
        shape.setRadius(1);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.isSensor = true;
        MassData mass = new MassData();
        mass.mass = 0;
        groundCheck.setMassData(mass);
        groundCheck.createFixture(fixtureDef);

        gunshot = Gdx.audio.newSound(Gdx.files.internal("data/sound/bang 2.mp3"));
        grunt = Gdx.audio.newSound(Gdx.files.internal("data/sound/grunt.mp3"));

        //System.out.println((this.getBody().getUserData() instanceof DynamicEntity) + " " + this.isPlayer());

        Vector2 bodyPos = this.getBody().getPosition();
        BodyDef lArmDef = new BodyDef();
        lArmDef.position.set(bodyPos.x - Constants.PLAYER_RADIUS, bodyPos.y);
        lArmDef.type = BodyDef.BodyType.DynamicBody;
        lArm = world.createBody(lArmDef);
        CircleShape lArmShape = new CircleShape();
        lArmShape.setRadius(Constants.PLAYER_RADIUS/3f);
        FixtureDef lArmFix = new FixtureDef();
        lArmFix.shape = lArmShape;
        lArmFix.friction = 0.6f;
        lArmFix.restitution = .01f;
        lArmFix.density = 1;
        MassData lmass = new MassData();
        lmass.mass = 0;
        lArm.setMassData(lmass);
        lArm.createFixture(lArmFix);

        BodyDef rArmDef = new BodyDef();
        rArmDef.position.set(bodyPos.x + Constants.PLAYER_RADIUS, bodyPos.y);
        rArmDef.type = BodyDef.BodyType.DynamicBody;
        rArm = world.createBody(rArmDef);
        CircleShape rArmShape = new CircleShape();
        rArmShape.setRadius(Constants.PLAYER_RADIUS/3f);
        FixtureDef rArmFix = new FixtureDef();
        rArmFix.shape = rArmShape;
        rArmFix.friction = 0.6f;
        rArmFix.restitution = .01f;
        rArmFix.density = 1;
        MassData rmass = new MassData();
        rmass.mass = 0;
        rArm.setMassData(rmass);
        rArm.createFixture(lArmFix);

        this.tileWidth = tileWidth;
        this.collidingEntities = new ArrayList<>();
        atlas = new TextureAtlas(Gdx.files.internal("data/textures/player.atlas"));
        running = new Animation<TextureRegion>(animSpeed, atlas.findRegions("run"), Animation.PlayMode.LOOP);
        jump = new Animation<TextureRegion>(animSpeed, atlas.findRegions("jump"), Animation.PlayMode.NORMAL);
        idle = new Animation<TextureRegion>(0.46f, atlas.findRegions("idle"), Animation.PlayMode.LOOP);
        this.batch = new SpriteBatch();
        state = PlayerState.IDLE;
        animTime = animSpeed;
        direction = true;
        for (TextureRegion region : running.getKeyFrames()){
            region.flip(true, false);
        }
        for (TextureRegion region : idle.getKeyFrames()){
            region.flip(true, false);
        }
        for (TextureRegion region : jump.getKeyFrames()){
            region.flip(true, false);
        }
        this.camera = camera;
    }

    public int update() {
        Vector2 pos = this.getBody().getPosition();
        TextureRegion curFrame = idle.getKeyFrame(animTime);

        groundCheck.setTransform(pos.x, pos.y - (Constants.PLAYER_RADIUS) + 8, 0);
        lArm.setTransform(pos.x - Constants.PLAYER_RADIUS - Constants.PLAYER_RADIUS/3, pos.y, 0);
        rArm.setTransform(pos.x + Constants.PLAYER_RADIUS + Constants.PLAYER_RADIUS/3, pos.y, 0);
        if((Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) && this.getBody().getLinearVelocity().x > -Constants.PLAYER_MAX_SPEED){
            this.getBody().applyLinearImpulse(-Constants.PLAYER_IMPULSE_MUL, 0, pos.x, pos.y, true);
            this.capSpeed(Constants.PLAYER_MAX_SPEED);
            curFrame = running.getKeyFrame(animTime);

            if(!direction) {
                for (TextureRegion region : running.getKeyFrames()){
                    region.flip(true, false);
                }
                for (TextureRegion region : idle.getKeyFrames()){
                    region.flip(true, false);
                }
                for (TextureRegion region : jump.getKeyFrames()){
                    region.flip(true, false);
                }
                animTime = animSpeed;
            }
            direction = true;
            state = PlayerState.RUNNING_LEFT;
        }
        if((Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) && this.getBody().getLinearVelocity().x < Constants.PLAYER_MAX_SPEED){
            this.getBody().applyLinearImpulse(Constants.PLAYER_IMPULSE_MUL, 0, pos.x, pos.y, true);
            this.capSpeed(Constants.PLAYER_MAX_SPEED);
            if(state != PlayerState.RUNNING_RIGHT) {
                if(direction){
                    for (TextureRegion region : running.getKeyFrames()){
                        region.flip(true, false);
                    }
                    for (TextureRegion region : idle.getKeyFrames()){
                        region.flip(true, false);
                    }
                    for (TextureRegion region : jump.getKeyFrames()){
                        region.flip(true, false);
                    }
                }
                animTime = animSpeed;
            }
            direction = false;
            state = PlayerState.RUNNING_RIGHT;
            curFrame = running.getKeyFrame(animTime);
        }
        //System.out.println(this.onGround);
        if((Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.W)) && this.onGround > 0 && this.jumpTimer == 0){
            grunt.play(0.1f);
            this.getBody().setTransform(pos.x, pos.y + 2, 0);
            this.getBody().applyLinearImpulse(0, Constants.PLAYER_JUMP_FORCE*5000, pos.x, pos.y, true);
            this.jumpTimer = Constants.PLAYER_JUMP_TIMER;
            curFrame = jump.getKeyFrame(animTime);
            if(state != PlayerState.JUMPING) {
                animTime = animSpeed;
            }
            state = PlayerState.JUMPING;
        }
        if(curFrame != null && state != PlayerState.RUNNING_LEFT) curFrame.flip(false, false);
        if(Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
            this.getBody().applyLinearImpulse(0, -Constants.PLAYER_FASTFALL_SPEED*Constants.PLAYER_IMPULSE_MUL, pos.x, pos.y, true);
        }
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT) && !(this.gun == null)){
            gunshot.play(0.05f, 2, 0);
            Vector2 gunForce = this.gun.fireGun(new Vector2(Gdx.input.getX() - (float)Gdx.graphics.getWidth()/2, Gdx.input.getY() - (float)Gdx.graphics.getHeight()/2), this.getBody().getPosition(), camera).scl(Constants.GUN_FORCE_STATIC_MULT);
            //System.out.println((Gdx.input.getX() - Gdx.graphics.getWidth()/2) + " " + (Gdx.input.getY() - Gdx.graphics.getHeight()/2));
            this.getBody().applyLinearImpulse(gunForce.x, gunForce.y, pos.x, pos.y, true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.X)){
            System.out.println(collidingEntities.size());
            for(StaticEntity e : collidingEntities){
                if(e instanceof Sign){
                    ((Sign) e).displayText();
                }else if(e instanceof ExitStar){
                    return 1;
                }
            }
        }
        if(this.jumpTimer > 0) jumpTimer--;
        this.gun.update();
        if(onGround < 1) {
            curFrame = jump.getKeyFrame(animTime);
            if(state != PlayerState.JUMPING) {
                animTime = animSpeed;
            }
            state = PlayerState.JUMPING;
        } else if(curFrame == null) {
            if(state != PlayerState.IDLE) animTime = animSpeed;
            state = PlayerState.IDLE;
            curFrame = idle.getKeyFrame(animTime);
        }
        batch.begin();
        batch.draw(curFrame, (float)Gdx.graphics.getWidth()/2 - 18, (float)Gdx.graphics.getHeight()/2 - 8);
        batch.end();
        animTime += Gdx.graphics.getDeltaTime();
        return 0;
    }

    public void draw() {

    }

    public Gun getGun() {
        return gun;
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

    public void addCollidingEntity(StaticEntity entity){
        collidingEntities.add(entity);
    }
    public void removeCollidingEntity(StaticEntity entity){
        collidingEntities.remove(entity);
    }
    public void resetCollidingEntity(){
        collidingEntities = new ArrayList<>();
    }

    public void createGun(int bulletCount, float bulletSpread, float bulletForce, int clipSize, float reloadTime, int firerate){
        this.gun = new Gun(body.getWorld(), bulletCount, bulletSpread, bulletForce, clipSize, reloadTime, firerate);
    }
}