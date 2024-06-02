package br.com.mob1st.features.onboarding.impl.ui

import androidx.lifecycle.ViewModel
import br.com.mob1st.core.state.async.Async
import br.com.mob1st.core.state.async.launch
import br.com.mob1st.core.state.extensions.collectUpdate
import br.com.mob1st.core.state.extensions.launchEmit
import br.com.mob1st.core.state.extensions.onCollect
import br.com.mob1st.features.onboarding.impl.domain.OpenAppUseCase
import br.com.mob1st.features.onboarding.impl.domain.SplashDestination
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

/**
 * A ViewModel that manages of the Launcher screen.
 *
 * @param openAppUseCase A use case triggered when the app opens.
 */
@KoinViewModel
class LauncherViewModel(
    openAppUseCase: OpenAppUseCase,
) : ViewModel(), LauncherUiContract {
    // output state
    private val _uiOutput = MutableStateFlow(LauncherUiState())
    override val uiOutput: StateFlow<LauncherUiState> = _uiOutput.asStateFlow()

    // inputs
    private val helperPrimaryActionInput = MutableSharedFlow<Unit>()

    // actions
    private val openAppAction =
        Async<SplashDestination> {
            openAppUseCase()
        }

    init {
        _uiOutput.collectUpdate(openAppAction.loading) { currentState, newData ->
            currentState.copy(isLoading = newData)
        }

        _uiOutput.collectUpdate(openAppAction.failure) { currentState, newData ->
            // TODO display the DS component called HelperMessage
            currentState.copy(errorMessage = newData.message)
        }

        _uiOutput.collectUpdate(openAppAction.success) { currentState, splashDestination ->
            currentState.copy(navTarget = LauncherNavTarget.of(splashDestination))
        }

        helperPrimaryActionInput.onCollect {
            openAppAction.launch()
        }
        openAppAction.launch()
    }

    override fun helperPrimaryAction() {
        helperPrimaryActionInput.launchEmit(Unit)
    }
}
