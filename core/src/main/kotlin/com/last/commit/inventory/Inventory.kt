package com.last.commit.inventory

import com.last.commit.GameState

class Inventory() {

    val items: MutableList<InventoryItem> = ArrayList()
    public var updated = false

    /**
     * @param name the name of the subtexture loaded from xml
     * @return wether the item was added
     */
    fun add(name: String, state: GameState): Boolean {
        if (this.items.find { it.name == name } == null) {
            if (this.items.size < 8) {
                items.add(InventoryItem(name))
                this.updated = true

                return true
            }
        } else {
            // Item is already in inventory
            state.dialogStage.setTexts("You already have this item.")
            state.dialogStage.show()
        }
        return false
    }

    fun remove(name: String) {
        items.removeIf() { item -> item.name == name }
    }
}