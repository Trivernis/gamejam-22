import com.last.commit.inventory.Inventory
import com.last.commit.config.GameSettings
import com.last.commit.audio.SoundEngine

data class GameState(
    val inventory: Inventory,
    val settings: GameSettings,
    val soundEngine: SoundEngine,
    var mapDescription: String = "2020"
)