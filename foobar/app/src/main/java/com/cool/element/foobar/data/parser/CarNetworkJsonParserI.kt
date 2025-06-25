package com.cool.element.foobar.data.parser

import android.content.Context
import androidx.annotation.RawRes
import java.io.IOException
import java.io.InputStream
import com.cool.element.foobar.domain.entity.network.CarNetwork

interface CarNetworkJsonParserI {
    @Throws(IOException::class)
    fun parseCarModelsFromRaw(context: Context, @RawRes resourceId: Int): List<CarNetwork>

    @Throws(Exception::class)
    fun parseCarModelsFromStream(inputStream: InputStream): List<CarNetwork>

    @Throws(Exception::class)
    fun parseCarModelsFromString(jsonString: String): List<CarNetwork>
}
