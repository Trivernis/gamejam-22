package com.last.commit.stages

import GameState
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FillViewport
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.Gdx
import com.last.commit.Player
import com.last.commit.map.TimeMap
import com.last.commit.map.Interactable
import com.last.commit.config.ActionCommand
import com.badlogic.gdx.Input.Keys
import kotlin.math.floor

public class WorldStage(height: Float, width: Float, val state: GameState) :
        Stage(FillViewport(height, width)) {

    val player = Player(TextureRegion(Texture("sprites/characters.png"), 300, 44, 35, 43), state)
    var map = TimeMap(state.gameConfig.getRandomMap(), state)
    var overlayRenderer = ShapeRenderer()
    val highlightColor = Color(0f, 0f, 1f, 0.5f)

    init {
        player.addItemToInventory("drill")
        this.spawnPlayer()
        this.updateCamera()
        overlayRenderer.setAutoShapeType(true)
    }

    fun resize(width: Int, height: Int) {
        this.viewport.update(width, height, true)
    }
    
    override fun act(delta: Float) {
        super.act(delta)
        this.handleInput(delta)
        this.handleMapBorderCollision()
    }
    
    override fun draw() {
        super.draw()
        val interactables = map.getInteractablesAt(player.getAbsoluteDirection())
        this.renderInteractables(interactables)
        val mousePosition: Vector2 = getMousePosition()
        player.lookAt(mousePosition)

        batch.projectionMatrix = camera.combined
    }
    
    override fun keyTyped(character: Char): Boolean {
        super.keyTyped(character)
        val characterUpperCase = character.uppercase()
        val characterKey = Keys.valueOf(characterUpperCase)

        if (state.settings.getAction(characterKey) == ActionCommand.INTERACT) {
            openDoor()
        } else if (state.settings.getAction(characterKey) == ActionCommand.TIME_TRAVEL) {
            map.teleport(player)
        }
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val mouseCoordinates: Vector2 = toWorldCoordinates(screenX.toFloat(), screenY.toFloat())
        println("Mouse World coordinates is ${mouseCoordinates.x}:${mouseCoordinates.y}")

        val playerDirection: Vector2 = player.getAbsoluteDirection()
        println("Player interactor is ${playerDirection.x}:${playerDirection.y}")
        map.interactWith(playerDirection.x, playerDirection.y, player.getCollider())
        return false
    }

    private fun openDoor() {
        println("Attempt to toggle door")
        val playerDirection: Vector2 = player.getAbsoluteDirection()
        println("Player interactor is ${playerDirection.x}:${playerDirection.y}")
        map.interactWith(playerDirection.x, playerDirection.y, player.getCollider())
    }
    
    private fun spawnPlayer() {
        val playerSpawn: Vector2 = map.getPlayerSpawn()
        this.player.position = playerSpawn
        this.addActor(player)
        this.addActor(map)
    }

    private fun renderInteractables(interactables: List<Interactable>) {
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        overlayRenderer.setProjectionMatrix(this.camera.combined)
        overlayRenderer.setColor(highlightColor)
        overlayRenderer.begin(ShapeRenderer.ShapeType.Filled)

        for (interactable in interactables) {
            overlayRenderer
            .rect(
                    interactable.getCollider().x,
                    interactable.getCollider().y,
                    interactable.getCollider().width,
                    interactable.getCollider().height
            )
        }
        overlayRenderer.end()
        Gdx.gl.glDisable(GL20.GL_BLEND)
    }
    
    private fun updateCamera() {
        val mapSize = Vector2(map.width.toFloat(), map.height.toFloat())

        val scale = viewport.worldHeight / viewport.screenHeight
        camera.position.x = MathUtils.clamp(
            player.position.x,
            (viewport.worldWidth / 2f) + (viewport.leftGutterWidth * scale),
            mapSize.x - (viewport.worldWidth / 2f) - (viewport.rightGutterWidth * scale)
        )
        camera.position.y = MathUtils.clamp(
            player.position.y,
            (viewport.worldHeight / 2f) + (viewport.topGutterHeight * scale),
            mapSize.y - (viewport.worldHeight / 2f) - (viewport.bottomGutterHeight * scale)
        )
        camera.update()
    }

    private fun handleInput(delta: Float) {        
        val horizontalMovement = Vector2()
        if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)) {
            horizontalMovement.sub(Vector2.X)
        }
        if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) {
            horizontalMovement.add(Vector2.X)
        }
        this.player.move(horizontalMovement, delta)
        var isColliding = this.map.isCollidingWith(player)

        if (isColliding) {
            horizontalMovement.rotateDeg(180f)
            this.player.move(horizontalMovement, delta)
        }
        val verticalMovement = Vector2()
        if (Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.UP)) {
            verticalMovement.add(Vector2.Y)
        }
        if (Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.DOWN)) {
            verticalMovement.sub(Vector2.Y)
        }
        this.player.move(verticalMovement, delta)
        isColliding = this.map.isCollidingWith(player)
        if (isColliding) {
            verticalMovement.rotateDeg(180f)
            this.player.move(verticalMovement, delta)
        }
        val hasMoved = !horizontalMovement.isZero || !verticalMovement.isZero

        if (hasMoved) {
            this.updateCamera()
        }
    }
    
    
    private fun handleMapBorderCollision() {
        val mapWidth: Int = map.width
        val mapHeight: Int = map.height
        val playerSize: Float = player.getSize()
        val playerX: Float = MathUtils.clamp(this.player.getX(), 0f, mapWidth - playerSize)
        val playerY: Float = MathUtils.clamp(this.player.getY(), 0f, mapHeight - playerSize)
        this.player.setPosition(playerX, playerY)
    }

    private fun getMousePosition(): Vector2 {
        val unprojectedMousePosition =
                viewport.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))
        return Vector2(unprojectedMousePosition.x, unprojectedMousePosition.y)
    }

    fun toWorldCoordinates(x: Float, y: Float): Vector2 {
        val mouseInWorldPosition = viewport.unproject(Vector3(x, y, 0f))
        return Vector2(
                floor(mouseInWorldPosition.x.toDouble() / this.map.getTileWidth()).toFloat(),
                floor(mouseInWorldPosition.y.toDouble() / this.map.getTileHeight()).toFloat()
        )
    }
}
