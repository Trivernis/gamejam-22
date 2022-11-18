package com.last.commit.stages

import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.last.commit.inventory.Inventory

class InventoryStage(inventory: Inventory) : Stage() {

    var visible = false

    init {
        for (item in inventory.items) {

            val image = Image(item.texture)
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