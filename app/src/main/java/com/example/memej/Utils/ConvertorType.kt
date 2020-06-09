package com.example.memej.Utils

import androidx.room.TypeConverter
import com.example.memej.responses.homeMememResponses.*

class ConvertorType {

    @TypeConverter
    fun getLikedByUserName(user: HomeUsers): String? {
        return user.username
    }

    @TypeConverter
    fun getLikedByUserId(user: HomeUsers): String? {
        return user._id
    }

    @TypeConverter
    fun getUserId(user: HomeUsers): String? {
        return user._id
    }

    @TypeConverter
    fun getUsername(user: HomeUsers): String? {
        return user.username
    }


    @TypeConverter
    fun getLPlaceholderValues(str: HomePlaceHolders): String? {
        return str.toString()
    }

    @TypeConverter
    fun getTagsOfMemeGroup(str: MemeGroupTags): String? {
        return str.toString()
    }

    @TypeConverter
    fun getTemplateImage(templateId: TemplateId): String? {
        return templateId.imageUrl
    }

    @TypeConverter
    fun getTemplateTags(templateId: TemplateId): List<String>? {
        return templateId.tags
    }

    @TypeConverter
    fun getCoordinates(templateId: TemplateId): List<Coordinates> {
        return templateId.coordinates
    }

}