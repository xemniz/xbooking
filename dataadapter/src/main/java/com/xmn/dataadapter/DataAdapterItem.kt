package com.xmn.dataadapter

interface DataAdapterItem
class DataItemsBuilder<T : DataAdapterItem> {
    private val items: MutableList<T> = mutableListOf()
    operator fun T?.unaryPlus() {
        this?.let { items.add(it) }
    }

    operator fun List<T?>.unaryPlus() {
        items.addAll(this.filterNotNull())
    }

    fun build(): List<T> = items
}

fun <T : DataAdapterItem> dataAdapterItems(builder: DataItemsBuilder<T>.() -> Unit): List<T> {
    return DataItemsBuilder<T>().apply(builder).build()
}