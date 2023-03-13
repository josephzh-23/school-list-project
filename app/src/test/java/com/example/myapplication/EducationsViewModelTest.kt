package com.example.myapplication

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myapplication.Data.SchoolSATScore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class EducationsViewModelTest {

    @Mock
    lateinit var repo: SchoolRepository

    @Mock
    lateinit var app: Application

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `Happy path - should return Success when network request is successful`() = runTest {
        val viewModel = EducationsViewModel(app, repo)
        Mockito.`when`(repo.getSATScoreBySchool("01M292")).thenReturn(
            Resource.Success(SchoolSATScore("01M292"))
        )
        viewModel.getSATScoreBySchool("01M292")
        testDispatcher.scheduler.advanceUntilIdle()
        val res = viewModel.scores.getOrAwaitValue()
        assertEquals(res.data?.dbn, "01M292")
        assertEquals(true, res is Resource.Success<SchoolSATScore>)

    }

    @Test
    fun `should return Error when network request is unsuccessful`() = runTest {
        Mockito.`when`(repo.getSATScoreBySchool("")).thenReturn(
            Resource.Error("An empty dbn is requested")
        )
        val viewModel = EducationsViewModel(app, repo)

        viewModel.getSATScoreBySchool("")
        testDispatcher.scheduler.advanceUntilIdle()
        val res = viewModel.scores.getOrAwaitValue()
        assertEquals(res.message, "An empty dbn is requested")
        assertEquals(true, res is Resource.Error<SchoolSATScore>)
    }


}