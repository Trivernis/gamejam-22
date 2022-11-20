package com.last.commit.config

import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.math.MathUtils
import java.util.*

class GameSettings {

    private val actionKeys: EnumMap<ActionCommand, List<Int>> = EnumMap(ActionCommand::class.java)
    private val actionKeysReversed: HashMap<Int, ActionCommand> = hashMapOf()


    var musicEnabled: Boolean = true
    var sfxEnabled: Boolean = true

    var musicVolume: Float = 0.5F
        set(newValue: Float) {
            field = MathUtils.clamp(newValue, 1f, 0f)
        }
    var sfxVolume: Float = 0.5F
        set(newValue: Float) {
            field = MathUtils.clamp(newValue, 1f, 0f)
        }


    init {
        actionKeys[ActionCommand.UP] = listOf(Keys.UP, Keys.W)
        actionKeys[ActionCommand.DOWN] = listOf(Keys.DOWN, Keys.S)
        actionKeys[ActionCommand.LEFT] = listOf(Keys.LEFT, Keys.A)
        actionKeys[ActionCommand.RIGHT] = listOf(Keys.RIGHT, Keys.D)
        actionKeys[ActionCommand.OPEN_MENU] = listOf(Keys.ESCAPE)
        actionKeys[ActionCommand.TIME_TRAVEL] = listOf(Keys.T)
        actionKeys[ActionCommand.INTERACT] = listOf(Keys.E)
        actionKeys[ActionCommand.JUMP] = listOf(Keys.SPACE)

        musicVolume = 0.5F

        setReversed(actionKeys)

    }

    private fun setReversed(actionKeys: EnumMap<ActionCommand, List<Int>>) {
        for (actionCode in actionKeys.keys) {
            for (key in actionKeys.getValue(actionCode)) {
                actionKeysReversed[key] = actionCode
            }
        }

    }

    fun getAction(keyCode: Int): ActionCommand? {
        return actionKeysReversed[keyCode]
    }


    fun getKeyCode(actionCommand: ActionCommand): List<Int> {

        return if (Objects.nonNull(actionKeys[actionCommand])) {
            actionKeys[actionCommand]!!
        } else {
            listOf()
        }
    }

}