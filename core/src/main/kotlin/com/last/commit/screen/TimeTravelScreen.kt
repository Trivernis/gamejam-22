package com.last.commit.screen

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen
import com.last.commit.config.ActionCommand

abstract class TimeTravelScreen : Screen {

    abstract fun handleKeyInput(action: ActionCommand)
    abstract fun handleMouseInput(screenX: Int, screenY: Int, pointer: Int, button: Int)

    abstract fun getInputProcessors(): Array<InputProcessor>
}