package com.udacity.project4.locationreminders.reminderslist

import android.content.Context
import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.local.RemindersLocalRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import com.udacity.project4.R
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.util.DataBindingIdlingResource
import com.udacity.project4.util.monitorFragment
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.not
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest:AutoCloseKoinTest() {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var fakeDataSource: FakeDataSource
    private lateinit var viewModel: RemindersListViewModel
    private lateinit var reminderDTO: ReminderDTO
    private val dataBindingIdlingResource = DataBindingIdlingResource()
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
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }
// Test the displayed UI
    @Test
    fun displayFragmentUI() {
        runBlockingTest {
            fakeDataSource.saveReminder(reminderDTO)
           val fragment= launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
            dataBindingIdlingResource.monitorFragment(fragment)
            onView(withId(R.id.noDataTextView)).check(matches(not(isDisplayed())))
            onView(withText(reminderDTO.title)).check(matches(isDisplayed()))
            onView(withText(reminderDTO.location)).check(matches(isDisplayed()))
            onView(withText(reminderDTO.description)).check(matches(isDisplayed()))
        }
    }
    // Test navigation to Save Reminder Fragment
    @Test
    fun navigationToSaveReminderFragment() {
        runBlockingTest {
            val nav = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.AppTheme)
            dataBindingIdlingResource.monitorFragment(nav)
            val navController = mock(NavController::class.java)
            nav.onFragment {
                Navigation.setViewNavController(it.view!!, navController)
            }
            onView(withId(R.id.addReminderFAB)).perform(click())
            verify(navController).navigate(ReminderListFragmentDirections.toSaveReminder())
        }
    }

    @After
    fun stop() {
        stopKoin()
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }
}
