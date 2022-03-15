package com.mygame.game;

import com.badlogic.gdx.Game;
import com.mygame.views.GameScreen;

public class Boot extends Game{

	@Override
	public void create (){
		setScreen(new GameScreen());
	}

	@Override
	public void render () {
		super.render();
	}
}
