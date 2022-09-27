package br.com.mob1st.bet.core.utils.objects

/**
 * An abstraction used when a selection of something can be done and return a value
 */
interface Selector<in Selection, out Result> {
    fun select(selection: Selection): Result
}