package com.hyacinth.entities;

import com.badlogic.gdx.physics.box2d.*;

abstract class Entity {
    Entity(World w, float res, float rad, float density, float friction){
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(100, 100);
        Body body = w.createBody(def);
        CircleShape shape = new CircleShape();
        shape.setRadius(rad);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.restitution = res;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        shape.dispose();
    }

    abstract void update();

    abstract void draw();
}
