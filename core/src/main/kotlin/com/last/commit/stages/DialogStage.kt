package com.last.commit.stages

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextArea
import com.badlogic.gdx.utils.viewport.ExtendViewport


/**
 * Stage for dialog
 */
class DialogStage(skin: Skin?) : Stage(ExtendViewport(512f, 512f)) {
    private var isVisible = false
    private val texts = com.badlogic.gdx.utils.Array<String>()
    private val area: TextArea

    init {
        area = TextArea("#", skin)
        area.width = viewport.worldWidth
        area.height = 100f
        addActor(area)
    }

    fun setTexts(vararg texts: String?) {
        this.texts.clear()
        this.texts.addAll(*texts)
        next()
    }

    fun show() {
        isVisible = true
    }

    fun hide() {
        isVisible = false
    }

    fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        area.width = viewport.worldWidth
        this.camera.update()
    }

    override fun draw() {
        if (isVisible) {
            this.viewport.apply()
            super.draw()
        }
    }

    override fun act() {
        if (isVisible) {
            super.act()
        }
    }

    override fun act(delta: Float) {
        if (isVisible) {
            super.act(delta)
        }
    }

    override fun keyDown(keyCode: Int): Boolean {
        if (!isVisible) {
            return false
        }
        if (keyCode == Input.Keys.SPACE) {
            if (texts.size > 0) {
                //set next dialog text
                next()
            } else {
                // hide dialog area cause there is no more text to show
                hide()
            }
        }
        return true
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        return keyDown(Input.Keys.SPACE)
    }

    private operator fun next() {
        area.clear()
        area.text = texts.first()
        texts.removeIndex(0)
    }

}