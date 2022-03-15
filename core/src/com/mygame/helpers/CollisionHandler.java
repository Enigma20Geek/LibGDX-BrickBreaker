package com.mygame.helpers;

import com.badlogic.gdx.physics.box2d.*;
import com.mygame.views.GameScreen;

public class CollisionHandler implements ContactListener {

    private final float changeFactor;
    private final GameScreen gameScreen;
    private final MapHelper mapHelper;

    public CollisionHandler(GameScreen gs,MapHelper mh){
        gameScreen = gs;
        mapHelper = mh;
        changeFactor = 5f;
    }

    @Override
    public void beginContact(Contact contact) {
        float pointX;
        if(contact.getFixtureA().getUserData().equals("paddle") && contact.getFixtureB().getUserData().equals("ball")){
            pointX = contact.getWorldManifold().getPoints()[0].x;
            contact.getFixtureB().getBody().setLinearVelocity((float) (Math.signum(pointX - contact.getFixtureA().getBody().getPosition().x)*changeFactor*Math.pow(pointX - contact.getFixtureA().getBody().getPosition().x,2)),contact.getFixtureB().getBody().getLinearVelocity().y);
//            contact.getFixtureB().getBody().setLinearVelocity(contact.getFixtureB().getBody().getLinearVelocity().x + contact.getFixtureA().getBody().getLinearVelocity().x,contact.getFixtureB().getBody().getLinearVelocity().y);
        }
        else if(contact.getFixtureA().getUserData().equals("ball") && contact.getFixtureB().getUserData().equals("paddle")){
            pointX = contact.getWorldManifold().getPoints()[0].x;
            contact.getFixtureA().getBody().setLinearVelocity((float) (Math.signum(pointX - contact.getFixtureB().getBody().getPosition().x)*changeFactor*Math.pow(pointX - contact.getFixtureB().getBody().getPosition().x,2)),contact.getFixtureA().getBody().getLinearVelocity().y);
//            contact.getFixtureA().getBody().setLinearVelocity(contact.getFixtureA().getBody().getLinearVelocity().x + contact.getFixtureB().getBody().getLinearVelocity().x,contact.getFixtureA().getBody().getLinearVelocity().y);
        }

        if(contact.getFixtureA().getUserData().toString().split(",")[0].equals("tile") && contact.getFixtureB().getUserData().equals("ball")) {
            int x = Integer.parseInt(contact.getFixtureA().getUserData().toString().split(",")[1]);
            int y = Integer.parseInt(contact.getFixtureA().getUserData().toString().split(",")[2]);
            if (mapHelper.getHits(x,y) == 1) {
                mapHelper.removeTile(x,y);
                gameScreen.addToDestroyable(contact.getFixtureA().getBody());
            }
            else{
                mapHelper.replaceTile(x,y);
            }
        }

        else if(contact.getFixtureA().getUserData().equals("ball") && contact.getFixtureB().getUserData().toString().split(",")[0].equals("tile")){
            int x = Integer.parseInt(contact.getFixtureB().getUserData().toString().split(",")[1]);
            int y = Integer.parseInt(contact.getFixtureB().getUserData().toString().split(",")[2]);
            if (mapHelper.getHits(x, y) == 1) {
                mapHelper.removeTile(x,y);
                gameScreen.addToDestroyable(contact.getFixtureB().getBody());
            }
            else{
                mapHelper.replaceTile(x,y);
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
