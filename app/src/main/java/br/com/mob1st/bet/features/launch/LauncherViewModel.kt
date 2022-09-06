package br.com.mob1st.bet.features.launch

import androidx.compose.runtime.Immutable
import br.com.mob1st.bet.core.ui.state.AsyncState
import br.com.mob1st.bet.core.ui.state.FetchedData
import br.com.mob1st.bet.core.ui.state.SimpleMessage
import br.com.mob1st.bet.core.ui.state.StateViewModel
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LauncherViewModel(
    private val launchAppUseCase: LaunchAppUseCase,
) : StateViewModel<LaunchData, LauncherUiEvent>(AsyncState(LaunchData(), loading = true)){

    init {
        triggerUseCase()
    }

    override fun fromUi(uiEvent: LauncherUiEvent) {
        setState {
            it.removeMessage((uiEvent as LauncherUiEvent.TryAgain).simpleMessage, loading = true)
        }
        triggerUseCase()
    }

    private fun triggerUseCase() {
        setState {
            launchAppUseCase()
            it.data(data = it.data.copy(success = true))
        }
    }

}

@Immutable
data class LaunchData(val success: Boolean = false) : FetchedData {
    override fun hasData(): Boolean = success
}

sealed class LauncherUiEvent {
    data class TryAgain(val simpleMessage: SimpleMessage) : LauncherUiEvent()
}