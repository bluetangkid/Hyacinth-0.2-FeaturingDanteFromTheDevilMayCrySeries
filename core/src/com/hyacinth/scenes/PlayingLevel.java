package com.hyacinth.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.hyacinth.Game;
import com.hyacinth.entities.Constants;
import com.hyacinth.entities.DynamicEntity;
import com.hyacinth.entities.Player;

public class PlayingLevel {
    private TiledMap map;
    private World world;
    private OrthogonalTiledMapRenderer mapRenderer;
    private float accumulator = 0;
    private Box2DDebugRenderer debugRenderer;
    private float timeStep;
    private Player player;
    private long time;

    public PlayingLevel(OrthographicCamera camera, TiledMap map){
        this.map = map;
        world = new World(new Vector2(0, -Constants.GRAVITY), true);
        debugRenderer = new Box2DDebugRenderer();
        player = new Player(world);
        timeStep = 1f/ Gdx.graphics.getDisplayMode().refreshRate;
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(new Vector2(0, -70));
        Body groundBody = world.createBody(groundBodyDef);
        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(camera.viewportWidth, 20.0f);
        groundBody.createFixture(groundBox, 0.0f);
        groundBox.dispose();
        world.setContactListener(new GroundListener(this));
        world.setContactFilter(new BulletFilter());
        mapRenderer = new OrthogonalTiledMapRenderer(map);//TODO this might need to be passed in with the map to render
        tiledBoxToBodies(map, world, "Object Layer 1");
    }

    public void render(OrthographicCamera camera, TiledMapRenderer renderer){
        Array<Body> bodies = new Array<>();
        Vector2 playerPosition = new Vector2();
        world.getBodies(bodies);
        for (Body b : bodies) {
            DynamicEntity entity = (DynamicEntity) b.getUserData();
            if (entity != null) {
                //System.out.println(b.getPosition());
                entity.update();
                if (entity instanceof Player) {
                    //System.out.println("Player");
                    playerPosition = entity.getBody().getPosition();
                }
                if(!entity.getBody().isActive()){
                    //get it outta here
                    world.destroyBody(entity.getBody());
                }
            }
        }
        camera.position.x = playerPosition.x;
        camera.position.y = playerPosition.y;
        debugRenderer.render(world, camera.combined);
        doPhysicsStep(System.currentTimeMillis() - time);
        mapRenderer.setView(camera);
        mapRenderer.render();
    }

    private void doPhysicsStep(float deltaTime) {
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        time = System.currentTimeMillis();
        while (accumulator >= timeStep) {
            world.step(timeStep, 6, 2);
            accumulator -= timeStep;
        }
    }

    public void tiledBoxToBodies(TiledMap map, World world, String layer) {
        TiledMapTileLayer mapLayer = (TiledMapTileLayer)map.getLayers().get("Tile Layer 1");
        float tileWidth = map.getProperties().get("tilewidth", Integer.class);
        for (int i = 0; i < mapLayer.getWidth(); i++) {
            for (int j = 0; j < mapLayer.getHeight(); j++) {
                if(mapLayer.getCell(i, j) != null) {
                    // System.out.println(i + " " + j);
                    Rectangle rectangle = new Rectangle(i * tileWidth + mapLayer.getOffsetX(), j * tileWidth + mapLayer.getOffsetY(), tileWidth, tileWidth);
                    BodyDef bodyDef = new BodyDef();
                    bodyDef.type = BodyDef.BodyType.StaticBody;
                    Body body = world.createBody(bodyDef);
                    Fixture fixture = body.createFixture(getShapeFromRectangle(rectangle, tileWidth), 0.2f);
                    fixture.setFriction(0.1f);
                    body.setTransform(getTransformedCenterForRectangle(rectangle, map), 0);
                }
            }
        }
    }

    public static Shape getShapeFromRectangle(Rectangle rectangle, float tileSize){
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(rectangle.width*0.5F,rectangle.height*0.5F);
        return polygonShape;
    }

    public static Vector2 getTransformedCenterForRectangle(Rectangle rectangle, TiledMap map){
        Vector2 center = new Vector2();
        rectangle.getCenter(center);
        return center;
    }

    public void setPlayerGround(boolean bruh){
        player.setGround(bruh);
    }
}

class GroundListener implements ContactListener {
    PlayingLevel world;

    public GroundListener(PlayingLevel w){
        this.world = w;
    }

    @Override
    public void beginContact(Contact contact) {
        if(contact.getFixtureA().isSensor() || contact.getFixtureB().isSensor()){
            world.setPlayerGround(false);
        }
    }

    @Override
    public void endContact(Contact contact) {
        if((contact.getFixtureA().isSensor() || contact.getFixtureB().isSensor()) && !contact.isTouching()){
            world.setPlayerGround(true);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
class BulletFilter implements ContactFilter {
    @Override
    public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
        if(fixtureA.getBody().isBullet() || fixtureB.getBody().isBullet()){
            return false;
        }
        return true;
    }
}