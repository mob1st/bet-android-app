package br.com.mob1st.features.finances.impl.dependencies

import br.com.mob1st.features.finances.impl.domain.usecases.GetBudgetBuilderForStepUseCase
import br.com.mob1st.features.finances.impl.domain.usecases.ProceedBuilderUseCase
import br.com.mob1st.features.finances.impl.domain.usecases.StartBuilderStepUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

internal val domainModule
    get() = module {
        factoryOf(::GetBudgetBuilderForStepUseCase)
        factoryOf(::StartBuilderStepUseCase)
        factoryOf(::ProceedBuilderUseCase)
    }
