package com.example.woof.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Dog (
    @DrawableRes val imageRes: Int,
    @StringRes val name: Int,
    val age: Int,
    @StringRes val dogDescription: Int,
)