package com.last.commit.screen

import com.badlogic.gdx.Gdx
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
import com.last.commit.Game
import com.last.commit.Player
import com.last.commit.audio.GameMusic
import com.last.commit.config.ActionCommand
import com.last.commit.config.GameConfig
import com.last.commit.map.Interactable
import com.last.commit.map.TimeMap
import com.last.commit.stages.UIStage
import kotlin.math.floor

/** First screen of the application. Displayed after the application is created. */
class FirstScreen(private val parent: Game) : TimeTravelScreen() {

    val gameState = parent.state

    val viewportSize = 1200f

    private var delta = 0.008f
    private var isColliding = false

    val batch = SpriteBatch()
    val camera = OrthographicCamera(viewportSize, viewportSize)

    var map: TimeMap

    val playerTexture = Texture("sprites/characters.png")
    val player = Player(TextureRegion(playerTexture, 300, 44, 35, 43), gameState)
    var shapeRenderer = ShapeRenderer()

    val highlightColor = Color(0f, 0f, 1f, 0.5f)

    var pause = true
    var uiStage: UIStage


    init {
        val gameConfig = this.loadGameConfig()
        val randomMap = gameConfig.getRandomMap()
        map = TimeMap(randomMap, gameState)

        this.spawnPlayer()
        this.updateCamera()

        handleRatioChange()

        uiStage = UIStage("sprites/genericItems_spritesheet_colored", gameState)
        shapeRenderer.setAutoShapeType(true)

        player.addItemToInventory("drill")
        gameState.soundEngine.play(GameMusic.WORLD_MUSIC, 0.25f)

    }

    override fun handleKeyInput(action: ActionCommand) {
        if (!pause) {
            when (action) {
                ActionCommand.INTERACT -> {
                    openDoor()
                }

                ActionCommand.TIME_TRAVEL -> {
                    map.teleport(player)
                }

                else -> {}
            }
            if (action == ActionCommand.OPEN_MENU) {
                //Gdx.app.exit()
                parent.changeScreen(Screens.MAIN_MENU)
            }
        }

    }

    override fun handleMouseInput(screenX: Int, screenY: Int, pointer: Int, button: Int) {
        if (!pause) {

            val mouseCoordinates: Vector2 = toWorldCoordinates(screenX.toFloat(), screenY.toFloat())
            val playerDirection: Vector2 = player.getAbsoluteDirection()

            map.interactWith(playerDirection.x, playerDirection.y, player.getCollider())
        }
    }

    override fun show() {
        // Prepare your screen here.

        resume()
    }

    fun loadGameConfig(): GameConfig {
        val jsonFileHandle = Gdx.files.local("config.json")
        val json = Json()

        return json.fromJson(GameConfig::class.java, jsonFileHandle)
    }

    override fun render(delta: Float) {
        if (!pause) {
            handleInput(delta)
            handleMapBorderCollision()

            val mousePosition: Vector2 = getMousePosition()
            player.lookAt(mousePosition)

            batch.projectionMatrix = camera.combined
            batch.begin()
            this.map.render(batch, camera, delta)
            this.player.render(batch)
            batch.end()

            val interactables = map.getInteractablesAt(player.getAbsoluteDirection())
            renderInteractables(interactables)

            uiStage.act(delta)
            uiStage.draw()
        }
    }

