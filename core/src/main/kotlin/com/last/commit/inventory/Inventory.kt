package com.last.commit.inventory

class Inventory {

    val items: MutableList<InventoryItem> = ArrayList()
    public var updated = false
    private set

    /**
     * @param name the name of the subtexture loaded from xml
     */
    fun add(name: String) {
        items.add(InventoryItem(name))
        this.updated = true
    }
    
    fun remove(name: String) {
        items.removeIf() {item -> item.name == name}
    }
}