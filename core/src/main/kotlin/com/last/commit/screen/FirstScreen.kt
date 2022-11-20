package com.last.commit.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
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
import com.badlogic.gdx.utils.viewport.Viewport
import com.badlogic.gdx.utils.viewport.StretchViewport
import com.badlogic.gdx.utils.viewport.FillViewport
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.last.commit.Game
import com.last.commit.Player
import com.last.commit.audio.GameMusic
import com.last.commit.config.ActionCommand
import com.last.commit.config.GameConfig
import com.last.commit.map.Interactable
import com.last.commit.map.TimeMap
import com.last.commit.stages.UIStage
import com.last.commit.stages.PromptStage
import kotlin.math.floor

/** First screen of the application. Displayed after the application is created. */
class FirstScreen(private val parent: Game) : TimeTravelScreen() {

    val gameState = parent.state

    val viewportSize = 1200f

    private var delta = 0.008f
    private var isColliding = false

    val batch = SpriteBatch()
    val viewport = FillViewport(viewportSize, viewportSize)
    val camera = OrthographicCamera(viewportSize, viewportSize)

    var map: TimeMap

    val playerTexture = Texture("sprites/characters.png")
    val player = Player(TextureRegion(playerTexture, 300, 44, 35, 43), gameState)
    var shapeRenderer = ShapeRenderer()

    val highlightColor = Color(0f, 0f, 1f, 0.5f)

    var uiStage: UIStage
    val promptStage: PromptStage


    init {
        val gameConfig = this.loadGameConfig()
        val randomMap = gameConfig.getRandomMap()
        map = TimeMap(randomMap, gameState)

        this.spawnPlayer()
        this.updateCamera()

        uiStage = UIStage("sprites/genericItems_spritesheet_colored", gameState)
        shapeRenderer.setAutoShapeType(true)

        promptStage = PromptStage(Skin(Gdx.files.internal("ui/uiskin.json")))
        promptStage.addText("""  
        You are stranded in time.
        The time traveling device you carry with you cannot be controlled anymore.
        """.trimIndent())
        promptStage.addText("""
        You need to find the tools required to fix your time traveling device.
        Look around the map to find them.
        """.trimIndent())
        promptStage.addText("""
        You can use <W>, <A>, <S> and <D> to walk around the map.
        Some doors require keys to be opened.
        """.trimIndent())
        promptStage.addText("""
        At some locations you can press <T> to travel to a random spot in time.
        The top left indicates where you traveled to.
        """.trimIndent())
        promptStage.addText("""
        Some items can only be collected in certain time locations.
        A collectible item will be highlighted when hovering over it using the mouse
        """.trimIndent())
        promptStage.visible = true

        viewport.camera = camera
        viewport.apply()
    }

    override fun getInputProcessors(): Array<InputProcessor> {
        return arrayOf(gameState.dialogStage, promptStage)
    }

    override fun handleKeyInput(action: ActionCommand) {

            when (action) {
                ActionCommand.INTERACT -> {
                    openDoor()
                }

                ActionCommand.TIME_TRAVEL -> {
                    map.teleport(player)
                }

                ActionCommand.JUMP -> {
                    gameState.dialogStage.setTexts("Hello", "Please read the documentation")
                    gameState.dialogStage.show()
                }

                else -> {}
            }
            if (action == ActionCommand.OPEN_MENU) {
                //Gdx.app.exit()
                parent.changeScreen(Screens.MAIN_MENU)
            }


    }

    override fun handleMouseInput(screenX: Int, screenY: Int, pointer: Int, button: Int) {

            val mouseCoordinates: Vector2 = toWorldCoordinates(screenX.toFloat(), screenY.toFloat())
            val playerDirection: Vector2 = player.getAbsoluteDirection()

            map.interactWith(playerDirection.x, playerDirection.y, player.getCollider())
    }

    override fun show() {


        resume()
    }

    fun loadGameConfig(): GameConfig {
        val jsonFileHandle = Gdx.files.local("config.json")
        val json = Json()

        return json.fromJson(GameConfig::class.java, jsonFileHandle)
    }

    override fun render(delta: Float) {
        if (gameState.inventory.checkVictoryCondition()) {
            promptStage.visible = true
            promptStage.act(delta)
            promptStage.clearText()
            promptStage.addText("You won!")
            promptStage.draw()
        } else {
            uiStage.act(delta)
            gameState.dialogStage.act(delta)
            promptStage.act(delta)

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

            uiStage.draw()
            gameState.dialogStage.draw()
            promptStage.draw()
            viewport.apply()
            updateCamera()
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

}
