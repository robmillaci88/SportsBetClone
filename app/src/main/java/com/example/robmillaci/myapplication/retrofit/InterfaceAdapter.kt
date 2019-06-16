package com.example.robmillaci.myapplication.retrofit

import android.util.Log
import com.example.robmillaci.myapplication.pojos.SpecificTypes
import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer
import java.lang.reflect.Type
import com.google.gson.JsonParseException
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonSerializationContext


const val CLASS_PATH_NAME = "com.example.robmillaci.myapplication.pojos."

class InterfaceAdapter<T : Any> : JsonSerializer<T>, JsonDeserializer<T> {
    override fun serialize(obj: T, interfaceType: Type, context: JsonSerializationContext): JsonElement {
        val wrapper = JsonObject().apply {
            this.addProperty("specificType", obj::class.java.name)
            this.add("data", context.serialize(obj))
        }
        return wrapper
    }

    @Throws(JsonParseException::class)
    override fun deserialize(elem: JsonElement, interfaceType: Type, context: JsonDeserializationContext): T {
        val wrapper = elem as JsonObject
        Log.d("Wrapper",wrapper.toString())
        val typeName = get(wrapper, "type")
        Log.d("Wrapper",typeName.toString())

        val actualType = typeForName(typeName)

        return context.deserialize(wrapper, actualType)
    }

    private fun typeForName(typeElem: JsonElement): Type {
        try {
            return Class.forName(CLASS_PATH_NAME + typeElem.asString)
        } catch (e: ClassNotFoundException) {
            throw JsonParseException(e)
        }

    }

    private operator fun get(wrapper: JsonObject, memberName: String): JsonElement {
        return wrapper.get(memberName)
            ?: throw JsonParseException("no '$memberName' member found in what was expected to be an interface wrapper")
    }
}