package com.last.commit;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Game extends com.badlogic.gdx.Game {
	@Override
	public void create() {
		setScreen(new FirstScreen());
	}
}