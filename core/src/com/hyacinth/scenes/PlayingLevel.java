package com.hyacinth.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
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
import com.hyacinth.entities.*;

import java.util.Iterator;

public class PlayingLevel {
    private TiledMap map;
    private World world;
    private OrthogonalTiledMapRenderer mapRenderer;
    private float accumulator = 0;
    private Box2DDebugRenderer debugRenderer;
    private float timeStep;
    private Player player;
    private long time;
    private OrthographicCamera camera;
    BitmapFont signFont;

    public PlayingLevel(TiledMap map, OrthographicCamera camera, FreeTypeFontGenerator fontGenerator){
        this.camera = camera;
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.color = Color.LIGHT_GRAY;
        params.size = 22;
        signFont = fontGenerator.generateFont(params);
        initialize(map); // so that we can reset
    }
    public void initialize(TiledMap map){
        this.map = map;
        world = new World(new Vector2(0, -Constants.GRAVITY), true);
        tiledBoxToBodies(map, world, "Ground");
        loadObjects(map, world, "Objects");
        MapProperties properties = map.getLayers().get("Ground").getProperties();
        Vector2 spawn = this.getSpawnLocation(properties);
        //debugRenderer = new Box2DDebugRenderer();
        player = new Player(world, spawn, map.getProperties().get("tilewidth", Integer.class), camera);
        this.createGun(properties);
        timeStep = 1f/ Gdx.graphics.getDisplayMode().refreshRate;
//        BodyDef groundBodyDef = new BodyDef();
//        groundBodyDef.position.set(new Vector2(0, -70));
//        Body groundBody = world.createBody(groundBodyDef);
//        PolygonShape groundBox = new PolygonShape();
//        groundBox.setAsBox(camera.viewportWidth, 20.0f);
//        groundBody.createFixture(groundBox, 0.0f);
//        groundBox.dispose();
        world.setContactListener(new GroundListener(this));
        world.setContactFilter(new BulletFilter(this));
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        Gdx.input.setCursorCatched(true);
    }

    public int render(OrthographicCamera camera, TiledMapRenderer renderer){
        Array<Body> bodies = new Array<>();
        Vector2 playerPosition = new Vector2();
        int levelComplete = 0;
        doPhysicsStep(System.currentTimeMillis() - time);
        world.getBodies(bodies);
        mapRenderer.setView(camera);
        mapRenderer.render();
        for (Body b : bodies) {
            if(b.getUserData() instanceof DynamicEntity) {
                DynamicEntity entity = (DynamicEntity) b.getUserData();
                if (entity != null) {
                    levelComplete += entity.update();
                    if (entity instanceof Player) {
                        playerPosition = entity.getBody().getPosition();
                    }
                    if (!entity.getBody().isActive()) {
                        //get it outta here
                        world.destroyBody(entity.getBody());
                    }
                }
            }else if(b.getUserData() instanceof StaticEntity){
                StaticEntity entity = (StaticEntity)b.getUserData();
                if (entity != null){
                    entity.update();
                }
            }
        }
        camera.position.x = playerPosition.x;
        camera.position.y = playerPosition.y;
        //debugRenderer.render(world, camera.combined);
        return levelComplete;
    }

    private void doPhysicsStep(float deltaTime) {
        float frameTime = Math.min(deltaTime, 0.25f * 60f/144f);
        accumulator += frameTime;
        time = System.currentTimeMillis();
        while (accumulator >= timeStep) {
            world.step(timeStep, 6, 2);
            accumulator -= timeStep;
        }

        if(player.getBody().getPosition().y < -20){
            reset();
        }
    }

    public void tiledBoxToBodies(TiledMap map, World world, String layer) {
        TiledMapTileLayer mapLayer = (TiledMapTileLayer)map.getLayers().get(layer);
        float tileWidth = map.getProperties().get("tilewidth", Integer.class);
        for (int i = 0; i < mapLayer.getWidth(); i++) {
            for (int j = 0; j < mapLayer.getHeight(); j++) {
                if(mapLayer.getCell(i, j) != null) {
                    Rectangle rectangle = new Rectangle(i * tileWidth + mapLayer.getOffsetX(), j * tileWidth + mapLayer.getOffsetY(), tileWidth, tileWidth);
                    BodyDef bodyDef = new BodyDef();
                    bodyDef.type = BodyDef.BodyType.StaticBody;
                    Body body = world.createBody(bodyDef);
                    FixtureDef def = new FixtureDef();
                    def.shape = getShapeFromRectangle(rectangle);
                    def.density = .2f;
                    def.isSensor = (mapLayer.getCell(i, j).getTile().getId() != 1);
                    def.friction = 0.1f;
                    body.setTransform(getTransformedCenterForRectangle(rectangle), 0);
                    body.createFixture(def);
                }
            }
        }
    }

