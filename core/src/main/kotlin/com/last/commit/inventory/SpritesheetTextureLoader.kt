package com.last.commit.inventory

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.XmlReader

/**
 * Parses textures from a png file with the corresponding xml mapping file
 */
class SpritesheetTextureLoader(path: String) {

    private val itemsSpriteSheet: Texture
    private val textureMapping: FileHandle
    private val subTextures = Array<XmlReader.Element>()
    private val textures: HashMap<String, TextureRegion> = HashMap()

    init {
        println("Loading textures from $path")
        itemsSpriteSheet = Texture("${path}.png")
        textureMapping = Gdx.files.local("${path}.xml")
        parse()
        println("Loaded ${subTextures.size} textures")
    }

    fun getTexture(itemName: String): TextureRegion {
        var itemTexture = textures[itemName]

        if (itemTexture == null) {
            val subTexture = subTextures.first { it.getAttribute("name") == itemName }
            val x = subTexture.getIntAttribute("x")
            val y = subTexture.getIntAttribute("y")
            val width = subTexture.getIntAttribute("width")
            val height = subTexture.getIntAttribute("height")
            itemTexture = TextureRegion(itemsSpriteSheet, x, y, width, height)
            this.textures[itemName] = itemTexture
        }
        return itemTexture
    }

    private fun parse() {
        val xml = XmlReader()
        val textureAtlasElement = xml.parse(textureMapping)
        this.subTextures.addAll(textureAtlasElement.getChildrenByName("SubTexture"))
    }
}
