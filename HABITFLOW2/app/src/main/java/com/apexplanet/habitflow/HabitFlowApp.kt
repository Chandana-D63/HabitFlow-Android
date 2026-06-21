package com.apexplanet.habitflow

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.ViewModelStoreNavContent
import androidx.navigation3.NavBackStack
import androidx.navigation3.NavDisplay
import androidx.navigation3.rememberNavBackStack
import com.apexplanet.habitflow.data.repository.HabitRepository
import com.apexplanet.habitflow.ui.screens.AddHabitScreen
import com.apexplanet.habitflow.ui.screens.HabitListScreen
import com.apexplanet.habitflow.ui.viewmodel.HabitViewModel
import com.apexplanet.habitflow.ui.viewmodel.HabitViewModelFactory
import kotlinx.serialization.Serializable

@Serializable
sealed interface NavKey {
    @Serializable
    data object HabitList : NavKey
    @Serializable
    data object AddHabit : NavKey
}

@Composable
fun HabitFlowApp(repository: HabitRepository) {
    val backStack = rememberNavBackStack(initialKey = NavKey.HabitList)
    val viewModelFactory = remember(repository) { HabitViewModelFactory(repository) }

    NavDisplay(
        backstack = backStack,
        onBack = { backStack.pop() }
    ) { key ->
        NavBackStack(key, backStack) { contentKey ->
            ViewModelStoreNavContent(contentKey) { targetKey ->
                val viewModel: HabitViewModel = viewModel(factory = viewModelFactory)
                when (targetKey) {
                    NavKey.HabitList -> HabitListScreen(
                        viewModel = viewModel,
                        onAddHabitClick = { backStack.push(NavKey.AddHabit) }
                    )
                    NavKey.AddHabit -> AddHabitScreen(
                        viewModel = viewModel,
                        onBack = { backStack.pop() }
                    )
                }
            }
        }
    }
}
