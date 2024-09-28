package com.app.spendable.utils

import java.math.BigDecimal

fun BigDecimal.toFormatedPrice(): String {
    return this.toString() + "€"
}