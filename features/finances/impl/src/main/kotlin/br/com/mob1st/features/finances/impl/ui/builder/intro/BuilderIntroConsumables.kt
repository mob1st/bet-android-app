package br.com.mob1st.features.finances.impl.ui.builder.intro

import androidx.compose.runtime.Immutable
import arrow.optics.optics
import br.com.mob1st.features.finances.impl.ui.builder.navigation.BuilderRoute
import br.com.mob1st.features.utils.errors.CommonErrorSnackbarState

@optics
@Immutable
data class BuilderIntroConsumables(
    val route: BuilderRoute? = null,
    val snackbar: CommonErrorSnackbarState? = null,
) {
    companion object
}
