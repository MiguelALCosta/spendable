package com.app.spendable.presentation.add.subscription

import com.app.spendable.presentation.components.SelectableChoiceComponent
import com.app.spendable.presentation.wallet.SubscriptionIcon
import java.time.LocalDate

enum class SubscriptionCategory {
    STREAMING, MUSIC, MULTI_SERVICES, GAMING, SPORTS, UTILITIES, OTHER
}

data class AddSubscriptionForm(
    var amount: String?,
    val categories: List<SelectableChoiceComponent.Choice>,
    val subcategories: Map<String, List<SelectableChoiceComponent.Choice>?>,
    var title: String?,
    var selectedCategory: String?,
    var selectedSubcategory: String?,
    var date: LocalDate,
    val frequencies: List<SelectableChoiceComponent.Choice>,
    var selectedFrequency: String
) {

    fun getActiveSubcategoryChoices() =
        selectedCategory?.let {
            subcategories.getOrDefault(it, null)
        }

    fun needsTitle(): Boolean {
        val category = SubscriptionCategory.entries.firstOrNull { it.name == selectedCategory }
        val subcategory = SubscriptionIcon.entries.firstOrNull { it.name == selectedSubcategory }
        return category == SubscriptionCategory.OTHER
                || category == SubscriptionCategory.SPORTS
                || subcategory == SubscriptionIcon.OTHER_MUSIC
                || subcategory == SubscriptionIcon.OTHER_GAMING
                || subcategory == SubscriptionIcon.OTHER_STREAMING
                || subcategory == SubscriptionIcon.OTHER_MULTI_SERVICE
    }


}