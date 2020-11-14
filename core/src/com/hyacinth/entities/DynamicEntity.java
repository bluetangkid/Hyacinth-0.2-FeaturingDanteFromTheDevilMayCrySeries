package com.hyacinth.entities;

import com.badlogic.gdx.physics.box2d.*;

public abstract class DynamicEntity {
    Body body;

    DynamicEntity(World w, float res, float rad, float density, float friction){
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(20, 0);
        this.body = w.createBody(def);
        CircleShape shape = new CircleShape();
        shape.setRadius(rad);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.restitution = res;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        shape.dispose();
        this.body.setUserData(this);
    }

    public Body getBody (){
        return body;
    }

    public abstract void update();

    public abstract void draw();
}
