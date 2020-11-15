package com.hyacinth.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public abstract class DynamicEntity {
    Body body;
    boolean isPlayer = false;

    DynamicEntity(World w, float res, float rad, float density, float friction, Vector2 position){
        BodyDef def = new BodyDef();
        def.position.set(position);
        def.type = BodyDef.BodyType.DynamicBody;
        this.body = w.createBody(def);
        CircleShape shape = new CircleShape();
        shape.setRadius(rad);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.restitution = res;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        this.body.createFixture(fixtureDef);
        shape.dispose();
        this.body.setUserData(this);
    }

    public boolean isPlayer(){ return isPlayer; };

    public Body getBody (){
        return body;
    }

    public abstract int update();

    public abstract void draw();
}
