package com.udacity.project4.locationreminders.savereminder

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
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
            5.00,
            10.00
        )
        viewModel = SaveReminderViewModel(
            ApplicationProvider.getApplicationContext(),
            fakeDataSource
        )
    }


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