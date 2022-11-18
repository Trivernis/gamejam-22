package com.last.commit.inventory

class Inventory {

    val items: MutableList<InventoryItem> = ArrayList()
    val textureLoader = InventoryItemTextureLoader()

    init {
        textureLoader.parse()
    }

    fun add(name: String) {
        items.add(InventoryItem(name, textureLoader.loadTexture(name)))
    }
}