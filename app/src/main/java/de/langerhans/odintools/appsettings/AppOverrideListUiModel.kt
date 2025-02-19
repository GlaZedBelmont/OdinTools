package de.langerhans.odintools.appsettings

import android.graphics.drawable.Drawable
import de.langerhans.odintools.models.ControllerStyle
import de.langerhans.odintools.models.L2R2Style

data class AppOverrideListUiModel(
    val deviceVersion: String = "",
    val showAppSelectDialog: Boolean = false,
    val overrideList: List<AppUiModel> = emptyList(),
    val overrideCandidates: List<AppUiModel> = emptyList()
)

data class AppOverridesUiModel(
    val deviceVersion: String = "",
    val app: AppUiModel? = null,

    val hasUnsavedChanges: Boolean = false,
    val showDeleteConfirmDialog: Boolean = false,
    val navigateBack: Boolean = false,
    val isNewApp: Boolean = false,
)

data class AppUiModel(
    val packageName: String,
    val appName: String,
    val appIcon: Drawable,
    val subtitle: String? = null,
    val controllerStyle: ControllerStyle? = null,
    val l2r2Style: L2R2Style? = null
)