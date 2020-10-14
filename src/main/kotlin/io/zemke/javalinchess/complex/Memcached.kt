package io.zemke.javalinchess.complex

import net.rubyeye.xmemcached.XMemcachedClient

class Memcached {

    private val client: XMemcachedClient by lazy {
        XMemcachedClient("localhost", 11211)
    }

    fun store(key: String, obj: Any): Boolean {
        client[key, 3600] = obj
        return client.touch(key, 60 * 60 * 24) // a day
    }

    fun <T> retrieve(key: String): T {
        return client.get<T>(key)
    }

    fun delete(key: String): Boolean {
        return client.delete(key)
    }
}
