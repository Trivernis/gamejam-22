import com.last.commit.inventory.Inventory
import com.last.commit.config.GameSettings

data class GameState(
    val inventory: Inventory,
    val settings: GameSettings,
)