package com.last.commit

import GameState
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Json
import com.last.commit.config.ActionCommand
import com.last.commit.config.GameConfig
import com.last.commit.map.Interactable
import com.last.commit.map.TimeMap
import com.last.commit.stages.InventoryStage
import com.last.commit.stages.WorldStage
import com.last.commit.audio.GameSoundEffect
import com.last.commit.audio.GameMusic
import kotlin.math.floor

/** First screen of the application. Displayed after the application is created. */
class FirstScreen(val gameState: GameState) : Screen, InputProcessor {
    val state = ColorState()

    lateinit var inventoryStage: InventoryStage
    lateinit var worldStage: WorldStage

    override fun show() {
        // Prepare your screen here.

        inventoryStage = InventoryStage("sprites/genericItems_spritesheet_colored", gameState.inventory)
        worldStage = WorldStage(1200f, 1200f, gameState)
        inventoryStage.viewport.setCamera(worldStage.camera)

        Gdx.input.setInputProcessor(this)
        // gameState.soundEngine.play(GameMusic.WORLD_MUSIC, 0.25f)
    }

    fun loadGameConfig(): GameConfig {
        val jsonFileHandle = Gdx.files.local("config.json")
        val json = Json()

        return json.fromJson(GameConfig::class.java, jsonFileHandle)
    }

    override fun render(delta: Float) {
        //        state.step((delta * 1000).toLong())
        // Draw your screen here. "delta" is the time since last render in seconds.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        Gdx.gl.glClearColor(state.red, state.green, state.blue, 1f)

        worldStage.act(delta)
        worldStage.draw()
        inventoryStage.act(delta)
        inventoryStage.draw()
    }
    
    override fun resize(width: Int, height: Int) {
        // Resize your screen here. The parameters represent the new window size.
        inventoryStage.resize(width, height)
        worldStage.resize(width, height)
    }

    override fun pause() {
        // Invoked when your application is paused.
    }

    override fun resume() {
        // Invoked when your application is resumed after pause.
    }

    override fun hide() {
        // This method is called when another screen replaces this one.
    }

    override fun dispose() {
        // Destroy screen's assets here.
    }

    override fun keyDown(keycode: Int): Boolean {
        return this.worldStage.keyDown(keycode)
    }

    override fun keyUp(keycode: Int): Boolean {
        if (gameState.settings.getAction(keycode) == ActionCommand.OPEN_MENU) {
            Gdx.app.exit()
        }
        this.worldStage.keyUp(keycode)
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        this.worldStage.touchUp(screenX, screenY, pointer, button)
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        this.worldStage.touchDragged(screenX, screenY, pointer)
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        // TODO: "Not yet implemented"
        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        // TODO: Not yet implemented
        return false
    }


    override fun keyTyped(character: Char): Boolean { 
        return this.worldStage.keyTyped(character)
    }

    override fun touchDown(p0: Int, p1: Int, p2: Int, p3: Int): Boolean {
        return this.worldStage.touchDown(p0, p1, p2, p3)
    }
}
