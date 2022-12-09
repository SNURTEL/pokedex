package com.example.pappokedex.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class RepositoryCache<Key, Type> {
    protected val contents = mutableMapOf<Key, Type>()

    suspend fun getOrDefault(identifier: Key, default: suspend (Key) -> Type): Type =
        withContext(Dispatchers.IO) {
            contents.getOrElse(identifier) {
                val wanted = default(identifier)
                put(identifier, wanted)
                wanted
            }
        }

    fun getOrNull(identifier: Key): Type? = contents.getOrDefault(identifier, null)

    fun put(identifier: Key, value: Type) {
        contents[identifier] = value
    }

    val entryCount: Int
        get() = contents.size

    val values: List<Type>
        get() = contents.values.toList()
}