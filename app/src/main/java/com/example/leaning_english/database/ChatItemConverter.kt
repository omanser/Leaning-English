package com.example.leaning_english.database

import androidx.room.TypeConverter
import com.example.leaning_english.database.entity.Explanation
import com.example.leaning_english.database.entity.Meanings
import com.example.leaning_english.database.entity.Phrase
import com.example.leaning_english.database.entity.Sentence
import com.example.leaning_english.database.entity.Translation
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import java.lang.Exception

class ChatItemConverter {
    @TypeConverter
    fun objectToString(arrayList: ArrayList<String>?): String? {
        return GsonInstance().INSTANCE?.gson?.toJson(arrayList)
    }

    @TypeConverter
    fun stringToObject(json: String?): ArrayList<String>?{
        val arrayListType = object: TypeToken<ArrayList<String>>(){}.type
        return GsonInstance().INSTANCE?.gson?.fromJson(json, arrayListType)
    }

    @TypeConverter
    fun IntListToObject(arrayList: Flow<ArrayList<Int>>?): String? {
        return GsonInstance().INSTANCE?.gson?.toJson(arrayList)
    }

    @TypeConverter
    fun objectToIntList(json: String?): Flow<ArrayList<Int>>?{
        val arrayListType = object: TypeToken<Flow<ArrayList<Int>>>(){}.type
        return GsonInstance().INSTANCE?.gson?.fromJson(json, arrayListType)
    }

    @TypeConverter
    fun listIntToObject(arrayList: ArrayList<Int>?): String? {
        return GsonInstance().INSTANCE?.gson?.toJson(arrayList)
    }

    @TypeConverter
    fun objectToListInt(json: String?): ArrayList<Int>?{
        val arrayListType = object: TypeToken<ArrayList<Int>>(){}.type
        return GsonInstance().INSTANCE?.gson?.fromJson(json, arrayListType)
    }

    @TypeConverter
    fun arrayListPhraseToString(arrayList: ArrayList<Phrase>?): String?{
        return GsonInstance().INSTANCE?.gson?.toJson(arrayList)
    }

    @TypeConverter
    fun stringToArrayListPhrase(json: String?): ArrayList<Phrase>?{
        val arrayList = object: TypeToken<ArrayList<Phrase>>(){}.type
        return GsonInstance().INSTANCE?.gson?.fromJson(json, arrayList)
    }

    @TypeConverter
    fun arrayListSentenceToString(sentence: ArrayList<Sentence>?): String?{
        return GsonInstance().INSTANCE?.gson?.toJson(sentence)
    }

    @TypeConverter
    fun stringToArrayListSentence(json: String?): ArrayList<Sentence>?{
        val arrayList = object: TypeToken<ArrayList<Sentence>>(){}.type
        return GsonInstance().INSTANCE?.gson?.fromJson(json, arrayList)
    }

    @TypeConverter
    fun explanationToString(explanation: Explanation?): String?{
        return GsonInstance().INSTANCE?.gson?.toJson(explanation)
    }

    @TypeConverter
    fun stringToExplanation(json: String?): Explanation?{
        val explanation = object: TypeToken<Explanation>(){}.type
        return GsonInstance().INSTANCE?.gson?.fromJson(json, explanation)
    }

    @TypeConverter
    fun translationToString(translation: Translation?): String? {
        return GsonInstance().INSTANCE?.gson?.toJson(translation)
    }

    @TypeConverter
    fun stringToTranslation(json: String?): Translation?{
        val arrayListType = object: TypeToken<Translation>(){}.type
        return GsonInstance().INSTANCE?.gson?.fromJson(json, arrayListType)
    }

    @TypeConverter
    fun mapToString(map: MutableMap<String, Int>?): String? {
        return GsonInstance().INSTANCE?.gson?.toJson(map)
    }

    @TypeConverter
    fun stringToMap(json: String?): MutableMap<String, Int>?{
        val listType = object: TypeToken<MutableMap<String, Int>>(){}.type
        return GsonInstance().INSTANCE?.gson?.fromJson(json, listType)
    }

    @TypeConverter
    fun meaningsToString(meanings: Meanings?): String? {
        return GsonInstance().INSTANCE?.gson?.toJson(meanings)
    }

    @TypeConverter
    fun stringToMeanings(json: String?): Meanings?{
        val arrayListType = object: TypeToken<Meanings>(){}.type
        return GsonInstance().INSTANCE?.gson?.fromJson(json, arrayListType)
    }
}