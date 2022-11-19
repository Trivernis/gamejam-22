package com.last.commit

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor
import com.last.commit.inventory.Inventory
import com.last.commit.audio.GameSoundEffect
import GameState


class Player(private val textureRegion: TextureRegion, private val gameState: GameState) : Collidable, Actor() {

    private var collider: Rectangle = Rectangle(0f, 0f, 0f, 0f)
    var position: Vector2 = Vector2.Zero
    private var direction = Vector2.Zero
    private val movementSpeed = 200f
    private val interactionRange = 60f
    private var lastStep = 0L
    val batch = SpriteBatch()
    val renderer = ShapeRenderer()

    init {
        val size = Math.max(textureRegion.regionWidth, textureRegion.regionHeight).toFloat()
        collider = Rectangle(0f, 0f, size, size)
    }

    fun addItemToInventory(name: String) {
        gameState.inventory.add(name)
    }

    override fun getX(): Float {
        return position.x
    }

    override fun getY(): Float {
        return position.y
    }

    fun move(v: Vector2, delta: Float) {
        if (v.x != 0f || v.y != 0f) {   
            updatePosition(v, delta)
            updateCollider()

            if (System.currentTimeMillis() - lastStep > 500) {
                gameState.soundEngine.play(GameSoundEffect.STEPS_INDOOR, 0.5f)
                lastStep = System.currentTimeMillis()
            }
        }
    }

    private fun updatePosition(v: Vector2, delta: Float) {
        position.add(v.setLength(movementSpeed * delta))
    }

    private fun updateCollider() {
        this.collider.x = position.x
        this.collider.y = position.y
    }

    override fun getCollider(): Rectangle {
        return collider
    }

    override fun setPosition(x: Float, y: Float) {
        position[x] = y
    }

    override fun getWidth(): Float {
        return collider.getWidth()
    }

    override fun getHeight(): Float {
        return collider.getHeight()
    }

    fun getSize(): Float {
        return Math.max(getWidth(), getHeight())
    }

    fun lookAt(position: Vector2) {
        calculateRotationTo(position)
    }

    private fun calculateRotationTo(position: Vector2) {
        val playerPosition = this.position.cpy()
        playerPosition.add(collider.width / 2f, collider.height / 2f)
        direction = position.cpy()
        direction.sub(playerPosition)
        direction.setLength(interactionRange)
    }

    /**
     * Returns the direction relative to the players center.
     */
    fun getRelativeDirection(): Vector2 {
        return direction.cpy().add(getHalfPlayerWidth(), getHalfPlayerHeight())
    }

    /**
     * Returns the direction of the player in World positon considerung the interactionRange
     */
    fun getAbsoluteDirection(): Vector2 {
        return position.cpy().add(getRelativeDirection())
    }


    private fun getHalfPlayerWidth(): Float {
        return getWidth()/ 2
    }

    private fun getHalfPlayerHeight(): Float {
        return getHeight() / 2
    }

    override fun getRotation(): Float {
        return direction.angleDeg()
    }
    
    override fun draw(batch: Batch, parentAlpha: Float) {
        val halfPlayerWidth: Float = getHalfPlayerWidth() // TODO maybe use collider
        // dimensions
        val halfPlayerHeight: Float = getHalfPlayerHeight() // TODO maybe use collider
        // dimensions
        
        
        batch.draw(
            textureRegion, getX(), getY(),
            halfPlayerWidth, halfPlayerHeight, getWidth(),
            getHeight(), 1f, 1f,
            getRotation()
        )
    }
}