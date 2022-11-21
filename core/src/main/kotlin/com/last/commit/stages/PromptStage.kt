package com.last.commit.stages

import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextArea
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.ExtendViewport

class PromptStage(skin: Skin?) : Stage(ExtendViewport(300f, 300f)) {

    private val textArea: TextArea;
    var visible = false
    private val text: ArrayList<String> = ArrayList()

    init {
        textArea = TextArea("", skin)
        textArea.setSize(viewport.worldWidth * 0.8f, viewport.worldHeight * 0.6f)
        textArea.setPosition(viewport.worldWidth * 0.1f, viewport.worldHeight * 0.2f)
        textArea.setAlignment(Align.center)

        this.addActor(textArea)
    }

    override fun act(delta: Float) {
        if (visible) {
            if (this.textArea.text.isEmpty()) {
                this.next()
            }
            super.act(delta)
        }
    }

    override fun draw() {
        if (visible) {
            this.viewport.apply()
            super.draw()
        }
    }

    fun clearText() {
        this.text.clear()
    }

    fun addText(text: String) {
        this.text.add(text)
    }

    fun resize(width: Int, height: Int) {
        this.viewport.update(width, height)
        this.camera.update()
    }

    override fun keyDown(keyCode: Int): Boolean {
        return if (!visible) {
            false
        } else if (keyCode == Input.Keys.SPACE) {
            next()
            true
        } else {
            false
        }
    }

    private operator fun next() {
        textArea.clear()

        if (text.isNotEmpty()) {
            val text = text.removeAt(0).trimIndent()
            textArea.text = text
        } else {
            textArea.text = ""
            visible = false
        }
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return keyDown(Input.Keys.SPACE)
    }
}