    fun renderInteractables(interactables: List<Interactable>) {
        Gdx.gl.glEnable(GL20.GL_BLEND)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)
        shapeRenderer.setProjectionMatrix(this.camera.combined)
        shapeRenderer.setColor(highlightColor)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        for (interactable in interactables) {
            shapeRenderer.rect(
                interactable.getCollider().x,
                interactable.getCollider().y,
                interactable.getCollider().width,
                interactable.getCollider().height
            )
        }
        shapeRenderer.end()
        Gdx.gl.glDisable(GL20.GL_BLEND)
    }

    private fun getMousePosition(): Vector2 {
        val unprojectedMousePosition =
            camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))
        return Vector2(unprojectedMousePosition.x, unprojectedMousePosition.y)
    }

    private fun handleMapBorderCollision() {
        val mapWidth: Int = map.width
        val mapHeight: Int = map.height
        val playerSize: Float = player.getSize()
        val playerX: Float = MathUtils.clamp(this.player.getX(), 0f, mapWidth - playerSize)
        val playerY: Float = MathUtils.clamp(this.player.getY(), 0f, mapHeight - playerSize)
        this.player.setPosition(playerX, playerY)
    }

    private fun handleInput(delta: Float) {
        val horizontalMovement = Vector2()
        if (isKeyPressed(gameState.settings.getKeyCode(ActionCommand.LEFT))) {
            horizontalMovement.sub(Vector2.X)
        }
        if (isKeyPressed(gameState.settings.getKeyCode(ActionCommand.RIGHT))) {
            horizontalMovement.add(Vector2.X)
        }
        this.player.move(horizontalMovement, delta)
        checkCollision()
        if (this.isColliding) {
            horizontalMovement.rotateDeg(180f)
            this.player.move(horizontalMovement, delta)
        }
        val verticalMovement = Vector2()
        if (isKeyPressed(gameState.settings.getKeyCode(ActionCommand.UP))) {
            verticalMovement.add(Vector2.Y)
        }
        if (isKeyPressed(gameState.settings.getKeyCode(ActionCommand.DOWN))) {
            verticalMovement.sub(Vector2.Y)
        }
        this.player.move(verticalMovement, delta)
        checkCollision()
        if (this.isColliding) {
            verticalMovement.rotateDeg(180f)
            this.player.move(verticalMovement, delta)
        }

        val hasMoved = !horizontalMovement.isZero || !verticalMovement.isZero
        if (hasMoved) {
            updateCamera()
        }
    }

    private fun isKeyPressed(keyCodes: List<Int>): Boolean {
        for (key in keyCodes) {
            if (Gdx.input.isKeyPressed(key)) {
                return true
            }
        }
        return false
    }

    private fun spawnPlayer() {
        val playerSpawn: Vector2 = map.getPlayerSpawn()
        this.player.position = playerSpawn
    }

    private fun checkCollision() {
        this.isColliding = map.isCollidingWith(player)
    }

    fun updateCamera() {
        val cX: Float
        val cY: Float

        val halfScreenWidth = camera.viewportWidth / 2
        val halfScreenHeight = camera.viewportHeight / 2
        val playerXPosition: Float = this.player.getX()
        val playerYPosition: Float = this.player.getY()
        val mapWidth: Int = map.width
        val mapHeight: Int = map.height

        cX = if (playerXPosition < halfScreenWidth) {
            halfScreenWidth
        } else if (playerXPosition > mapWidth - halfScreenWidth) {
            mapWidth - halfScreenWidth
        } else {
            playerXPosition
        }

        cY = if (playerYPosition < halfScreenHeight) {
            halfScreenHeight
        } else if (playerYPosition > mapHeight - halfScreenHeight) {
            mapHeight - halfScreenHeight
        } else {
            playerYPosition
        }

        camera.position[cX, cY] = 0f
        camera.update()
    }

    override fun resize(width: Int, height: Int) {
        // Resize your screen here. The parameters represent the new window size.
        uiStage.resize(width, height)
        handleRatioChange()
    }

    fun handleRatioChange() {
        val height = Gdx.graphics.height
        val width = Gdx.graphics.width

        val wRatio = width.toFloat() / height.toFloat()
        val hRatio = height.toFloat() / width.toFloat()
        if (wRatio < 1) {
            camera.viewportWidth = viewportSize * wRatio
            camera.viewportHeight = viewportSize
        } else {
            camera.viewportHeight = viewportSize * hRatio
            camera.viewportWidth = viewportSize
        }
        updateCamera()
    }

    override fun pause() {
        pause = true
        gameState.soundEngine.stop()

    }

    override fun resume() {
        pause = false
        gameState.soundEngine.resume();
        // Invoked when your application is resumed after pause.
    }

    override fun hide() {
        pause()
    }

    override fun dispose() {
        // Destroy screen's assets here.
        batch.dispose()
        shapeRenderer.dispose()
    }

    fun openDoor() {
        println("Attempt to toggle door")
        val playerDirection: Vector2 = player.getAbsoluteDirection()
        map.interactWith(playerDirection.x, playerDirection.y, player.getCollider())
    }

    fun toWorldCoordinates(x: Float, y: Float): Vector2 {
        val mouseInWorldPosition = camera.unproject(Vector3(x, y, 0f))
        return Vector2(
            floor(mouseInWorldPosition.x.toDouble() / this.map.getTileWidth()).toFloat(),
            floor(mouseInWorldPosition.y.toDouble() / this.map.getTileHeight()).toFloat()
        )
    }

}
