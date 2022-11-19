package com.last.commit.inventory

class Inventory(path: String) {

    val items: MutableList<InventoryItem> = ArrayList()
    val textureLoader = InventoryItemTextureLoader(path)

    init {
        textureLoader.parse()
    }

    /**
     * @param name the name of the subtexture loaded from xml
     */
    fun add(name: String) {
        items.add(InventoryItem(name, textureLoader.loadTexture(name)))
    }
}