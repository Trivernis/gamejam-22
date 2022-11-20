package com.last.commit.stages

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.last.commit.GameState
import com.last.commit.inventory.InventoryItemTextureLoader

class UIStage(path: String, val state: GameState) : Stage(ExtendViewport(512f, 512f)) {
    val textureLoader = InventoryItemTextureLoader(path)
    private val labelStyle = Label.LabelStyle(BitmapFont(), Color.BLACK)
    var mapLabel = Label("unknown time", labelStyle)
    var fpsLabel = Label("0", labelStyle)
    private var lastFpsUpdate = 0L

    init {
        textureLoader.parse()
    }

    fun refresh() {
        super.clear()
        state.inventory.items.forEachIndexed { index, inventoryItem ->
            val image = Image(textureLoader.getTexture(inventoryItem.name))
            image.x = index * 32f
            image.width = 32f
            image.height = 32f

            addActor(image)
        }
    }

    fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun act(delta: Float) {
        if (state.inventory.updated) {
            this.refresh()
            state.inventory.updated = false
        }
        this.addFpsLabel(delta)
        this.addMapDescriptionLabel()
    }

    override fun draw() {
        this.viewport.apply()
        super.draw()
    }

    private fun addFpsLabel(delta: Float) {
        if (System.currentTimeMillis() - lastFpsUpdate > 500) {
            actors.removeValue(this.fpsLabel, true)
            fpsLabel = Label((1 / delta).toInt().toString(), labelStyle)
            fpsLabel.x = this.viewport.worldWidth - fpsLabel.width
            fpsLabel.y = this.viewport.worldHeight - fpsLabel.height
            addActor(fpsLabel)
            lastFpsUpdate = System.currentTimeMillis()
        }
    }

    private fun addMapDescriptionLabel() {
        if (state.map != null) {
            this.actors.removeValue(this.mapLabel, true)
            this.mapLabel = Label(state.map!!.description, labelStyle)
            mapLabel.x = 0f
            mapLabel.y = this.viewport.worldHeight - mapLabel.height
            addActor(mapLabel)
        }
    }
}
