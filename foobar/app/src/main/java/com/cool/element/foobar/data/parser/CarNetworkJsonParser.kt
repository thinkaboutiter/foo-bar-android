package com.cool.element.foobar.data.parser

import com.google.gson.Gson
import android.content.Context
import androidx.annotation.RawRes
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

import com.cool.element.foobar.domain.entity.network.CarNetwork
import com.cool.element.foobar.domain.entity.network.CarNetworkResponse

class CarNetworkJsonParser(
    private val gson: Gson = Gson()
) : CarNetworkJsonParserI {

    @Throws(IOException::class)
    override fun parseCarModelsFromRaw(
        context: Context, @RawRes resourceId: Int
    ): List<CarNetwork> {
        val inputStream = context.resources.openRawResource(resourceId)
        return parseCarModelsFromStream(inputStream)
    }

    @Throws(Exception::class)
    override fun parseCarModelsFromStream(inputStream: InputStream): List<CarNetwork> {
        val reader = InputStreamReader(inputStream)
        val response = gson.fromJson(reader, CarNetworkResponse::class.java)
        reader.close()
        return response.cars
    }

    @Throws(Exception::class)
    override fun parseCarModelsFromString(jsonString: String): List<CarNetwork> {
        val response = gson.fromJson(jsonString, CarNetworkResponse::class.java)
        return response.cars
    }
}

