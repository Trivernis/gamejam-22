package com.last.commit.stages

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import com.last.commit.inventory.Inventory
import com.last.commit.inventory.InventoryItemTextureLoader

class InventoryStage(path: String, val inventory: Inventory) : Stage(FitViewport(1200f, 1200f)) {
    val textureLoader = InventoryItemTextureLoader(path)
    var images: List<Image> = ArrayList()
    val batch = SpriteBatch()

    init {
        textureLoader.parse()
        this.refresh()
        this.setDebugAll(true)
    }

    fun refresh() {
        super.clear()
        inventory.items.forEachIndexed { index, inventoryItem ->
            val image = Image(textureLoader.getTexture(inventoryItem.name))
            image.x = index * 32f
            image.width = 32f
            image.height = 32f

            addActor(image)
        }
    }

    fun resize(width: Int, height: Int) {
        this.viewport.update(width, height, true)
    }
    
    override fun act() {
        this.refresh()
        super.act()
    }

    override fun draw() {
        super.draw()
    }
}