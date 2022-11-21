package com.last.commit.screen

import com.badlogic.gdx.*
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
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.viewport.FillViewport
import com.last.commit.Game
import com.last.commit.Player
import com.last.commit.config.GameConfig
import com.last.commit.map.Interactable
import com.last.commit.map.TimeMap
import com.last.commit.stages.PromptStage
import com.last.commit.stages.UIStage
import kotlin.math.floor

/** First screen of the application. Displayed after the application is created. */
class FirstScreen(private val parent: Game) : Screen, InputProcessor {

    val gameState = parent.state

    val viewportSize = 1200f

    private var isColliding = false

    val batch = SpriteBatch()
    val viewport = FillViewport(viewportSize, viewportSize)
    val camera = OrthographicCamera(viewportSize, viewportSize)

    lateinit var map: TimeMap

    val playerTexture = Texture("sprites/characters.png")
    val player = Player(TextureRegion(playerTexture, 300, 44, 35, 43), gameState)
    var shapeRenderer = ShapeRenderer()

    val highlightColor = Color(0f, 0f, 1f, 0.5f)

    lateinit var uiStage: UIStage
    lateinit var promptStage: PromptStage

    val inputProcessors = InputMultiplexer()

    init {

    }


    override fun show() {
        val gameConfig = this.loadGameConfig()
        val randomMap = gameConfig.getRandomMap()
        map = TimeMap(randomMap, gameState)

        this.spawnPlayer()
        this.updateCamera()

        uiStage = UIStage("sprites/genericItems_spritesheet_colored", gameState)
        shapeRenderer.setAutoShapeType(true)

        promptStage = PromptStage(Skin(Gdx.files.internal("ui/uiskin.json")))
        promptStage.addText(
            """  
        You are stranded in time.
        The time traveling device you carry with you cannot be controlled anymore.
        """.trimIndent()
        )
        promptStage.addText(
            """
        You need to find the tools required to fix your time traveling device.
        Look around the map to find them.
        """.trimIndent()
        )
        promptStage.addText(
            """
        You can use <W>, <A>, <S> and <D> to walk around the map.
        """.trimIndent()
        )
        promptStage.addText(
            """
        Doors can be opened and items picked up with <E>.
        Some doors require keys to be opened.
        """
        )
        promptStage.addText(
            """
        At some locations you can press <T> to travel to a random spot in time.
        The top left indicates where you traveled to.
        """.trimIndent()
        )
        promptStage.addText(
            """
        Some items can only be collected in certain time locations.
        A collectible item will be highlighted when hovering over it using the mouse
        """.trimIndent()
        )
        promptStage.visible = true

        viewport.camera = camera
        viewport.apply()

        resume()
        inputProcessors.addProcessor(gameState.dialogStage)
        inputProcessors.addProcessor(promptStage)
        inputProcessors.addProcessor(this)
        Gdx.input.inputProcessor = inputProcessors
    }

    fun loadGameConfig(): GameConfig {
        val jsonFileHandle = Gdx.files.local("config.json")
        val json = Json()

        return json.fromJson(GameConfig::class.java, jsonFileHandle)
    }

    override fun render(delta: Float) {
        uiStage.act(delta)
        gameState.dialogStage.act(delta)
        promptStage.act(delta)
        if (gameState.inventory.checkVictoryCondition()) {
            promptStage.visible = true
            promptStage.clearText()
            promptStage.addText("You won!")
        } else {

            if (!promptStage.visible) {
                handleInput(delta)
                handleMapBorderCollision()
            }

            val mousePosition: Vector2 = getMousePosition()
            player.lookAt(mousePosition)


            batch.projectionMatrix = camera.combined
            batch.begin()
            this.map.render(batch, camera, delta)
            this.player.render(batch)
            batch.end()

            val interactables = map.getInteractablesAt(player.getAbsoluteDirection())
            renderInteractables(interactables)

            viewport.apply()
            updateCamera()
        }
        promptStage.draw()
        uiStage.draw()
        gameState.dialogStage.draw()
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
            viewport.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))
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
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            horizontalMovement.sub(Vector2.X)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            horizontalMovement.add(Vector2.X)
        }
        this.player.move(horizontalMovement, delta)
        checkCollision()
        if (this.isColliding) {
            horizontalMovement.rotateDeg(180f)
            this.player.move(horizontalMovement, delta)
        }
        val verticalMovement = Vector2()
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            verticalMovement.add(Vector2.Y)
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
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

    override fun resize(width: Int, height: Int) {
        // Resize your screen here. The parameters represent the new window size.
        uiStage.resize(width, height)
        promptStage.resize(width, height)
        gameState.dialogStage.resize(width, height)
        viewport.update(width, height)
        viewport.apply()
        camera.update()
    }


    override fun pause() {

    }

    override fun resume() {
        this.viewport.apply()
        this.updateCamera()
        // Invoked when your application is resumed after pause.
    }

    override fun hide() {

        //gameState.soundEngine.stop()
    }

    override fun dispose() {
        // Destroy screen's assets here.
        batch.dispose()
        shapeRenderer.dispose()
    }

    fun openDoor() {
        val playerDirection: Vector2 = player.getAbsoluteDirection()
        map.interactWith(playerDirection.x, playerDirection.y, player.getCollider())
    }

    fun toWorldCoordinates(x: Float, y: Float): Vector2 {
        val mouseInWorldPosition = viewport.unproject(Vector3(x, y, 0f))
        return Vector2(
            floor(mouseInWorldPosition.x.toDouble() / this.map.getTileWidth()).toFloat(),
            floor(mouseInWorldPosition.y.toDouble() / this.map.getTileHeight()).toFloat()
        )
    }

    override fun keyDown(keycode: Int): Boolean {
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        when (keycode) {
            Input.Keys.E -> {
                openDoor()
            }

            Input.Keys.T -> {
                map.teleport(player)
            }

            Input.Keys.ESCAPE -> {
                parent.changeScreen(Screens.MAIN_MENU)
            }
        }

        return false
    }

    override fun keyTyped(character: Char): Boolean {
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        val mouseCoordinates: Vector2 = toWorldCoordinates(screenX.toFloat(), screenY.toFloat())
        val playerDirection: Vector2 = player.getAbsoluteDirection()

        map.interactWith(playerDirection.x, playerDirection.y, player.getCollider())

        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        //TODO:("Not yet implemented")
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        //TODO:("Not yet implemented")
        return false
    }

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean {
        //TODO:("Not yet implemented")
        return false
    }

    override fun scrolled(amountX: Float, amountY: Float): Boolean {
        //TODO:("Not yet implemented")
        return false
    }

}
