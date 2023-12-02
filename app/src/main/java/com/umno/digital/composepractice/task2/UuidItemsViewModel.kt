package com.umno.digital.composepractice.task2

import androidx.lifecycle.ViewModel
import com.umno.digital.composepractice.data.UuidItem
import com.umno.digital.composepractice.data.createUuidsList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class UuidItemsViewModel : ViewModel() {

    // StateFlow to hold the list of uuids
    private val _uuidListState = MutableStateFlow(emptyList<UuidItem>())
    val uuidsListState: StateFlow<List<UuidItem>> = _uuidListState.asStateFlow()

    init {
        // Initialize the list of uuids when the ViewModel is created
        _uuidListState.update { createUuidsList() }
    }

    /**
     * Refreshes the list of uuids.
     */
    fun refreshAll() {
        _uuidListState.update { createUuidsList() }
    }

    /**
     * Removes uuid from the list.
     * @param currentItem The email message to be removed.
     */
    fun removeItem(currentItem: UuidItem) {
        _uuidListState.update {
            val mutableList = it.toMutableList()
            mutableList.remove(currentItem)
            mutableList
        }
    }

    /**
     * Add a new random uuid to the list.
     */
    fun addItem() = _uuidListState.update {
        val mutableList = it.toMutableList()
        mutableList.add(UuidItem(UUID.randomUUID().toString()))
        mutableList
    }

    /**
     * Refresh current uuid item in the list.
     * @param currentItem The email message to be removed.
     */
    fun refreshItem(currentItem: UuidItem) = _uuidListState.update {
        val mutableList = it.map { uuidItem ->
            if (uuidItem.uuid == currentItem.uuid) {
                UuidItem(UUID.randomUUID().toString())
            } else {
                uuidItem
            }
        }.toMutableList()
        mutableList
    }
}