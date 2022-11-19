package com.last.commit

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
import com.last.commit.config.GameConfig
import com.last.commit.map.Interactable
import com.last.commit.map.TimeMap
import com.last.commit.stages.InventoryStage
import kotlin.math.floor

/** First screen of the application. Displayed after the application is created. */
class FirstScreen : Screen, InputProcessor {

    val viewportSize = 800f

    private var delta = 0f
    private var isColliding = false
    val state = ColorState()

    val batch = SpriteBatch()
    val camera = OrthographicCamera(viewportSize, viewportSize)
    lateinit var map: TimeMap // = TimeMap("tiled/base.tmx")
    val playerTexture = Texture("sprites/characters.png")
    val player = Player(TextureRegion(playerTexture, 300, 44, 35, 43))
    var shapeRenderer = ShapeRenderer()

    val highlightColor = Color(0f, 0f, 1f, 0.5f)

    lateinit var inventoryStage: InventoryStage

    override fun show() {
        // Prepare your screen here.

        val gameConfig = this.loadGameConfig()
        val randomMap = gameConfig.getRandomMap()
        map = TimeMap(randomMap)
        handleRatioChange()

        this.spawnPlayer()
        this.updateCamera()

        player.addItemToInventory("drill")
        inventoryStage = InventoryStage(player.inventory)
        shapeRenderer.setAutoShapeType(true)

        Gdx.input.setInputProcessor(this)
    }

    fun loadGameConfig(): GameConfig {
        val jsonFileHandle = Gdx.files.local("config.json")
        val json = Json()

        return json.fromJson(GameConfig::class.java, jsonFileHandle)
    }

    override fun render(delta: Float) {
        this.delta = delta
        //        state.step((delta * 1000).toLong())
        // Draw your screen here. "delta" is the time since last render in seconds.
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        Gdx.gl.glClearColor(state.red, state.green, state.blue, 1f)

        handleInput()
        handleMapBorderCollision()

        val mousePosition: Vector2 = getMousePosition()
        player.lookAt(mousePosition)
        val interactables = map.getInteractablesAt(player.getAbsoluteDirection())

        batch.projectionMatrix = camera.combined
        batch.begin()
        this.map.render(batch, camera, delta)
        this.player.render(batch)
        batch.end()

        // TODO: auslagern in sperate Methode
        renderInteractables(interactables)

        inventoryStage.draw()
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

    private fun handleInput() {
        val horizontalMovement = Vector2()
        if (Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT)) {
            horizontalMovement.sub(Vector2.X)
        }
        if (Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT)) {
            horizontalMovement.add(Vector2.X)
        }
        this.player.move(horizontalMovement, delta)
        checkCollision()
        if (this.isColliding) {
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
        inventoryStage.resize(width, height)
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
        batch.dispose()
        shapeRenderer.dispose()
    }

    override fun keyDown(keycode: Int): Boolean {
        // TODO: Auto-generated method stub
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        if (keycode == Keys.ESCAPE) {
            Gdx.app.exit()
        }
        return false
    }

    override fun keyTyped(character: Char): Boolean {
        if (character == 'e') {
            openDoor()
        } else if (character == 't') {
            map.teleport(player)
        } else if (character == 'i') {
            inventoryStage.visible = !inventoryStage.visible
        } else if (character == 'p') {
            player.inventory.add("compass")
            inventoryStage.refresh()
        }
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val mouseCoordinates: Vector2 = toWorldCoordinates(screenX.toFloat(), screenY.toFloat())
        println("Mouse World coordinates is ${mouseCoordinates.x}:${mouseCoordinates.y}")

        val playerDirection: Vector2 = player.getAbsoluteDirection()
        println("Player interactor is ${playerDirection.x}:${playerDirection.y}")
        map.interactWith(playerDirection.x, playerDirection.y, player.getCollider())
        // TODO Auto-generated method stub
        return false
    }

    fun openDoor() {
        println("Attempt to toggle door")
        val playerDirection: Vector2 = player.getAbsoluteDirection()
        println("Player interactor is ${playerDirection.x}:${playerDirection.y}")
        map.interactWith(playerDirection.x, playerDirection.y, player.getCollider())
    }

    fun toWorldCoordinates(x: Float, y: Float): Vector2 {
        val mouseInWorldPosition = camera.unproject(Vector3(x, y, 0f))
        return Vector2(
                floor(mouseInWorldPosition.x.toDouble() / this.map.getTileWidth()).toFloat(),
                floor(mouseInWorldPosition.y.toDouble() / this.map.getTileHeight()).toFloat()
        )
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        // TODO: ("Not yet implemented")
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        // TODO: ("Not yet implemented")
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
}
