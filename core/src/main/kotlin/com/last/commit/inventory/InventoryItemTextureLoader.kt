package com.last.commit.inventory

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.XmlReader

class InventoryItemTextureLoader {

    private val itemsSpriteSheet = Texture("sprites/genericItems_spritesheet_colored.png")
    private lateinit var subTextures: Array<XmlReader.Element>

    fun loadTexture(itemName: String): TextureRegion {
        var subtexture = subTextures.first { it.getAttribute("name") == itemName }
        val x = subtexture.getIntAttribute("x")
        val y = subtexture.getIntAttribute("y")
        val width = subtexture.getIntAttribute("width")
        val height = subtexture.getIntAttribute("height")
        return TextureRegion(itemsSpriteSheet, x, y, width, height)
    }

    fun parse() {
        val xml = XmlReader()
        val textureAtlasElement = xml.parse(Gdx.files.local("sprites/genericItems_spritesheet_colored.xml"))
        this.subTextures = textureAtlasElement.getChildrenByName("SubTexture")
        println("Found ${subTextures.size} textures")
    }

}