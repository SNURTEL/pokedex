package com.example.pappokedex

import com.example.pappokedex.data.RepositoryCache
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*

class TestCache<K, T> : RepositoryCache<K, T>() {
    val cacheContents: MutableMap<K, T>
        get() = contents
}

class RepoCacheTest {
    @Test
    fun testMiss() {
        val cache = TestCache<Int, String>()
        runBlocking {
            cache.getOrDefault(123) { "balbinka" }
        }
        assert(cache.cacheContents[123] == "balbinka")
    }

    @Test
    fun testHit() {
        val cache = TestCache<Int, String>()
        runBlocking {
            cache.put(123, "balbinka")
            cache.getOrDefault(123) {
                fail()
                ""
            }
        }
    }

    @Test
    fun testOnUnit() {
        val cache = TestCache<Unit, String>()
        runBlocking {
            cache.put(Unit, "balbinka")
            cache.getOrDefault(Unit) {
                fail()
                ""
            }
        }
    }
}