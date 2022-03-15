package com.mygame.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.mygame.helpers.CollisionHandler;
import com.mygame.helpers.MapHelper;
import com.mygame.objects.Ball;
import com.mygame.objects.Paddle;

public class GameScreen extends ScreenAdapter {

    private OrthographicCamera camera;
    private final SpriteBatch batch;
    private final float WIDTH = 100,HEIGHT = 100;
    private float OFFSET;
    private Box2DDebugRenderer box2DDebugRenderer;
    private World world;
    private final MapHelper mapHelper;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
    private Ball ball;
    private Paddle paddle;
    private Array<Body> brickList;
    public Array<Body> bodiesToBeDestroyed;

    public GameScreen(){
        batch = new SpriteBatch();
        box2DDebugRenderer = new Box2DDebugRenderer();

        mapHelper = new MapHelper(this);
        bodiesToBeDestroyed = new Array<>();

        world  = new World(new Vector2(0,0),false);
        world.setContactListener(new CollisionHandler(this,mapHelper));

        orthogonalTiledMapRenderer = mapHelper.getMapRenderer();
        brickList = mapHelper.getBricks();
        OFFSET = HEIGHT - WIDTH * (mapHelper.tileHeight * mapHelper.mapHeight)/(mapHelper.tileWidth * mapHelper.mapWidth);

        camera = new OrthographicCamera(WIDTH,HEIGHT);
        camera.position.set(WIDTH/2,HEIGHT/2-OFFSET,0);

        ball = new Ball(WIDTH/2,-OFFSET+2,1,new Vector2(50,50),this);
        paddle = new Paddle(WIDTH/2,-OFFSET+1,15,2,50f,this);
    }

    public void update(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !ball.hasStarted()) {
            ball.start();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            paddle.move(-1,delta);
            if(!ball.hasStarted())
                ball.move(paddle.getX());
        }

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            paddle.move(1,delta);
            if(!ball.hasStarted())
                ball.move(paddle.getX());
        }

        camera.update();
        ball.update();
        paddle.update();
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        for (Body b:bodiesToBeDestroyed){
            world.destroyBody(b);
        }
        bodiesToBeDestroyed.clear();

        world.step(Math.min(delta,1/60f), 6, 2);

        update(delta);

        orthogonalTiledMapRenderer.setView(camera);
        orthogonalTiledMapRenderer.render();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        ball.draw(batch);
        paddle.draw(batch);
        batch.end();
//        box2DDebugRenderer.render(world, camera.combined);
    }

    @Override
    public void dispose(){
        world.dispose();
    }

    public float getWIDTH() {
        return WIDTH;
    }

    public float getHEIGHT() {
        return HEIGHT;
    }

    public float getOFFSET() {
        return OFFSET;
    }

    public World getWorld() {
        return world;
    }

    public Ball getBall() {
        return ball;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public void addToDestroyable(Body body){
        bodiesToBeDestroyed.add(body);
    }
}

