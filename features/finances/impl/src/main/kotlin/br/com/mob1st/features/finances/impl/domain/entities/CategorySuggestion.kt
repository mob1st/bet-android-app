package br.com.mob1st.features.finances.impl.domain.entities

import br.com.mob1st.core.kotlinx.structures.Identifiable
import br.com.mob1st.core.kotlinx.structures.RowId

data class CategorySuggestion(
    override val id: RowId,
    val name: Name,
    val linkedCategory: Category? = null,
) : Identifiable<RowId> {
    enum class Name {
        RentOrMortgage,
        PropertyTaxes,
        HealthInsurance,
        CarInsurance,
        PublicTransport,
        HomeInsurance,
        LoanPayments,
        InternetSubscription,
        CellPhonePlan,
        CableOrStreamingServices,
        MusicStreamingServices,
        MagazineOrNewspaperSubscriptions,
        Gym,
        AssociationFees,
        PrivateRetirementPlans,
        PersonalEducation,
        ChildrenSchool,
        Childcare,
        Groceries,
        DiningOut,
        FoodDelivery,
        WeekdayLunch,
        CoffeeSnacks,
        TransportationFuel,
        PublicTransportTickets,
        Cinema,
        Concerts,
        SportsTickets,
        ElectronicGames,
        Bars,
        NightClubs,
        HouseholdSupplies,
        FitnessRecreation,
        HolidayGifts,
        VacationTravel,
        BackToSchoolSupplies,
        WinterClothing,
        SummerActivities,
        GardenSupplies,
        HomeHeating,
        HolidayDecorations,
        TaxPreparationFees,
        SpringCleaning,
        Salary,
        Pension,
        RentalIncome,
    }
}
