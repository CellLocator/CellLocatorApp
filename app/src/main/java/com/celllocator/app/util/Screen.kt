import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CellTower
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.celllocator.app.R

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    data object Cells : Screen("cellinfo", R.string.menu_cells, Icons.Rounded.CellTower)

    data object Settings : Screen("settings", R.string.menu_settings, Icons.Rounded.Settings)
}