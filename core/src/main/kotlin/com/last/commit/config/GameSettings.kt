package com.last.commit.config

import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.math.MathUtils
import com.last.commit.GameState
import com.last.commit.audio.SoundEngine
import java.util.*

class GameSettings() {
    lateinit var soundEngin : SoundEngine

    private val actionKeys: EnumMap<ActionCommand, List<Int>> = EnumMap(ActionCommand::class.java)
    private val actionKeysReversed: HashMap<Int, ActionCommand> = hashMapOf()


    var musicEnabled: Boolean = true
        set(newValue: Boolean) {
        field = newValue
        soundEngin?.volumeMusic =  musicVolume
    }
    var sfxEnabled: Boolean = true
        set(newValue: Boolean) {
        field = newValue
        soundEngin?.volumeSfx =  sfxVolume
    }

    var musicVolume: Float = 0.5F
        get() {
            return if (musicEnabled) {
                field
            } else {
                0F
            }
        }
        set(newValue: Float) {
            field = newValue
            soundEngin?.volumeMusic =  musicVolume
        }
    var sfxVolume: Float = 0.5F
        get() {
            return if (sfxEnabled) {
                field
            } else {
                0F
            }
        }
        set(newValue: Float) {
            field = newValue

            println("settings volume sfx :$sfxVolume")
            soundEngin?.volumeSfx = sfxVolume
            soundEngin?. let {
                println("soundengine volume sfx :$sfxVolume")
            }
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