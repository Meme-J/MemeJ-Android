package com.example.memej.Utils

import androidx.room.TypeConverter
import com.example.memej.responses.homeMememResponses.Coordinates
import com.example.memej.responses.homeMememResponses.HomeUsers
import com.example.memej.responses.memeWorldResponses.Coordinate
import com.example.memej.responses.memeWorldResponses.TemplateId
import com.example.memej.responses.memeWorldResponses.User
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.util.*

object RoomConvertor {

    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()


    //MOSHI
    @TypeConverter
    @JvmStatic
    fun toDate(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    @JvmStatic
    fun toLong(value: Date?): Long? {
        return value?.time
    }


    fun stringAdapter(): JsonAdapter<List<String>> {
        val listOfStringsType = Types.newParameterizedType(List::class.java, String::class.java)
        val adapter: JsonAdapter<List<String>> = moshi.adapter(listOfStringsType)
        return adapter
    }

    fun coordinateAdapter(): JsonAdapter<List<Coordinate>> {
        val listType = Types.newParameterizedType(List::class.java, Coordinate::class.java)
        val adapter: JsonAdapter<List<Coordinate>> = moshi.adapter(listType)
        //val jsonAdapter = RoomConvertor.moshi.adapter(Coordinate::class.java)
        return adapter
    }

    fun coordinateHomeAdapter(): JsonAdapter<List<Coordinates>> {
        val listType = Types.newParameterizedType(List::class.java, Coordinates::class.java)
        val adapter: JsonAdapter<List<Coordinates>> = moshi.adapter(listType)
        //val jsonAdapter = RoomConvertor.moshi.adapter(Coordinate::class.java)
        return adapter
    }

    fun intAdapter(): JsonAdapter<List<Int>> {
        val listOfStringsType = Types.newParameterizedType(List::class.java, String::class.java)
        val adapter: JsonAdapter<List<Int>> = moshi.adapter(listOfStringsType)
        return adapter
    }

    fun userAdapter(): JsonAdapter<List<User>> {
        val listType = Types.newParameterizedType(List::class.java, User::class.java)
        val adapter: JsonAdapter<List<User>> = moshi.adapter(listType)
        return adapter
    }

    fun userHomeAdapter(): JsonAdapter<List<HomeUsers>> {
        val listType = Types.newParameterizedType(List::class.java, HomeUsers::class.java)
        val adapter: JsonAdapter<List<HomeUsers>> = moshi.adapter(listType)
        return adapter
    }

    fun templateAdapter(): JsonAdapter<TemplateId> {
        val adapter: JsonAdapter<TemplateId> = moshi.adapter(TemplateId::class.java)
        return adapter
    }

    fun templateHomeAdapter(): JsonAdapter<com.example.memej.responses.homeMememResponses.TemplateId> {
        val adapter: JsonAdapter<com.example.memej.responses.homeMememResponses.TemplateId> =
            moshi.adapter(com.example.memej.responses.homeMememResponses.TemplateId::class.java)
        return adapter
    }

    @TypeConverter
    @JvmStatic
    fun toJsonCoordinate(cn: List<Coordinate>): String = coordinateAdapter().toJson(cn)

    @TypeConverter
    @JvmStatic
    fun fromJsonCoordinate(string: String): List<Coordinate>? = coordinateAdapter().fromJson(string)

    @TypeConverter
    @JvmStatic
    fun toJsonHomeCoordinate(cn: List<Coordinates>): String = coordinateHomeAdapter().toJson(cn)

    @TypeConverter
    @JvmStatic
    fun fromJsonHomeCoordinate(string: String): List<Coordinates>? =
        coordinateHomeAdapter().fromJson(string)


    @TypeConverter
    @JvmStatic
    fun toJSON(list: List<String>?): String? = stringAdapter().toJson(list)

    @TypeConverter
    @JvmStatic
    fun fromJson(list: String): List<String>? = stringAdapter().fromJson(list)


    @TypeConverter
    @JvmStatic
    fun toJSONInt(list: List<Int>?): String? = intAdapter().toJson(list)

    @TypeConverter
    @JvmStatic
    fun fromJsonInt(list: String): List<Int>? = intAdapter().fromJson(list)


    @TypeConverter
    @JvmStatic
    fun toJSONUser(list: List<User>?): String? = userAdapter().toJson(list)

    @TypeConverter
    @JvmStatic
    fun fromJsonUser(list: String): List<User>? = userAdapter().fromJson(list)

    @TypeConverter
    @JvmStatic
    fun toJSONHomeUser(list: List<HomeUsers>?): String? = userHomeAdapter().toJson(list)

    @TypeConverter
    @JvmStatic
    fun fromJsonHomeUser(list: String): List<HomeUsers>? = userHomeAdapter().fromJson(list)


    @TypeConverter
    @JvmStatic
    fun toJSONTemplate(temp: TemplateId): String? = templateAdapter().toJson(temp)

    @TypeConverter
    @JvmStatic
    fun fromJsonTemplate(list: String): TemplateId? = templateAdapter().fromJson(list)


    @TypeConverter
    @JvmStatic
    fun toJSONTemplateHome(temp: com.example.memej.responses.homeMememResponses.TemplateId): String? =
        templateHomeAdapter().toJson(temp)

    @TypeConverter
    @JvmStatic
    fun fromJsonTemplateHome(list: String): com.example.memej.responses.homeMememResponses.TemplateId? =
        templateHomeAdapter().fromJson(list)


}