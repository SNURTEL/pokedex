package com.example.pappokedex.data.database

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PokemonConverter {
    @TypeConverter
    fun listToJson(types: List<String>) = Json.encodeToString(types)

    @TypeConverter
    fun jsonToList(value: String): List<String> = Json.decodeFromString(value)
}