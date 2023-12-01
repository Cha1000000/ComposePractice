package com.umno.digital.composepractice.data

import java.util.UUID

fun createTextInputList() : List<TextItemData> {
    val list = ArrayList<TextItemData>()
    for (i in 1..10) list.add(TextItemData(i.toLong(), ""))
    return list
}

fun createUuidsList() : List<UuidItem> {
    val list = ArrayList<UuidItem>()
    repeat(16) { list.add(UuidItem(UUID.randomUUID().toString())) }
    return list
}