package com.udacity.project4.locationreminders.data

import com.google.android.gms.common.api.ResultTransform
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import java.lang.Exception

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(var tasks:MutableList<ReminderDTO>?= mutableListOf()) : ReminderDataSource {

    private var shouldReturnError=false
//    TODO: Create a fake data source to act as a double to the real data source

//    override suspend fun getReminders(): Result<List<ReminderDTO>> {
//        if(shouldReturnError) throw Exception("No Reminders")
//        tasks?.let { return Result.Success(ArrayList(it)) }
//        return Result.Error(
//            "tasks not found"
//        )
//    }
override suspend fun getReminders(): Result<List<ReminderDTO>> {
    try {
        if(shouldReturnError) {
            throw Exception("No Reminders")
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
        if(shouldReturnError) throw Exception("No such item")
        tasks?.let {
           val reminder = it.find {
                it.id==id
            }
            return if(reminder==null) Result.Error("no such item")
            else Result.Success(reminder)
        }
        return Result.Error(
            "tasks not found"
        )
    }

    override suspend fun deleteAllReminders() {
       tasks?.clear()
    }


}