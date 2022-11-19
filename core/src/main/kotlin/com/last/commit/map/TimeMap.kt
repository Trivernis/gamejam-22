package com.last.commit.map

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.last.commit.Collidable
import com.last.commit.Player
import com.last.commit.Wall
import com.last.commit.audio.GameSoundEffect
import com.last.commit.inventory.InventoryItemTextureLoader
import Position
import GameState


class TimeMap(fileName: String, val state: GameState) {
    private val CELL_SIZE = 64
    val textureLoader = InventoryItemTextureLoader("sprites/genericItems_spritesheet_colored")

    private val walls = Array<Wall>()
    private val doors = Array<Door>()
    private val collectibles = Array<Collectible>()
    val mapLoader: TmxMapLoader = TmxMapLoader()
    var mapRenderer: OrthogonalTiledMapRenderer
    var map: TiledMap
    var gridWidth = 0
    var gridHeight = 0
    var width = 0
    var height = 0
    var mapTileWidth = 0
    var mapTileHeight = 0
    var halfMapTileWidth = 0f
    var halfMapTileHeight = 0f
    var description: String = "2020"
        private set


    init {
        map = mapLoader.load(fileName)
        mapRenderer = OrthogonalTiledMapRenderer(map)
        loadDimensions()
        loadWalls()
        loadCollectibles()
        this.textureLoader.parse()
    }

    fun teleport(player: Player) {
        val teleporters = map.layers["Teleporter"].objects
        for (teleporter in teleporters) {
            if (teleporter is RectangleMapObject) {
                if (teleporter.rectangle.contains(player.getX(), player.getY())) {
                    state.soundEngine.play(GameSoundEffect.TIME_TRAVEL)
                    val targetMap = teleporter.properties.get("target", String::class.java)
                    System.out.println("Teleporting to targetMap $targetMap")
                    map = mapLoader.load("tiled/$targetMap")
                    mapRenderer.map = map
                    loadDimensions()
                    loadWalls()
                    loadCollectibles()
                }
            } else {
                println("Found illegal teleporter. ${teleporter.properties.get("id")}")
            }
        }
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

    private fun loadDimensions() {
        val prop = map.properties
        this.gridWidth = prop.get("width", Int::class.java)
        this.gridHeight = prop.get("height", Int::class.java)

        if (prop.containsKey("description")) {
            this.description = prop.get("description", String::class.java)
        }  else {
            this.description = "Unknown time"
        }
        this.state.mapDescription = this.description
        this.width = gridWidth * CELL_SIZE
        this.height = gridHeight * CELL_SIZE
        this.mapTileWidth = map.properties.get("tilewidth", Int::class.java)
        this.mapTileHeight = map.properties.get("tileheight", Int::class.java)
        this.halfMapTileWidth = mapTileWidth / 2f
        this.halfMapTileHeight = mapTileHeight / 2f
    }

    private fun loadWalls() {
        walls.clear()
        doors.clear()
        val wallsLayer = map.layers["Walls"] as TiledMapTileLayer

        for (column in 0 until wallsLayer.width) {
            for (row in 0 until wallsLayer.height) {
                val cell: TiledMapTileLayer.Cell? = wallsLayer.getCell(column, row)
                if (cell != null) {
                    val isDoor: Boolean = cell.getTile().getProperties().get("isDoor", false, Boolean::class.java)
                    val wallCollider = Rectangle(
                        column.toFloat() * wallsLayer.tileWidth,
                        row.toFloat() * wallsLayer.tileHeight, wallsLayer.tileWidth.toFloat(),
                        wallsLayer.tileHeight.toFloat()
                    )
                    if (java.lang.Boolean.TRUE == isDoor) {
                        doors.add(Door(column, row, wallCollider, cell))
                    } else {
                        walls.add(Wall(column, row, wallCollider, cell))
                    }
                }
            }
        }
    }

    fun loadCollectibles() {
        this.collectibles.clear()
        val collectiableLayer = map.layers["Collectibles"]
        if (collectiableLayer == null) {
            println("Could not load collectibles layer. Check map.")
            return
        }
        val collectibleMapObjects = collectiableLayer.objects

        for (mapObject in collectibleMapObjects) {
            val mapObjectProperties = mapObject.properties
            val x = mapObjectProperties.get("x", Float::class.java)
            val gridX = Math.round(x / mapTileWidth)
            val y = mapObjectProperties.get("y", Float::class.java)
            val gridY = Math.round(y / mapTileHeight)
            val width = mapObjectProperties.get("width", Float::class.java)
            val height = mapObjectProperties.get("height", Float::class.java)

            if (mapObject is RectangleMapObject) {
                val itemName = mapObjectProperties.get("item", String::class.java)
                itemName?. let {
                    this.collectibles.add(Collectible(itemName, Position(x, y, gridX, gridY), width, height))
                }
            } else {
                println("Found non-rectangular map object at ${x}-${y} skipping it")
            }
        }
        println("Loaded ${collectibles.size} collectibles")
    }

    private fun findInteractableAtPosition(gridX: Int, gridY: Int): Interactable? {
        for (door in doors) {
            if (door.gridX == gridX && door.gridY == gridY && door is Door) {
                return door
            }
        }
        for (collectible in collectibles) {
            if (collectible.pos.gridX == gridX && collectible.pos.gridY == gridY) {
                return collectible
            }
        }
        return null
    }

    fun interactWith(x: Float, y: Float, blockingCollider: Rectangle) {
        val gridX = x.toInt() / CELL_SIZE
        val gridY = y.toInt() / CELL_SIZE
        println("Interacting with element at $gridX:$gridY")

        //if no door is found return
        val interactable: Interactable = this.findInteractableAtPosition(gridX, gridY) ?: return
        //else continue

        interactable.interact(blockingCollider, state)
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
        this.collectibles.forEach { coll -> 
            val image = Image(textureLoader.getTexture(coll.name))
            image.x = coll.pos.x + this.getTileWidth() * 0.1f
            image.y = coll.pos.y + this.getTileHeight() * 0.1f
            image.width = this.getTileWidth() * 0.8f
            image.height = this.getTileHeight() * 0.8f
            image.draw(batch, 1f)
        }
    }

    fun isCollidingWith(collidable: Collidable): Boolean {

        for (wall in walls) {
            if (wall.collidesWidth(collidable)) {
                return true
            }
        }
        for (door in doors) {
            if (door.collidesWidth(collidable)) {
                return true
            }
        }
        return false
    }

    fun getInteractablesAt(absoluteDirection: Vector2): List<Interactable> {
        val interactables = ArrayList<Interactable>()
        val c = collectibles.filter { it.getCollider().contains(absoluteDirection) }
        interactables.addAll(c)
        val w = doors.filter { it.getCollider().contains(absoluteDirection) }
        interactables.addAll(w)

        return interactables
    }

}