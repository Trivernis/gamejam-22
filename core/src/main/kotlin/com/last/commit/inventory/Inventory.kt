package com.last.commit.inventory

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.graphics.g2d.Batch

class Inventory: Actor() {

    val textureLoader = InventoryItemTextureLoader("sprites/genericItems_spritesheet_colored")
    val items: MutableList<InventoryItem> = ArrayList()
    public var updated = false
    private set

    override fun draw(batch: Batch, delta: Float) {
        items.mapIndexed { index, inventoryItem ->
            val image = Image(textureLoader.getTexture(inventoryItem.name))
            image.x = index * 32f
            image.width = 32f
            image.height = 32f

            image
        }.forEach{image -> image.draw(batch, 1f)}
    }

    /**
     * @param name the name of the subtexture loaded from xml
     */
    fun add(name: String) {
        items.add(InventoryItem(name))
        this.updated = true
    }
    
    fun remove(name: String) {
        items.removeIf() {item -> item.name == name}
    }
    
}