package com.last.commit.screen

import com.badlogic.gdx.Screen

abstract class TimeTravelScreen : Screen{
    
    abstract fun handleKeyInput(keyCode : Int)
    abstract fun handleMouseInput(screenX: Int, screenY: Int, pointer: Int, button: Int)
}