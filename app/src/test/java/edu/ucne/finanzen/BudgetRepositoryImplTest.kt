package edu.ucne.finanzen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.finanzen.data.local.dao.BudgetDao
import edu.ucne.finanzen.data.local.entity.BudgetEntity
import edu.ucne.finanzen.data.remote.RemoteDataSource
import edu.ucne.finanzen.data.remote.Resource
import edu.ucne.finanzen.data.repository.BudgetRepositoryImpl
import edu.ucne.finanzen.domain.model.Budget
import edu.ucne.finanzen.domain.model.CategoryType
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class BudgetRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: BudgetRepositoryImpl
    private val budgetDao = mockk<BudgetDao>(relaxed = true)
    private val remote = mockk<RemoteDataSource>(relaxed = true)

    @Before
    fun setup() {
        repository = BudgetRepositoryImpl(budgetDao, remote)
    }

    @Test
    fun `upsertBudget guarda localmente aunque la API falle`() = runTest {
        val budget = Budget(
            budgetId = 1,
            category = CategoryType.ALIMENTACION,
            month = "2025-11",
            limit = 1000.0,
            spent = 0.0,
            usuarioId = 1,
            alertThreshold = 80
        )
        val slot = slot<BudgetEntity>()
        coEvery { remote.putBudget(any(), any()) } returns Resource.Error("Timeout")
        coEvery { budgetDao.upsert(capture(slot)) } just Runs

        repository.upsertBudget(budget)

        coVerify { budgetDao.upsert(any()) }
        assertEquals(1, slot.captured.budgetId)
    }

    @Test
    fun `deleteBudgetById borra local incluso si la API falla`() = runTest {
        coEvery { remote.deleteBudget(5) } returns Resource.Error("404")
        repository.deleteBudgetById(5)
        coVerify { budgetDao.deleteById(5) }
    }
}