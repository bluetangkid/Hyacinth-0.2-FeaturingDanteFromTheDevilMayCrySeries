package com.hyacinth.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public abstract class StaticEntity {
    Body body;
    World world;
    StaticEntity(World w, float x, float y, float width, float height){
        this.world = w;
        Rectangle rectangle = new Rectangle(x, y, width, height);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        this.body = this.world.createBody(bodyDef);
        FixtureDef def = new FixtureDef();
        def.shape = getShapeFromRectangle(rectangle);
        def.isSensor = true;
        body.setTransform(getTransformedCenterForRectangle(rectangle), 0);
        body.createFixture(def);
        body.setUserData(this);
    }

    public abstract void update();

    public static Shape getShapeFromRectangle(Rectangle rectangle){
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(rectangle.width*0.5F,rectangle.height*0.5F);
        return polygonShape;
    }

    public static Vector2 getTransformedCenterForRectangle(Rectangle rectangle){
        Vector2 center = new Vector2();
        rectangle.getCenter(center);
        return center;
    }
}
