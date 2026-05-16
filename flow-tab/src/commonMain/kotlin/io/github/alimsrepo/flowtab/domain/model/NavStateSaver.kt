package io.github.alimsrepo.flowtab.domain.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver

/**
 * State saver for [NavState] to support process death and recreation.
 *
 * Converts [NavState] to a serializable map for saving and restores it
 * from the saved map. Used internally with [rememberSaveable] to maintain
 * navigation state across configuration changes and process death.
 *
 * This is an internal implementation detail of the navigation component.
 */
internal val NavStateSaver = Saver<MutableState<NavState>, Map<String, Any?>>(
    save = { state ->
        mapOf(
            "selectedId" to state.value.selectedId,
            "returnToId" to state.value.returnToId,
            "searchQuery" to state.value.searchQuery,
            "isSearchExpanded" to state.value.isSearchExpanded
        )
    },
    restore = { saved ->
        mutableStateOf(
            NavState(
                selectedId = saved["selectedId"] as String,
                returnToId = saved["returnToId"] as? String ?: saved["selectedId"] as String,
                searchQuery = saved["searchQuery"] as? String ?: "",
                isSearchExpanded = saved["isSearchExpanded"] as? Boolean ?: false
            )
        )
    }
)