package com.last.commit.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.last.commit.Game
import com.last.commit.TimeTravelAssetManager


class LoadingScreen(private val assetManager: TimeTravelAssetManager, private val game: Game) : Screen {

    companion object {
        private const val FONTS = 1
        private const val TEXTURES = 2
        private const val SKINS = 3
        private const val MUSICS = 4
        private const val SOUNDS = 5
        private const val FINISHED = 6
    }

    private var currentLoadingStage = 0
    private val layout = GlyphLayout()
    private var currentText = ""
    private val camera = OrthographicCamera()

    // timer for exiting loading screen
    private var countDown = 1f // 5 seconds of waiting before menu screen

    private val batch = SpriteBatch()

    init {
        camera.setToOrtho(false, 800f, 600f)
    }

    override fun show() {}
    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        if (assetManager.update()) {
            currentLoadingStage += 1
            when (currentLoadingStage) {
                TEXTURES -> {
                    currentText = "Loading textures"
                    assetManager.loadTextures()
                }

                SKINS -> {
                    currentText = "Loading skins"
                    assetManager.loadSkins()
                }

                MUSICS -> {
                    currentText = "Loading musics"
                    assetManager.loadMusics("sounds/music/world_music.mp3")
                }

                SOUNDS -> {
                    currentText = "Loading sounds"
                    assetManager.loadSounds(
                        "sounds/effects/steps/steps_indoor_1.mp3",
                        "sounds/effects/steps/steps_indoor_2.mp3",
                        "sounds/effects/steps/steps_indoor_3.mp3",
                        "sounds/effects/door_open.mp3",
                        "sounds/effects/door_close.mp3",
                        "sounds/effects/time_travel.mp3",
                        "sounds/effects/grab.mp3"
                    )
                }

                FINISHED -> currentText = "Finished"
                else -> currentText = "Key no found"
            }
            if (currentLoadingStage > 5) {
                countDown -= delta // timer to stay on loading screen for short preiod once done loading
                currentLoadingStage =
                    5 // cap loading stage to 5 as will use later to display progress bar anbd more than 5 would go off the screen
                if (countDown < 0) { // countdown is complete
//                    game.getAudioManager().load()
                    game.changeScreen(Screens.MAIN_MENU)
                }
            }
        }
        layout.setText(game.font, currentText)
        camera.update()
        batch.projectionMatrix = camera.combined
        batch.begin()
        game.font.draw(batch, currentText, 800 / 2 - layout.width / 2, layout.height + 10)
        batch.end()
    }

    override fun resize(width: Int, height: Int) {}
    override fun pause() {}
    override fun resume() {}
    override fun hide() {
        assetManager.finishLoading()
    }

    override fun dispose() {}


}