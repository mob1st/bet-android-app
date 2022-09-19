package br.com.mob1st.bet.features.competitions.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.mob1st.bet.R
import br.com.mob1st.bet.core.localization.getText
import br.com.mob1st.bet.core.ui.compose.LocalLogger
import br.com.mob1st.bet.core.ui.ds.atoms.CompositionLocalGrid
import br.com.mob1st.bet.core.ui.ds.molecule.DismissSnackbar
import br.com.mob1st.bet.core.ui.ds.organisms.FetchedCrossfade
import br.com.mob1st.bet.core.ui.ds.page.DefaultErrorPage
import br.com.mob1st.bet.core.ui.state.AsyncState
import br.com.mob1st.bet.core.ui.state.SimpleMessage
import br.com.mob1st.bet.core.utils.extensions.ifNotEmpty
import br.com.mob1st.bet.features.competitions.domain.CompetitionEntry

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun ConfrontationDetailScreen(
    viewModel: ConfrontationListViewModel,
    popBackStack: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val updatedNavigateBack by rememberUpdatedState(popBackStack)
    if (state.data.selected == null) {
        LaunchedEffect(Unit) {
            updatedNavigateBack()
        }
    }

    ConfrontationDetailPage(
        state = state,
        onTryAgain = { viewModel.fromUi(ConfrontationUiEvent.TryAgain(it)) },
        onEmpty = updatedNavigateBack
    )

    BackHandler {
        viewModel.fromUi(ConfrontationUiEvent.SetSelection(null))
    }
}

@Composable
private fun ConfrontationDetailPage(
    state: AsyncState<ConfrontationData>,
    onTryAgain: (SimpleMessage) -> Unit,
    onEmpty: () -> Unit
) {

    val onEmptyUpdated by rememberUpdatedState(onEmpty)

    FetchedCrossfade(
        state = state,
        emptyError = { _, message -> DefaultErrorPage(message = message, onTryAgain = onTryAgain) },
        emptyLoading = { Loading() },
        empty = {
            LaunchedEffect(Unit) {
                onEmptyUpdated()
            }
        },
        data = { ConfrontationDetailContent(it, {}, {}) },
    )
}

@Composable
private fun Loading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ConfrontationDetailContent(
    state: AsyncState<ConfrontationData>,
    onDismissSnackbar: (SimpleMessage) -> Unit,
    onNullDetail: () -> Unit
) {
    state.messages.ifNotEmpty { message ->
        DismissSnackbar(
            message = stringResource(id = message.descriptionResId),
            onDismiss = { onDismissSnackbar(message) }
        )
    }
    if (state.data.detail != null) {
        ConfrontationData(state.data)
    } else {
        LocalLogger.current.w(
            "A confrontation detail should never be null. " +
                "Probably the page was opened in a weird state."
        )
        onNullDetail()
    }
}

@Composable
private fun ConfrontationData(
    confrontationData: ConfrontationData,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = CompositionLocalGrid.current.margin)
            .padding(vertical = CompositionLocalGrid.current.line * 4),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header(
                competitionEntry = confrontationData.entry,
                progress = confrontationData.progress
            )
            Spacer(
                modifier = Modifier.height(CompositionLocalGrid.current.line * 8)
            )
            NodeComponent(
                root = confrontationData.detail!!.contest,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            TextButton(
                onClick = { /*TODO*/ },
            ) {
                val text = if (confrontationData.isLast) {
                    R.string.confrontation_detail_score_done
                } else {
                    R.string.confrontation_detail_score_next
                }
                Text(text = stringResource(id = text))
            }
        }
    }
}

@Composable
private fun Header(
    modifier: Modifier = Modifier,
    competitionEntry: CompetitionEntry,
    progress: Float,
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = CompositionLocalGrid.current.line),
            progress = progress,
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = context.getText(competitionEntry.name),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}