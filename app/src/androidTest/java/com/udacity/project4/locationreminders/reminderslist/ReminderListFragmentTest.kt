package com.udacity.project4.locationreminders.reminderslist

import android.content.Context
import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.local.FakeDataSource
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import com.udacity.project4.R
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.not
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var fakeDataSource: FakeDataSource
    private lateinit var viewModel: RemindersListViewModel
    private lateinit var reminderDTO: ReminderDTO
    @Before
    fun setupFragment() {
        stopKoin()
        fakeDataSource = FakeDataSource()
         reminderDTO = ReminderDTO(
             "title",
             "description",
             "location",
             5.0,
             10.0
         )
        viewModel =
            RemindersListViewModel(getApplicationContext(), fakeDataSource)
        val myModule = module {
            single {
                viewModel
            }
        }
        startKoin {
            modules(listOf(myModule))
        }
    }

    @Test
    fun displayFragmentUI() {
        runBlockingTest {
            fakeDataSource.saveReminder(reminderDTO)
            launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
            onView(withId(R.id.noDataTextView)).check(matches(not(isDisplayed())))
            onView(withText(reminderDTO.title)).check(matches(isDisplayed()))
            onView(withText(reminderDTO.location)).check(matches(isDisplayed()))
            onView(withText(reminderDTO.description)).check(matches(isDisplayed()))
        }
    }
    @Test
    fun navigationToSaveReminderFragment() {
        runBlockingTest {
            val nav = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
            val navController = mock(NavController::class.java)
            nav.onFragment {
                Navigation.setViewNavController(it.view!!, navController)
            }
            onView(withId(R.id.addReminderFAB)).perform(click())
            verify(navController).navigate(ReminderListFragmentDirections.toSaveReminder())
        }
    }

    @After
    fun stop() = stopKoin()
//    TODO: test the navigation of the fragments.
//    TODO: test the displayed data on the UI.
//    TODO: add testing for the error messages.
}