import com.last.commit.inventory.Inventory
import com.last.commit.config.GameSettings
import com.last.commit.audio.SoundEngine
import com.last.commit.config.TimeTravelAssetManager
import com.last.commit.map.MapState

data class GameState(
    val inventory: Inventory,
    val settings: GameSettings,
    val soundEngine: SoundEngine,
    val assetManager: TimeTravelAssetManager,
    var map: MapState? = null
)