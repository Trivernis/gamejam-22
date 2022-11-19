import com.last.commit.inventory.Inventory
import com.last.commit.config.GameSettings
import com.last.commit.config.GameConfig
import com.last.commit.audio.SoundEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Json

class GameState(
    val inventory: Inventory,
    val settings: GameSettings,
    val soundEngine: SoundEngine,
) {
    val gameConfig: GameConfig
        get() = loadGameConfig()
            
    fun loadGameConfig(): GameConfig {
        val jsonFileHandle = Gdx.files.local("config.json")
        val json = Json()

        return json.fromJson(GameConfig::class.java, jsonFileHandle)
    }
}