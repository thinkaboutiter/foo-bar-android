package com.cool.element.foobar.domain.entity.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars")
data class CarLocal(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "about_url") val aboutUrlString: String,
    @ColumnInfo(name = "details") val details: String,
    @ColumnInfo(name = "image_url") val imageUrlString: String
)
