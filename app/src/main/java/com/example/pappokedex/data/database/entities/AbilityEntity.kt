package com.example.pappokedex.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "abilities")
data class AbilityEntity(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    @ColumnInfo(name = "effect_description") val effectDescription: String
)