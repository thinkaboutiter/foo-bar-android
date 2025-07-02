package com.cool.element.foobar.domain.entity.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CarDao {
    @Query("SELECT * FROM cars")
    suspend fun getAllCars(): List<CarLocal>

    @Query("SELECT * FROM cars WHERE id = :carId")
    suspend fun getCarById(carId: Long): CarLocal?

    @Insert
    suspend fun insertCar(car: CarLocal)

    @Delete
    suspend fun deleteCar(car: CarLocal)

    @Delete
    suspend fun deleteAllCars()

    @Update
    suspend fun updateCar(car: CarLocal)
}
