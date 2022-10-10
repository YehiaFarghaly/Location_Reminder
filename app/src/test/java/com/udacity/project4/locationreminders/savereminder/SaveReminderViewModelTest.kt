package com.udacity.project4.locationreminders.savereminder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.reminderslist.MainCoroutineRule
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SaveReminderViewModelTest {
    private lateinit var fakeDataSource: FakeDataSource
    private lateinit var viewModel: SaveReminderViewModel
    private lateinit var reminderDataItem: ReminderDataItem

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setupViewModel() {
        fakeDataSource = FakeDataSource()
       reminderDataItem= ReminderDataItem(
            "title",
            "description",
            "location",
            5.0,
            10.0
        )
        viewModel = SaveReminderViewModel(
            ApplicationProvider.getApplicationContext(),
            fakeDataSource
        )
    }

// Test the loading animation time
    @Test
    fun check_loading() {
        mainCoroutineRule.runBlockingTest {
            mainCoroutineRule.pauseDispatcher()
            viewModel.saveReminder(
                reminderDataItem
            )
            assertThat(viewModel.showLoading.value, `is`(true))
            mainCoroutineRule.resumeDispatcher()
            assertThat(viewModel.showLoading.value, `is`(false))
        }
    }
// Test validateAndSaveReminder Method when inserting a reminder with null location
    @Test
    fun testSaveReminder_LocationNull_ShouldReturnError(){
        viewModel.validateAndSaveReminder(ReminderDataItem("title","description",null,5.0,10.0))
        val snackBarMessage=viewModel.showSnackBarInt.value
        assertThat(snackBarMessage,`is`(R.string.err_select_location))
        assertThat(fakeDataSource.tasks!!.size,`is`(0))
    }
    
    // Test saving a reminder successfully
    @Test
    fun saveReminder_found() {
        val testReminder =
            reminderDataItem

        viewModel.saveReminder(testReminder)
        assertThat(viewModel.showToast.value, `is`("Reminder Saved !"))
    }


    //TODO: provide testing to the SaveReminderView and its live data objects
 @After
 fun stop(){
     stopKoin()
 }

}
