package com.last.commit.config

import com.badlogic.gdx.Input.Keys
import java.util.*

class PlayerSettings {

    val fullscreen: Boolean = false
    private val actionKeys: EnumMap<ActionCommand, List<Int>> = EnumMap(ActionCommand::class.java)
    private val actionKeysReversed: HashMap<Int, ActionCommand> = hashMapOf()



    init {
        actionKeys[ActionCommand.UP] = listOf(Keys.UP, Keys.W)
        actionKeys[ActionCommand.DOWN] = listOf(Keys.DOWN, Keys.S)
        actionKeys[ActionCommand.LEFT] = listOf(Keys.LEFT, Keys.A)
        actionKeys[ActionCommand.RIGHT] = listOf(Keys.RIGHT, Keys.D)
        actionKeys[ActionCommand.OPEN_MENU] = listOf(Keys.ESCAPE)
        actionKeys[ActionCommand.OPEN_INVENTORY] = listOf(Keys.I, Keys.A)
        actionKeys[ActionCommand.TIME_TRAVEL] = listOf(Keys.T)
        actionKeys[ActionCommand.INTERACT] = listOf(Keys.E)

        setReversed(actionKeys)

    }

    private fun setReversed(actionKeys: EnumMap<ActionCommand, List<Int>>) {
        for (actionCode in actionKeys.keys){
            for (key in actionKeys.getValue(actionCode)) {
                actionKeysReversed[key] = actionCode
            }
        }

    }

    fun getAction(keyCode: Int): ActionCommand? {
        return actionKeysReversed[keyCode]
    }

    fun getKeyCode(actionCommand: ActionCommand): List<Int>? {
        return actionKeys[actionCommand]

    }
    private fun isActionPressed(actionCommand: ActionCommand, keyCode: Int): Boolean {
        return actionCommand == getAction(keyCode)
    }

    fun isOpenMenuPressed(keyCode: Int): Boolean {
        return isActionPressed(ActionCommand.OPEN_MENU, keyCode)
    }
    fun isOpenInventoryPressed(keyCode: Int): Boolean {
        return isActionPressed(ActionCommand.OPEN_INVENTORY, keyCode)
    }

    fun isUpPressed(keyCode: Int): Boolean {
        return isActionPressed(ActionCommand.UP, keyCode)
    }

    fun isDownPressed(keyCode: Int): Boolean {
        return isActionPressed(ActionCommand.DOWN, keyCode)
    }

    fun isLeftPressed(keyCode: Int): Boolean {
        return isActionPressed(ActionCommand.LEFT, keyCode)
    }
    fun isRightPressed(keyCode: Int): Boolean {
        return isActionPressed(ActionCommand.RIGHT, keyCode)
    }
    fun isTimeTravelPressed(keyCode: Int): Boolean {
        return isActionPressed(ActionCommand.TIME_TRAVEL, keyCode)
    }

    fun isInteractPressed(keyCode: Int): Boolean {
        return isActionPressed(ActionCommand.INTERACT, keyCode)
    }

}