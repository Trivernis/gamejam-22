package com.last.commit.stages

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.last.commit.inventory.Inventory

class InventoryStage(val inventory: Inventory) : Stage() {

    var visible = false
        set(visible) {
            field = visible
            if (visible) {
                refresh()
            }
        }

    fun refresh() {
        super.clear()
        inventory.items.forEachIndexed { index, inventoryItem ->
            val image = Image(inventoryItem.texture)
            image.x = index * 32f
            image.width = 32f
            image.height = 32f

            addActor(image)
        }
    }

    fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun draw() {
        if (visible) {
            super.draw()
        }
    }
}