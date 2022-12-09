package com.example.pappokedex.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class RepositoryCache<Key, Type> {
    protected val _contents = mutableMapOf<Key, Type>()

    suspend fun getOrDefault(identifier: Key, default: suspend (Key) -> Type): Type = withContext(Dispatchers.IO) {
        _contents.getOrElse(identifier) {
            val wanted = default(identifier)
            put(identifier, wanted)
            wanted
        }
    }

    suspend fun getOrNull(identifier: Key): Type? = _contents.getOrDefault(identifier, null)

    suspend fun put(identifier: Key, value: Type) {
        _contents[identifier] = value
    }
}