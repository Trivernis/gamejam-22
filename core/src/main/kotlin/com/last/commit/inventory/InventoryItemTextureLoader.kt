package com.last.commit.inventory

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.XmlReader

class InventoryItemTextureLoader(path: String) {

    private val itemsSpriteSheet: Texture
    private val textureMapping: FileHandle
    private lateinit var subTextures: Array<XmlReader.Element>
    private val textures: HashMap<String, TextureRegion> = HashMap()
    private var initialized: Boolean = false

    init {
        itemsSpriteSheet = Texture("${path}.png")
        textureMapping = Gdx.files.local("${path}.xml")
    }

    fun getTexture(itemName: String): TextureRegion {
        if (!initialized) {
            this.parse()
        }
        var itemTexture = textures.get(itemName)

        if (itemTexture == null) {
            var subtexture = subTextures.first { it.getAttribute("name") == itemName }
            val x = subtexture.getIntAttribute("x")
            val y = subtexture.getIntAttribute("y")
            val width = subtexture.getIntAttribute("width")
            val height = subtexture.getIntAttribute("height")
            itemTexture = TextureRegion(itemsSpriteSheet, x, y, width, height)
            this.textures.set(itemName, itemTexture)
        }
        return itemTexture
    }

    fun parse() {
        val xml = XmlReader()
        val textureAtlasElement = xml.parse(textureMapping)
        this.subTextures = textureAtlasElement.getChildrenByName("SubTexture")
        println("Found ${subTextures.size} textures")
        this.initialized = true
    }
}
