package com.last.commit.config

import com.badlogic.gdx.Input.Keys
import java.util.*
import javax.swing.Action

class PlayerSettings {

    val fullscreen: Boolean = false
    val actionKeys: EnumMap<ActionCommand, List<Int>> = EnumMap(ActionCommand::class.java)


    init {
        actionKeys[ActionCommand.UP] = listOf(Keys.UP, Keys.W)
        actionKeys[ActionCommand.DOWN] = listOf(Keys.DOWN, Keys.S)
        actionKeys[ActionCommand.LEFT] = listOf(Keys.LEFT, Keys.A)
        actionKeys[ActionCommand.RIGHT] = listOf(Keys.RIGHT, Keys.D)
        actionKeys[ActionCommand.OPEN_MENU] = listOf(Keys.ESCAPE)
        actionKeys[ActionCommand.OPEN_INVENTORY] = listOf(Keys.I, Keys.A)
        actionKeys[ActionCommand.TIME_TRAVEL] = listOf(Keys.T)
    }

}