    public void loadObjects(TiledMap map, World world, String layer){
        MapObjects objects = map.getLayers().get(layer).getObjects();
        for(int i = 0; i < objects.getCount(); i++){
            MapObject curObject = objects.get(i);
            MapProperties properties = curObject.getProperties();
            Iterator<String> test = properties.getKeys();
            while(test.hasNext()){
                System.out.println(test.next());
            }
            float x = 0, y = 0, width = 0, height = 0;
            if(properties.containsKey("x")){
                x = (float)properties.get("x");
            }else{
                System.out.println("WARNING: Unable to find position of object ID " + i);
            }
            if(properties.containsKey("y")){
                y = (float)properties.get("y");
            }
            if(properties.containsKey("width")){
                width = (float)properties.get("width");
            }
            if(properties.containsKey("height")){
                height = (float)properties.get("height");
            }
            if(properties.containsKey("text")){
                //sign!
                new Sign(world, x, y, width, height, (String)properties.get("text"), signFont);
            }else if(properties.containsKey("exit")){
                //not sign :(
                new ExitStar(world, x, y, width, height);
            }
        }
    }

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

    public void setPlayerGround(int bruh){
        player.addGround(bruh);
    }

    public void playerCollidingWithEntity(StaticEntity entity){
        player.addCollidingEntity(entity);
    }
    public void playerRemoveCollidingWithEntity(StaticEntity entity){
        player.removeCollidingEntity(entity);
    }

    public Vector2 getSpawnLocation(MapProperties properties){
        if(properties.containsKey("spawnX") && properties.containsKey("spawnY")){
            return new Vector2((int)properties.get("spawnX"), (int)properties.get("spawnY"));
        }
        return new Vector2();
    }

    private void createGun(MapProperties properties) {
        int bulletCount = 1, clipSize = 6, firerate = 20;
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
        if(properties.containsKey("gunspeed")){
            firerate = (int)properties.get("gunspeed");
        }
        player.createGun(bulletCount, bulletSpread, bulletForce, clipSize, reloadTime, firerate);
    }
    public void reset(){
        this.initialize(map);
    }
}

class Ui {
    Ui(){

    }

    void render() {

    }
}

class GroundListener implements ContactListener {
    PlayingLevel world;

    public GroundListener(PlayingLevel w){
        this.world = w;
    }

    @Override
    public void beginContact(Contact contact) {
        if(((contact.getFixtureA().isSensor() && contact.getFixtureB().getBody().getType() == BodyDef.BodyType.StaticBody) ||
                (contact.getFixtureB().isSensor() && contact.getFixtureA().getBody().getType() == BodyDef.BodyType.StaticBody)) &&
                !contact.getFixtureA().getBody().isBullet() && !contact.getFixtureB().getBody().isBullet()){
            world.setPlayerGround(1);
        }
        if (isPlayer(contact.getFixtureA()) && isStaticEntity(contact.getFixtureB())){
            world.playerCollidingWithEntity((StaticEntity)contact.getFixtureB().getBody().getUserData());
        }else if(isPlayer(contact.getFixtureB()) && isStaticEntity(contact.getFixtureA())){
            world.playerCollidingWithEntity((StaticEntity)contact.getFixtureA().getBody().getUserData());
        }
    }

    @Override
    public void endContact(Contact contact) {
        if(((contact.getFixtureA().isSensor() && contact.getFixtureB().getBody().getType() == BodyDef.BodyType.StaticBody) ||
                (contact.getFixtureB().isSensor() && contact.getFixtureA().getBody().getType() == BodyDef.BodyType.StaticBody)) &&
                !contact.getFixtureA().getBody().isBullet() && !contact.getFixtureB().getBody().isBullet()){
            world.setPlayerGround(-1);
        }
        if (isPlayer(contact.getFixtureA()) && isStaticEntity(contact.getFixtureB())){
            world.playerRemoveCollidingWithEntity((StaticEntity)contact.getFixtureB().getBody().getUserData());
        }else if(isPlayer(contact.getFixtureB()) && isStaticEntity(contact.getFixtureA())){
            world.playerRemoveCollidingWithEntity((StaticEntity)contact.getFixtureA().getBody().getUserData());
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
    private boolean isPlayer(Fixture fixture){
        return fixture.getBody().getUserData() != null &&
                fixture.getBody().getUserData() instanceof DynamicEntity &&
                ((DynamicEntity)fixture.getBody().getUserData()).isPlayer();
    }
    private boolean isStaticEntity(Fixture fixture){
        return fixture.getBody().getUserData() != null &&
                fixture.getBody().getUserData() instanceof StaticEntity;
    }
}
class BulletFilter implements ContactFilter {
    PlayingLevel world;
    BulletFilter(PlayingLevel w){
        this.world = w;
    }

    @Override
    public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
        if((fixtureA.getBody().isBullet() && isPlayer(fixtureB))
                || (fixtureB.getBody().isBullet() && isPlayer(fixtureA))){
            return false;
        }
        return true;
    }

    private boolean isPlayer(Fixture fixture){
        return fixture.getBody().getUserData() != null &&
                fixture.getBody().getUserData() instanceof DynamicEntity &&
                ((DynamicEntity)fixture.getBody().getUserData()).isPlayer();
    }
}