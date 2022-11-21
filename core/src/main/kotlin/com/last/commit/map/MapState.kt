package com.last.commit.map

import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.maps.objects.RectangleMapObject
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.last.commit.Wall
import com.last.commit.inventory.SpritesheetTextureLoader
import kotlin.math.round

class MapState(val map: TiledMap, val textureLoader: SpritesheetTextureLoader) {
    private val CELL_SIZE = 64

    val size: Vector2
    val gridSize: Vector2
    val tileSize: Vector2
    val description: String?

    val collectibles: ArrayList<Collectible> = ArrayList()
    val teleporters: ArrayList<RectangleMapObject> = ArrayList()
    val walls: ArrayList<Wall> = ArrayList()
    val doors: ArrayList<Door> = ArrayList()

    init {
        val prop = map.properties
        val gridWidth = prop.get("width", Int::class.java)
        val gridHeight = prop.get("height", Int::class.java)
        gridSize = Vector2(gridWidth.toFloat(), gridHeight.toFloat())
        description = prop.get("description", String::class.java)

        val width = gridWidth * CELL_SIZE
        val height = gridHeight * CELL_SIZE
        size = Vector2(width.toFloat(), height.toFloat())

        val tileWidth = map.properties.get("tilewidth", Int::class.java)
        val tileHeight = map.properties.get("tileheight", Int::class.java)
        tileSize = Vector2(tileWidth.toFloat(), tileHeight.toFloat())
        this.loadCollectibles()
        this.loadWalls()
        for (obj in map.layers["Teleporter"].objects) {
            if (obj is RectangleMapObject) {
                this.teleporters.add(obj)
                println("Teleporter ${obj}")
            }
        }
        if (this.teleporters.isEmpty()) {
            println("No Teleporters defined!")
        }
    }

    private fun loadCollectibles() {
        val collectiableLayer = map.layers["Collectibles"]
        if (collectiableLayer == null) {
            println("Could not load collectibles layer. Check map.")
            return
        }

        collectiableLayer.objects.mapNotNullTo(collectibles) { createCollectible(it) }

        println("Loaded ${collectibles.size} collectibles")
    }

    private fun createCollectible(obj: MapObject): Collectible? {
        val x = obj.properties.get("x", Float::class.java)
        val y = obj.properties.get("y", Float::class.java)
        val coords = Vector2(x, y)
        val gridCoords = Vector2(round(x / tileSize.x), round(y / tileSize.y))

        val width = obj.properties.get("width", Float::class.java)
        val height = obj.properties.get("height", Float::class.java)
        val size = Vector2(width, height)

        return if (obj is RectangleMapObject) {
            val itemName: String? = obj.properties.get("item", String::class.java)
            val requiredItem = obj.properties.get("requiredItem", String::class.java) ?: ""

            if (itemName != null) {
                val image = Image(textureLoader.getTexture(itemName))
                image.x = coords.x + tileSize.x * 0.1f
                image.y = coords.y + tileSize.y * 0.1f
                image.width = tileSize.x * 0.8f
                image.height = tileSize.y * 0.8f
                Collectible(itemName, Position(coords, gridCoords), size, requiredItem, image)
            } else {
                null
            }
        } else {
            null
        }
    }

    private fun loadWalls() {
        walls.clear()
        doors.clear()
        val wallsLayer = map.layers["Walls"] as TiledMapTileLayer

        for (column in 0 until wallsLayer.width) {
            for (row in 0 until wallsLayer.height) {
                val cell = wallsLayer.getCell(column, row) ?: continue
                val isDoor: Boolean =
                    cell.getTile().getProperties().get("isDoor", false, Boolean::class.java)

                val wallCollider =
                    Rectangle(
                        column.toFloat() * wallsLayer.tileWidth,
                        row.toFloat() * wallsLayer.tileHeight,
                        wallsLayer.tileWidth.toFloat(),
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
