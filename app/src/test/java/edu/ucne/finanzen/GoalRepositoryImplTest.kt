package edu.ucne.finanzen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.finanzen.data.local.dao.GoalDao
import edu.ucne.finanzen.data.local.entity.GoalEntity
import edu.ucne.finanzen.data.remote.RemoteDataSource
import edu.ucne.finanzen.data.remote.Resource
import edu.ucne.finanzen.data.remote.dto.GoalResponse
import edu.ucne.finanzen.data.repository.GoalRepositoryImpl
import edu.ucne.finanzen.domain.model.Goal
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class GoalRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repo: GoalRepositoryImpl
    private val dao = mockk<GoalDao>(relaxed = true)
    private val remote = mockk<RemoteDataSource>(relaxed = true)

    @Before
    fun setup() {
        repo = GoalRepositoryImpl(dao, remote)
    }

    @Test
    fun `upsertGoal actualiza id con el de la API`() = runTest {
        val goal = Goal(
            goalId = 0,
            name = "Viaje",
            targetAmount = 1000.0,
            currentAmount = 0.0,
            deadline = "2026-12-31",
            usuarioId = 1,
            description = "Europa"
        )
        val apiResp = GoalResponse(
            goalId = 77,
            name = "Viaje",
            targetAmount = 1000.0,
            currentAmount = 0.0,
            deadline = "2026-12-31",
            description = "Europa",
            usuarioId = 1
        )
        coEvery { remote.postGoal(any()) } returns Resource.Success(apiResp)

        repo.upsertGoal(goal)

        val slot = slot<GoalEntity>()
        coVerify { dao.upsert(capture(slot)) }
        assertEquals(77, slot.captured.goalId)
    }

    @Test
    fun `getTotalCompletionPercentage calcula bien`() = runTest {

        val list = listOf(
            GoalEntity(0, "A", 100.0, 50.0, "2026-12-31", 1, "desc"),
            GoalEntity(0, "B", 200.0, 50.0, "2026-12-31", 1, "desc")
        )
        coEvery { dao.getAll() } returns list

        val pct = repo.getTotalCompletionPercentage(1)

        assertEquals(33.333, pct, 0.01) // (50+50)/(100+200) = 100/300
    }
}