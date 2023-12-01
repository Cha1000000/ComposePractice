package com.umno.digital.composepractice.data

import java.io.Serializable

data class TextItemData(
    val id: Long,
    var text: String,
    var isSelected: Boolean = false,
) : Serializable

data class UuidItem(var uuid: String) : Serializable
