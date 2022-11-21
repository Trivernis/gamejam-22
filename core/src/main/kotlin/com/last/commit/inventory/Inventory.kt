package com.last.commit.inventory

class Inventory() {

    val items: MutableList<InventoryItem> = ArrayList()
    var updated = false
    private val requiredItems = listOf("ram", "iphone", "ipod", "screwdriver")

    /**
     * @param name the name of the subtexture loaded from xml
     * @return wether the item was added
     */
    fun add(name: String) {
        items.add(InventoryItem(name))
        this.updated = true
    }

    fun hasItem(name: String): Boolean {
        return this.items.find { it.name == name } == null
    }

    fun isFull(): Boolean {
        return items.size >= 7
    }

    fun remove(name: String) {
        items.removeIf() { item -> item.name == name }
    }

    fun checkVictoryCondition(): Boolean {
        return requiredItems.all { itemName ->
            items.find { it.name == itemName } != null
        }
    }
}