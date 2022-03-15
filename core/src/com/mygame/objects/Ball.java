package com.mygame.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.mygame.views.GameScreen;

public class Ball {
    private float x,y;
    private final float radius;
    private final Vector2 speed;
    private final Body ballBody;
    private final Texture ballTexture;
    private final GameScreen gameScreen;
    private boolean hasStarted;

    public Ball(float x,float y,float radius,Vector2 speed,GameScreen gameScreen){
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.speed = speed;
        this.gameScreen = gameScreen;

        this.hasStarted = false;

        ballTexture = new Texture("Ball.png");

        ballBody = createBall();
    }

    public Body createBall(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x,y);
        bodyDef.fixedRotation = true;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        Body body = gameScreen.getWorld().createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        fixtureDef.shape = shape;
        fixtureDef.restitution = 1f;
        body.createFixture(fixtureDef).setUserData("ball");
        shape.dispose();
        return body;
    }

    public void start(){
        this.hasStarted = true;
        ballBody.setLinearVelocity(speed);
    }

    public void update(){
        x = ballBody.getPosition().x;
        y = ballBody.getPosition().y;
        if(x + Math.signum(ballBody.getLinearVelocity().x)*radius >= gameScreen.getWIDTH() || x + Math.signum(ballBody.getLinearVelocity().x)*radius <= 0)
            ballBody.setLinearVelocity(-ballBody.getLinearVelocity().x,ballBody.getLinearVelocity().y);
        if(y + Math.signum(ballBody.getLinearVelocity().y)*radius >= gameScreen.getHEIGHT() - gameScreen.getOFFSET())
            ballBody.setLinearVelocity(ballBody.getLinearVelocity().x,-ballBody.getLinearVelocity().y);

        if(hasStarted && ballBody.getLinearVelocity().y != -speed.y && ballBody.getLinearVelocity().y <= 0)
            ballBody.setLinearVelocity(ballBody.getLinearVelocity().x,-speed.y);
        if(hasStarted && ballBody.getLinearVelocity().y != speed.y && ballBody.getLinearVelocity().y > 0)
            ballBody.setLinearVelocity(ballBody.getLinearVelocity().x, speed.y);

        if(hasStarted && ballBody.getLinearVelocity().x < speed.x && ballBody.getLinearVelocity().x >= 0)
            ballBody.setLinearVelocity(speed.x, ballBody.getLinearVelocity().y);
        if(hasStarted && ballBody.getLinearVelocity().x > -speed.x && ballBody.getLinearVelocity().x <= 0)
            ballBody.setLinearVelocity(-speed.x, ballBody.getLinearVelocity().y);
//        System.out.println(ballBody.getLinearVelocity().y);
    }

    public void draw(SpriteBatch batch){
        batch.draw(ballTexture,x-radius,y-radius,2*radius,2*radius);
    }

    public void move(float x){
        ballBody.setTransform(x,ballBody.getPosition().y,0);
        update();
    }

    public boolean hasStarted() {
        return hasStarted;
    }
}
