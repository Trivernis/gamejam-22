package com.last.commit.map

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.last.commit.Collidable
import com.last.commit.Wall


class TimeMap(fileName: String) {
    private val CELL_SIZE = 64

    private val walls: Array<Wall> = Array()
    val mapLoader:TmxMapLoader = TmxMapLoader()
    var mapRenderer: OrthogonalTiledMapRenderer
    val map: TiledMap
    var gridWidth = 0
    var gridHeight = 0
    var width = 0
    var height = 0
    var mapTileWidth = 0
    var mapTileHeight = 0
    var halfMapTileWidth = 0f
    var halfMapTileHeight = 0f


    init {
        map = mapLoader.load(fileName)
        mapRenderer = OrthogonalTiledMapRenderer(map)
        loadDimensions()
        loadWalls()
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
        this.width = gridWidth * CELL_SIZE
        this.height = gridHeight * CELL_SIZE
        this.mapTileWidth = map.properties.get("tilewidth", Int::class.java)
        this.mapTileHeight = map.properties.get("tileheight", Int::class.java)
        this.halfMapTileWidth = mapTileWidth / 2f
        this.halfMapTileHeight = mapTileHeight / 2f
    }

    private fun loadWalls() {
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
                    val wall: Wall
                    if (java.lang.Boolean.TRUE == isDoor) {
                        wall = Door(column, row, wallCollider, cell)
                    } else {
                        wall = Wall(column, row, wallCollider, cell)
                    }
                    walls.add(wall)
                }
            }
        }
    }

    private fun findDoorByGridPosition(gridX: Int, gridY: Int): Door? {
        for (wall in walls) {
            if (wall.gridX == gridX && wall.gridY == gridY && wall is Door) {
                return wall
            }
        }
        return null
    }

    fun toggleDoorAt(x: Float, y: Float, blockingCollider: Rectangle?) {
        val gridX = x.toInt() / CELL_SIZE
        val gridY = y.toInt() / CELL_SIZE
        println("Toggling door at $gridX:$gridY")
        val door: Door = this.findDoorByGridPosition(gridX, gridY) ?: return
        if (door.isClosed) {
            door.isOpen = true
        } else if (door.isOpen) {
            if (door.getCollider().overlaps(blockingCollider)) {
                // can't close the door cause it is colliding with given collider
            } else {
                door.isOpen = false
            }
        }
        println("Door is now open = ${door.isOpen}")
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
    }

    fun isCollidingWithWall(collidable: Collidable): Boolean {
        for (wall in walls) {
            if (wall.collidesWidth(collidable)) {
                return true
            }
        }
        return false
    }

}