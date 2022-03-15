package com.mygame.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.*;
import com.mygame.views.GameScreen;

public class Paddle {
    private float x,y,width;
    private final float height,speed;
    private final Body paddleBody;
    private final Texture paddleTexture;
    private final GameScreen gameScreen;

    public Paddle(float x,float y,float width,float height,float speed,GameScreen gameScreen){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.gameScreen = gameScreen;

        paddleTexture = new Texture("paddleRed.png");

        paddleBody = createPaddle();
    }

    public Body createPaddle(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x,y);
        bodyDef.fixedRotation = true;
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        Body body = gameScreen.getWorld().createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2,height/2);
        fixtureDef.shape = shape;
        fixtureDef.restitution = 1f;
        body.createFixture(fixtureDef).setUserData("paddle");
        shape.dispose();
        return body;
    }

    public void move(int dir,float delta){
        if(paddleBody.getPosition().x + dir*speed*delta <= gameScreen.getWIDTH() - width/2 && paddleBody.getPosition().x + dir*speed*delta >= width/2)
            paddleBody.setTransform(paddleBody.getPosition().x + dir*speed*delta,paddleBody.getPosition().y,0f);
        update();
    }

    public void update(){
        x = paddleBody.getPosition().x;
        y = paddleBody.getPosition().y;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(paddleTexture, x - width / 2, y - height / 2, width, height);
    }

    public float getX() {
        return x;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }
}
