package com.last.commit

import com.badlogic.gdx.Game
import com.last.commit.config.PlayerSettings

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms.  */
class Game : Game() {

    var settings = PlayerSettings()

    override fun create() {
        setScreen(FirstScreen(this))
    }
}