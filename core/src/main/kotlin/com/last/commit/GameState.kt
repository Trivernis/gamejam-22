package com.last.commit

import com.last.commit.inventory.Inventory
import com.last.commit.map.MapState
import com.last.commit.stages.DialogStage

data class GameState(
    val inventory: Inventory,
    var map: MapState? = null,
    val dialogStage: DialogStage
) {
}