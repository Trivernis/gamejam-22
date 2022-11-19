package com.last.commit.config

import com.badlogic.gdx.Input.Keys
import java.util.*

class GameSettings {

    val fullscreen: Boolean = false
    private val actionKeys: EnumMap<ActionCommand, List<Int>> = EnumMap(ActionCommand::class.java)
    private val actionKeysReversed: HashMap<Int, ActionCommand> = hashMapOf()


    init {
        actionKeys[ActionCommand.UP] = listOf(Keys.UP, Keys.W)
        actionKeys[ActionCommand.DOWN] = listOf(Keys.DOWN, Keys.S)
        actionKeys[ActionCommand.LEFT] = listOf(Keys.LEFT, Keys.A)
        actionKeys[ActionCommand.RIGHT] = listOf(Keys.RIGHT, Keys.D)
        actionKeys[ActionCommand.OPEN_MENU] = listOf(Keys.ESCAPE)
        actionKeys[ActionCommand.OPEN_INVENTORY] = listOf(Keys.I)
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

}