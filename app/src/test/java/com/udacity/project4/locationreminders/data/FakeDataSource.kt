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
            if(shouldReturnError)
                return Result.Error("Error")
                tasks?.let { return Result.Success(it) }
        // case of null return an empty list ( as specified in the review)
        tasks = mutableListOf()
        return Result.Success(tasks!!)
    }
    fun setShouldReturnError(flag:Boolean) {
        shouldReturnError=flag
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        tasks?.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
   if(shouldReturnError) return Result.Error("Error")
        for(item in tasks!!) {
            if(item.id==id) return Result.Success(item)
        }
        return Result.Error("No item found")
    }

    override suspend fun deleteAllReminders() {
        tasks?.clear()
    }


}
