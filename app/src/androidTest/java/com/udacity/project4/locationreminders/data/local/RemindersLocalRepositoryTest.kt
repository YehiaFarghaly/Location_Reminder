package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {
    private lateinit var database: RemindersDatabase
    private lateinit var repository: RemindersLocalRepository


//    TODO: Add testing implementation to the RemindersLocalRepository.kt


    @Before
    fun createRepository(){
             database = Room.inMemoryDatabaseBuilder(
                 ApplicationProvider.getApplicationContext(),
                 RemindersDatabase::class.java
             ).build()
        repository= RemindersLocalRepository(database.reminderDao())
    }
    @ExperimentalCoroutinesApi
    @Test
   fun getRemindersWithID_found(){
       runBlocking {
      val reminderItem = ReminderDTO("title","description","location",5.0,5.0)
           repository.saveReminder(reminderItem)
          val item = repository.getReminder(reminderItem.id)
           item as Result.Success
           assertThat(item.data ,`is` (reminderItem))
       }
   }
        @ExperimentalCoroutinesApi
        @Test
    fun getReminderWithWrongID_notFound(){
        runBlocking {
            val reminderItem = ReminderDTO("title","description","location",5.0,5.0)
            repository.saveReminder(reminderItem)
            val item = repository.getReminder(reminderItem.id+"123")
            item as Result.Error
            assertThat(item.message,`is`("Reminder not found!"))
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun deleteAll_emptyListFound(){
        runBlocking {
            val reminderItem = ReminderDTO("title","description","location",5.0,5.0)
            repository.saveReminder(reminderItem)
            repository.deleteAllReminders()
            val list = repository.getReminders()
            list as Result.Success
            assertThat(list.data, `is`(emptyList()))
        }
    }
    @After
    fun cleanData() = database.close()
}