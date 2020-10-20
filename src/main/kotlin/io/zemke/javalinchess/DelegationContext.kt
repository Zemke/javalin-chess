package io.zemke.javalinchess

import io.javalin.http.Context

class DelegationContext(private val ctx: Context) {

    fun queryParam(key: String, default: String? = null) = ctx.queryParam(key, default)
    fun header(header: String) = ctx.header(header)
    fun pathParam(key: String) = ctx.pathParam(key)
    fun formParam(key: String, default: String? = null) = ctx.formParam(key, default)
    fun body() = ctx.body()
    fun status(statusCode: Int): Context = ctx.status(statusCode)
    fun json(obj: Any) = ctx.json(obj)
}
