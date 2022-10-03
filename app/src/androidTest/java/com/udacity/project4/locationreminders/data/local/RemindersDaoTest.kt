package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import com.udacity.project4.locationreminders.data.dto.ReminderDTO

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import kotlinx.coroutines.ExperimentalCoroutinesApi;
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Test

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    private lateinit var database: RemindersDatabase

    //    TODO: Add testing implementation to the RemindersDao.kt
    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).build()
    }

    @Test
    fun insertReminderAndGetById() {
        runBlockingTest {
            val reminderDTO = ReminderDTO("title", "description", "location", 5.0, 10.0)
            database.reminderDao().saveReminder(reminderDTO)
            val loaded = database.reminderDao().getReminderById(reminderDTO.id)
            assertThat<ReminderDTO>(loaded as ReminderDTO, notNullValue())
            assertThat(loaded.id, `is`(reminderDTO.id))
            assertThat(loaded.title, `is`(reminderDTO.title))
            assertThat(loaded.description, `is`(reminderDTO.description))
            assertThat(loaded.location, `is`(reminderDTO.location))
            assertThat(loaded.latitude, `is`(reminderDTO.latitude))
            assertThat(loaded.longitude, `is`(reminderDTO.longitude))
        }
    }

    @After
    fun closeDb() = database.close()

}