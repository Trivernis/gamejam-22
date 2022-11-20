package com.last.commit;

import com.badlogic.gdx.math.MathUtils

class ColorState {
    public var red = 0f
    private set;
    
    public var green = 0f
    private set;

    public var blue = 1f
    private set;

    private var state = 0
    
    public fun step(delta: Long) {
        val diff = delta / 5000f

        when (state) {
            0 -> {
                blue -= diff;
                red += diff
                if (red >= 1f) {
                    state = 1
                }
            }
            1 -> {
                red -= diff
                green += diff
                if (green >= 1f) {
                    state = 2
                }
            }
            2 -> {
                green -= diff
                blue += diff
                if (blue >= 1f) {
                    state = 0
                }
            }
        }
    }
    
}