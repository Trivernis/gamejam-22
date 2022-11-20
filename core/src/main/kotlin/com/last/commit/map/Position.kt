package com.last.commit.map

import com.badlogic.gdx.math.Vector2

public data class Position(
    public val pos: Vector2,
    public val gridPos: Vector2,
) {

    public val x: Float
        get() = pos.x

    public val y: Float
        get() = pos.y
        
    public val gridX: Int
        get() = gridPos.x.toInt()

    public val gridY: Int
        get() = gridPos.y.toInt()

    constructor(x: Float, y: Float, gridX: Int, gridY: Int): this(Vector2(x, y), Vector2(gridX.toFloat(), gridY.toFloat()))
}