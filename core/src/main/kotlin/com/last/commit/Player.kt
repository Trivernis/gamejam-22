package com.last.commit

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.last.commit.inventory.Inventory


class Player(private val textureRegion: TextureRegion) : Collidable {

    private var collider: Rectangle = Rectangle(0f, 0f, 0f, 0f)
    var position: Vector2 = Vector2.Zero
    private var direction = Vector2.Zero
    private val movementSpeed = 200f
    private val interactionRange = 60f

    val inventory = Inventory()

    init {
        val size = Math.max(textureRegion.regionWidth, textureRegion.regionHeight).toFloat()
        collider = Rectangle(0f, 0f, size, size)
        position = Vector2()
    }

    fun addItemToInventory(name: String) {
        this.inventory.add(name)
    }

    fun getX(): Float {
        return position.x
    }

    fun getY(): Float {
        return position.y
    }

    fun move(v: Vector2, delta: Float) {
        updatePosition(v, delta)
        updateCollider()
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

    fun setPosition(x: Float, y: Float) {
        position[x] = y
    }

    fun getWidth(): Float {
        return collider.getWidth()
    }

    fun getHeight(): Float {
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
        return getWidth() / 2
    }

    private fun getHalfPlayerHeight(): Float {
        return getHeight() / 2
    }

    fun getRotation(): Float {
        return direction.angleDeg()
    }

    fun render(batch: SpriteBatch) {
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