package com.raywenderlich.android.learningcompanion.prefstore

import kotlinx.coroutines.flow.Flow

interface PrefsStore {
    fun isNightMode(): Flow<Boolean>
    suspend fun toggleNightMode()
}