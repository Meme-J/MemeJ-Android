package com.example.memej.Utils.PagingUtils

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import timber.log.Timber
import java.io.IOException
import java.lang.reflect.Type

//Error on JSON Parsing Class
class DefaultOndataMisMatchAdapter<T> private constructor(
    private val delegate: JsonAdapter<T>,
    private val defaultValue: T?
) : JsonAdapter<T>() {

    @Throws(IOException::class)
    override fun fromJson(reader: JsonReader): T? {
        return try {
            delegate.fromJsonValue(reader.readJsonValue())
        } catch (e: Exception) {
            Timber.w("Wrongful content - could not parse delegate %s", delegate.toString())
            defaultValue
        }
    }

    @Throws(IOException::class)
    override fun toJson(writer: JsonWriter, value: T?) {
        delegate.toJson(writer, value)
    }

    companion object {
        @JvmStatic
        fun <T> newFactory(type: Class<T>, defaultValue: T?): JsonAdapter.Factory {
            return object : JsonAdapter.Factory {
                override fun create(
                    requestedType: Type,
                    annotations: Set<Annotation>,
                    moshi: Moshi
                ): JsonAdapter<*>? {
                    if (type != requestedType) {
                        return null
                    }
                    val delegate = moshi.nextAdapter<T>(this, type, annotations)
                    return DefaultOndataMisMatchAdapter(
                        delegate,
                        defaultValue
                    )
                }
            }
        }
    }
}