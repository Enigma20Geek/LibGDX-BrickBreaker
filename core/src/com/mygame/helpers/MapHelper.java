package com.mygame.helpers;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.mygame.views.GameScreen;

public class MapHelper {

    private final GameScreen gameScreen;
    private final TiledMap tiledMap;
    private final TiledMapTileLayer tmtl;

    public float mapWidth,mapHeight,tileWidth,tileHeight,PPM;

    public MapHelper(GameScreen gameScreen){
        this.gameScreen = gameScreen;

        tiledMap = new TmxMapLoader().load("Level3.tmx"); // Change Level Name Here
        mapWidth = tiledMap.getProperties().get("width",Integer.class);
        mapHeight = tiledMap.getProperties().get("height",Integer.class);
        tileWidth = tiledMap.getProperties().get("tilewidth",Integer.class);
        tileHeight = tiledMap.getProperties().get("tileheight",Integer.class);

        tmtl = (TiledMapTileLayer) tiledMap.getLayers().get("Tiles");

        PPM = (mapWidth*tileWidth)/gameScreen.getWIDTH();
    }

    public OrthogonalTiledMapRenderer getMapRenderer(){
        return new OrthogonalTiledMapRenderer(tiledMap,gameScreen.getWIDTH()/(mapWidth*tileWidth));
    }

    public Array<Body> getBricks(){
        Array<Body> returnList = new Array<>();

        for (RectangleMapObject r:tiledMap.getLayers().get("Boxes").getObjects().getByType(RectangleMapObject.class)) {
            BodyDef bodyDef = new BodyDef();
            float rectX = r.getRectangle().getX(),rectY = r.getRectangle().getY();
            rectX = (int)(rectX/tileWidth)*tileWidth/PPM+tileWidth/(2*PPM);
            rectY = (int)(rectY/tileHeight)*tileHeight/PPM+tileHeight/(2*PPM);
            bodyDef.position.set(rectX,rectY);
            bodyDef.fixedRotation = true;
            bodyDef.type = BodyDef.BodyType.StaticBody;
            Body body = gameScreen.getWorld().createBody(bodyDef);
            FixtureDef fixtureDef = new FixtureDef();
            PolygonShape shape = new PolygonShape();
            shape.setAsBox(tileWidth/(2*PPM),tileHeight/(2*PPM));
            fixtureDef.shape = shape;
            fixtureDef.restitution = 1f;
            body.createFixture(fixtureDef).setUserData("tile,"+(int)(r.getRectangle().getX()/tileWidth)+","+(int)(r.getRectangle().getY()/tileHeight));
            shape.dispose();
            returnList.add(body);
        }
//        tmtl.getCell(0,0).setTile(null);
        return returnList;
    }

    public int getHits(int x,int y){
        return (int)tmtl.getCell(x,y).getTile().getProperties().get("Hits");
    }

    public void replaceTile(int x,int y){
        if(tmtl.getCell(x,y) != null) {
            tmtl.getCell(x, y).setTile(tiledMap.getTileSets().getTile(tmtl.getCell(x,y).getTile().getId()+1));
        }
    }

    public void removeTile(int x,int y){
        if(tmtl.getCell(x,y) != null) {
            tmtl.getCell(x, y).setTile(null);
        }
    }
}
