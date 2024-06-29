package br.com.mob1st.features.finances.impl.dependencies

import br.com.mob1st.features.finances.impl.TwoCentsDb
import br.com.mob1st.features.finances.impl.data.preferences.RecurrenceBuilderCompletionsDataSource
import br.com.mob1st.features.finances.impl.data.ram.RecurrenceBuilderListsDataSource
import br.com.mob1st.features.finances.impl.data.repositories.RecurrenceBuilderRepositoryImpl
import br.com.mob1st.features.finances.impl.data.repositories.categories.CategoriesRepositoryImpl
import br.com.mob1st.features.finances.impl.data.repositories.categories.CategoryDataMapper
import br.com.mob1st.features.finances.impl.data.repositories.suggestions.SuggestionDataMapper
import br.com.mob1st.features.finances.impl.data.repositories.suggestions.SuggestionsRepositoryImpl
import br.com.mob1st.features.finances.impl.data.sqldelight.DatabaseFactory
import br.com.mob1st.features.finances.impl.data.system.AndroidRecurrenceLocalizationProvider
import br.com.mob1st.features.finances.impl.data.system.RecurrenceLocalizationProvider
import br.com.mob1st.features.finances.impl.domain.repositories.CategoriesRepository
import br.com.mob1st.features.finances.impl.domain.repositories.RecurrenceBuilderRepository
import br.com.mob1st.features.finances.impl.domain.repositories.SuggestionsRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule
    get() = module {
        includes(
            databaseModule,
            categoriesDataModule,
            suggestionsDataModule,
            repositoriesModule,
        )
    }

private val databaseModule = module {
    single {
        DatabaseFactory.create(get())
    }
}

private val categoriesDataModule = module {
    factory { CategoryDataMapper }
    factory {
        get<TwoCentsDb>().categoriesQueries
    }
    factoryOf(::CategoriesRepositoryImpl) bind CategoriesRepository::class
}

private val suggestionsDataModule = module {
    factory {
        get<TwoCentsDb>().suggestionsQueries
    }
    factoryOf(::SuggestionDataMapper)
    factoryOf(::SuggestionsRepositoryImpl) bind SuggestionsRepository::class
}

private val repositoriesModule = module {
    factory<RecurrenceBuilderRepository> {
        RecurrenceBuilderRepositoryImpl(
            listsDataSource = get<RecurrenceBuilderListsDataSource>(),
            completionsDataSource = get<RecurrenceBuilderCompletionsDataSource>(),
            io = get(),
        )
    }

    factoryOf(::AndroidRecurrenceLocalizationProvider) bind RecurrenceLocalizationProvider::class
    factoryOf(::RecurrenceBuilderCompletionsDataSource)
}
