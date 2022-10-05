package com.udacity.project4.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.mockito.internal.matchers.Null

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {
    private lateinit var fakeDataSource: FakeDataSource
    private lateinit var viewModel: RemindersListViewModel
    private lateinit var reminderDTO: ReminderDTO

    @get:Rule
    val instantTaskExecutorRule=InstantTaskExecutorRule()
    @get:Rule
    val mainCoroutineRule=MainCoroutineRule()
    //TODO: provide testing to the RemindersListViewModel and its live data objects
    @Before
    fun setUpViewModel(){
        fakeDataSource = FakeDataSource()
        viewModel = RemindersListViewModel(ApplicationProvider.getApplicationContext(),fakeDataSource)
        reminderDTO = ReminderDTO(
            "title",
            "description",
            "location",
            5.0,
            10.0
        )
    }
// Test the loading animation displayed time
    @Test
    fun check_loading() {
        mainCoroutineRule.runBlockingTest {
            mainCoroutineRule.pauseDispatcher()
            fakeDataSource.saveReminder(reminderDTO)
            viewModel.loadReminders()
            assertThat(viewModel.showLoading.value, `is`(true))
            mainCoroutineRule.resumeDispatcher()
            assertThat(viewModel.showLoading.value, `is`(false))
        }
    }
// Test saving a reminder in the database but with error
    @Test
    fun callLoadReminders_shouldReturnError(){
        mainCoroutineRule.runBlockingTest {
            fakeDataSource.setShouldReturnError(true)
            fakeDataSource.saveReminder(reminderDTO)
            viewModel.loadReminders()
            assertThat(viewModel.showSnackBar.value,`is`("No Reminders"))
        }
    }
// Test saving a reminder in the database
    @Test
    fun callLoadReminders_shouldReturnSuccess(){
        mainCoroutineRule.runBlockingTest {
            fakeDataSource.setShouldReturnError(false)
            fakeDataSource.saveReminder(reminderDTO)
            viewModel.loadReminders()
            assertThat(viewModel.showSnackBar.value,not("No Reminders"))
        }
    }



    @After
    fun stop(){
        stopKoin()
    }
}
