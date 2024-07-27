package br.com.mob1st.features.finances.impl.domain.events

import br.com.mob1st.core.observability.events.AnalyticsEvent

data object CompleteBuilderEvent : AnalyticsEvent {
    override val name: String = "builder_completed"
    override val logInfo: Map<String, Any?> = emptyMap()
}
