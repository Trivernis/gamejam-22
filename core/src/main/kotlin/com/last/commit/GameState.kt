import com.last.commit.inventory.Inventory
import com.last.commit.config.PlayerSettings

public data class GameState(
    public val inventory: Inventory,
    public val settings: PlayerSettings,
)