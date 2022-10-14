package com.udacity.project4.locationreminders.data

import com.google.android.gms.common.api.ResultTransform
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result


//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource() : ReminderDataSource {
    // tasks now is not nullable as mentioned in the review
    var tasks:MutableList<ReminderDTO> = mutableListOf()
    private var shouldReturnError=false
    //    TODO: Create a fake data source to act as a double to the real data source
    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        try {
            if (shouldReturnError)
                return Result.Error("Error")
            tasks.let { return Result.Success(it) }
            return Result.Success(tasks)
        }
        catch(exception:Exception){
            return Result.Error(exception.localizedMessage)
        }
    }
    fun setShouldReturnError(flag:Boolean) {
        shouldReturnError=flag
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        tasks.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        try {
            if (shouldReturnError) return Result.Error("Error")
            for (item in tasks) {
                if (item.id == id) return Result.Success(item)
            }
            return Result.Error("No item found")
        }
        catch(exception:Exception){
            return Result.Error(exception.localizedMessage)
        }
    }

    override suspend fun deleteAllReminders() {
        tasks.clear()
    }


}
