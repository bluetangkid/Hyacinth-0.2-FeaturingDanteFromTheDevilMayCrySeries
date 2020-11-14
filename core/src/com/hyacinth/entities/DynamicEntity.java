package com.hyacinth.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public abstract class DynamicEntity {
    Body body;

    DynamicEntity(World w, float res, float rad, float density, float friction){
        BodyDef def = new BodyDef();
        def.position.set(40, 10);
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

    public Body getBody (){
        return body;
    }

    public abstract void update();

    public abstract void draw();
}
