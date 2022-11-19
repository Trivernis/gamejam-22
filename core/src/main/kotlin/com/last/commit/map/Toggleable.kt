package com.last.commit.map

interface Toggleable : Interactable {
    fun toggle()

    override fun interact() {
        toggle()
    }
}