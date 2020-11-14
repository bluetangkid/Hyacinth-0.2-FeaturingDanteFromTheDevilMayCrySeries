package com.hyacinth.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapProperties;
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
        tiledBoxToBodies(map, world, "Tile Layer 1");
        MapProperties properties = map.getLayers().get("Tile Layer 1").getProperties();
        Vector2 spawn = this.getSpawnLocation(properties);
        debugRenderer = new Box2DDebugRenderer();
        player = new Player(world, spawn);
        this.createGun(properties);
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
        tiledBoxToBodies(map, world, "Tile Layer 1");
        Gdx.input.setCursorCatched(true);
    }

    public void render(OrthographicCamera camera, TiledMapRenderer renderer){
        Array<Body> bodies = new Array<>();
        Vector2 playerPosition = new Vector2();
        world.getBodies(bodies);
        for (Body b : bodies) {
            DynamicEntity entity = (DynamicEntity) b.getUserData();
            if (entity != null) {
                entity.update();
                if (entity instanceof Player) {
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
        float frameTime = Math.min(deltaTime, 0.25f * 60f/Gdx.graphics.getDisplayMode().refreshRate);
        accumulator += frameTime;
        time = System.currentTimeMillis();
        while (accumulator >= timeStep) {
            world.step(timeStep, 6, 2);
            accumulator -= timeStep;
        }
    }

    public void tiledBoxToBodies(TiledMap map, World world, String layer) {
        TiledMapTileLayer mapLayer = (TiledMapTileLayer)map.getLayers().get(layer);
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

    public void setPlayerGround(int bruh){
        player.addGround(bruh);
    }

    public Vector2 getSpawnLocation(MapProperties properties){
        if(properties.containsKey("spawnX") && properties.containsKey("spawnY")){
            return new Vector2((int)properties.get("spawnX"), (int)properties.get("spawnY"));
        }
        return new Vector2();
    }

    private void createGun(MapProperties properties) {
        int bulletCount = 1, clipSize = 6;
        float bulletSpread = 0f, bulletForce = 1f, reloadTime = 1f;
        if(properties.containsKey("gunbulletcount")){
            bulletCount = (int)properties.get("gunbulletcount");
        }
        if(properties.containsKey("gunbulletspread")){
            bulletSpread = (float)properties.get("gunbulletspread");
        }
        if(properties.containsKey("gunbulletforce")){
            bulletForce = (float)properties.get("gunbulletforce");
        }
        if(properties.containsKey("gunclip")){
            clipSize = (int)properties.get("gunclip");
        }
        if(properties.containsKey("gunreload")){
            reloadTime = (float)properties.get("gunreload");
        }
        player.createGun(bulletCount, bulletSpread, bulletForce, clipSize, reloadTime);
    }
}

class GroundListener implements ContactListener {
    PlayingLevel world;

    public GroundListener(PlayingLevel w){
        this.world = w;
    }

    @Override
    public void beginContact(Contact contact) {
        if((contact.getFixtureA().isSensor() && contact.getFixtureB().getBody().getType() == BodyDef.BodyType.StaticBody) ||
                (contact.getFixtureB().isSensor() && contact.getFixtureA().getBody().getType() == BodyDef.BodyType.StaticBody)){
            world.setPlayerGround(1);
        }
    }

    @Override
    public void endContact(Contact contact) {
        if((contact.getFixtureA().isSensor() && contact.getFixtureB().getBody().getType() == BodyDef.BodyType.StaticBody) ||
                (contact.getFixtureB().isSensor() && contact.getFixtureA().getBody().getType() == BodyDef.BodyType.StaticBody)){
            world.setPlayerGround(-1);
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
        if((fixtureA.getBody().isBullet() && fixtureB.getBody().getUserData() != null && ((DynamicEntity)fixtureB.getBody().getUserData()).isPlayer())
                || (fixtureB.getBody().isBullet() && fixtureA.getBody().getUserData() != null && ((DynamicEntity)fixtureA.getBody().getUserData()).isPlayer())){
            return false;
        }
        return true;
    }
}