package io.zemke.javalinchess.complex

import net.rubyeye.xmemcached.XMemcachedClient


// todo what's it like in Javalin (thinking dependency injection)?
object Memcached {

    // todo make this configurable
    private var client: XMemcachedClient = XMemcachedClient("localhost", 11211)

    fun store(key: String, obj: Any): Boolean {
        client[key, 3600] = obj
        return client.touch(key, 10)
    }

    fun <T> retrieve(key: String): T {
        return client.get<T>(key)
    }

    fun delete(key: String): Boolean {
        return client.delete(key)
    }
}
