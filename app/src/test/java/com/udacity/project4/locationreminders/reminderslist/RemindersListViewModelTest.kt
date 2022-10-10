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
            assertThat(viewModel.showSnackBar.value,`is`("Error"))
        }
    }
// Test saveReminder Method when inserting a reminder item in the fake data source
    @Test
    fun testLoadReminder_shouldFind() {
    mainCoroutineRule.runBlockingTest {
        fakeDataSource.saveReminder(reminderDTO)
      viewModel.loadReminders()
        val item = viewModel.remindersList.value!!.get(0)
        assertThat(item.title,`is`("title"))
        assertThat(item.description,`is`("description"))
        assertThat(item.location,`is`("location"))
        assertThat(item.longitude,`is`(10.0))
        assertThat(item.latitude,`is`(5.0))
    }
}


    @After
    fun stop(){
        stopKoin()
    }
}
