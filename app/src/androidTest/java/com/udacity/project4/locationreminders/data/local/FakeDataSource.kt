package com.udacity.project4.locationreminders.data

import com.google.android.gms.common.api.ResultTransform
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import java.lang.Exception

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(var tasks:MutableList<ReminderDTO>?= mutableListOf()) : ReminderDataSource {

    private var shouldReturnError=false
    //    TODO: Create a fake data source to act as a double to the real data source
    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        try {
            if(shouldReturnError) {
                throw Exception()
            }
            return Result.Success(ArrayList(tasks))
        } catch (ex: Exception) {
            return Result.Error(ex.localizedMessage)
        }
    }
    fun setShouldReturnError(flag:Boolean) {
        shouldReturnError=flag
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        tasks?.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        try{
            val reminder = tasks?.find { it.id==id }
            if(reminder==null || shouldReturnError){
                throw Exception()
            }
            else return Result.Success(reminder)
        }
        catch (exception:Exception){
            return Result.Error(exception.localizedMessage)
        }
    }

    override suspend fun deleteAllReminders() {
        tasks?.clear()
    }


}
