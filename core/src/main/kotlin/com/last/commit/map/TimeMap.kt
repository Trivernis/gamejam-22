package com.last.commit.map

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.last.commit.Collidable
import com.last.commit.GameState
import com.last.commit.Player
import com.last.commit.audio.GameSoundEffect
import com.last.commit.inventory.InventoryItemTextureLoader


class TimeMap(fileName: String, val state: GameState) {
    private val CELL_SIZE = 64
    val textureLoader = InventoryItemTextureLoader("sprites/genericItems_spritesheet_colored")

    val mapLoader: TmxMapLoader = TmxMapLoader()
    var mapRenderer: OrthogonalTiledMapRenderer
    var mapState: MapState
    val mapStates: HashMap<String, MapState> = HashMap()
    var map: TiledMap

    val gridWidth: Int
        get() = mapState.gridSize.x.toInt()

    val gridHeight: Int
        get() = mapState.gridSize.y.toInt()

    val width: Int
        get() = mapState.size.x.toInt()

    val height: Int
        get() = mapState.size.y.toInt()

    val mapTileWidth: Int
        get() = mapState.tileSize.x.toInt()

    val mapTileHeight: Int
        get() = mapState.tileSize.y.toInt()

    init {
        map = mapLoader.load(fileName)
        mapState = MapState(map, textureLoader)
        state.map = mapState
        mapStates[fileName] = mapState
        mapRenderer = OrthogonalTiledMapRenderer(map)
    }


    fun teleport(player: Player) {
        val teleporter = mapState.teleporters.find {
            it.rectangle.contains(player.getX(), player.getY())
        }
        if (teleporter != null) {
            state.soundEngine.play(GameSoundEffect.TIME_TRAVEL)
            val targetMap = teleporter.properties.get("target", String::class.java)
            System.out.println("Teleporting to targetMap $targetMap")
            loadMap("tiled/$targetMap")
            val mapDescription = mapState.description
            state.dialogStage.setTexts(4000L, "You teleported to $mapDescription")
            state.dialogStage.show()
        }
    }

    private fun loadMap(name: String) {
        val newState = this.mapStates.get(name)
        if (newState != null) {
            mapState = newState
            mapRenderer.map = mapState.map
        } else {
            val map = mapLoader.load(name)
            mapRenderer.map = map
            mapState = MapState(map, textureLoader)
            mapStates[name] = mapState
        }
        state.map = mapState
    }

    fun getPlayerSpawn(): Vector2 {
        val mapLayers = map.layers
        val spawnPoints = mapLayers["Spawnpoints"].objects

        for (spawnPoint in spawnPoints) {
            val spawnPointProperties = spawnPoint.properties
            if (spawnPointProperties != null && spawnPointProperties.containsKey("playerSpawn")) {
                val isPlayerSpawn = spawnPointProperties.get("playerSpawn", Boolean::class.java)
                if (java.lang.Boolean.TRUE == isPlayerSpawn) {
                    val x = spawnPointProperties.get("x", Float::class.java)
                    val y = spawnPointProperties.get("y", Float::class.java)
                    return Vector2(x, y)
                }
            }
        }
        Gdx.app.debug("SurvislandMap", "No spawn point found in map. Return map origin.")
        return Vector2.Zero
    }

    private fun findInteractableAtPosition(gridX: Int, gridY: Int): Interactable? {
        for (door in mapState.doors) {
            if (door.gridX == gridX && door.gridY == gridY) {
                return door
            }
        }
        for (collectible in mapState.collectibles) {
            if (collectible.pos.gridX == gridX && collectible.pos.gridY == gridY) {
                return collectible
            }
        }
        return null
    }

    fun interactWith(x: Float, y: Float, blockingCollider: Rectangle) {
        val gridX = x.toInt() / CELL_SIZE
        val gridY = y.toInt() / CELL_SIZE

        //if no door is found return
        val interactable: Interactable = this.findInteractableAtPosition(gridX, gridY) ?: return
        //else continue

        if (interactable.canInteract(state)) {
            println("Interacting with element at $gridX:$gridY")
            interactable.interact(blockingCollider, state)
        } else {
            println("Cannot interact with $gridX:$gridY")
        }
    }


    fun getTileWidth(): Float {
        return mapTileWidth.toFloat()
    }

    fun getTileHeight(): Float {
        return mapTileHeight.toFloat()
    }

    fun render(batch: SpriteBatch, camera: OrthographicCamera, delta: Float) {
        mapRenderer.setView(camera)
        mapRenderer.render()
        this.mapState.collectibles.forEach { coll ->
            val image = Image(textureLoader.getTexture(coll.name))
            image.x = coll.pos.x + this.getTileWidth() * 0.1f
            image.y = coll.pos.y + this.getTileHeight() * 0.1f
            image.width = this.getTileWidth() * 0.8f
            image.height = this.getTileHeight() * 0.8f
            image.draw(batch, 1f)
        }
    }

    fun isCollidingWith(collidable: Collidable): Boolean {

        for (wall in mapState.walls) {
            if (wall.collidesWidth(collidable)) {
                return true
            }
        }
        for (door in mapState.doors) {
            if (door.collidesWidth(collidable)) {
                return true
            }
        }
        return false
    }

    fun getInteractablesAt(absoluteDirection: Vector2): List<Interactable> {
        val interactables = ArrayList<Interactable>()
        val c = mapState.collectibles.filter { it.getCollider().contains(absoluteDirection) }
        interactables.addAll(c)
        val w = mapState.doors.filter { it.getCollider().contains(absoluteDirection) }
        interactables.addAll(w)

        return interactables
    }

}