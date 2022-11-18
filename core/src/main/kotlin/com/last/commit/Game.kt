package com.last.commit

import com.badlogic.gdx.Game

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms.  */
class Game : Game() {
    override fun create() {
        setScreen(FirstScreen())
    }
}