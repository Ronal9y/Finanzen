package edu.ucne.finanzen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.finanzen.data.local.dao.DebtDao
import edu.ucne.finanzen.data.local.entity.DebtEntity
import edu.ucne.finanzen.data.remote.RemoteDataSource
import edu.ucne.finanzen.data.remote.Resource
import edu.ucne.finanzen.data.remote.dto.DebtResponse
import edu.ucne.finanzen.data.repository.DebtRepositoryImpl
import edu.ucne.finanzen.domain.model.CompoundingPeriod
import edu.ucne.finanzen.domain.model.Debt
import edu.ucne.finanzen.domain.model.DebtStatus
import edu.ucne.finanzen.domain.model.InterestType
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
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class DebtRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repo: DebtRepositoryImpl
    private val dao = mockk<DebtDao>(relaxed = true)
    private val remote = mockk<RemoteDataSource>(relaxed = true)

    @Before
    fun setup() {
        repo = DebtRepositoryImpl(dao, remote)
    }

    @Test
    fun `upsertDebt usa id remoto cuando la API responde`() = runTest {
        val local = Debt(
            debtId = 0,
            name = "Banco",
            principalAmount = 5000.0,
            interestRate = 10.0,
            interestType = InterestType.SIMPLE,
            compoundingPeriod = CompoundingPeriod.MONTHLY,
            dueDate = "2026-12-31",
            remainingAmount = 1000.0,
            creditor = "Banco Popular",
            status = DebtStatus.ACTIVE,
            usuarioId = 1
        )
        val apiResp = DebtResponse(
            debtId = 99,
            name = "Banco",
            principalAmount = 5000.0,
            interestRate = 10.0,
            interestType = "SIMPLE",
            compoundingPeriod = "MONTHLY",
            dueDate = "2026-12-31",
            remainingAmount = 1000.0,
            creditor = "Banco Popular",
            status = "ACTIVE",
            penaltyRate = 5.0,
            creationDate = "2025-11-26",
            usuarioId = 1
        )
        coEvery { remote.postDebt(any()) } returns Resource.Success(apiResp)

        repo.upsertDebt(local)

        val slot = slot<DebtEntity>()
        coVerify { dao.upsert(capture(slot)) }
        assertEquals(99, slot.captured.debtId)
    }

    @Test
    fun `deleteDebt lanza excepcion cuando la API falla`() = runTest {
        val debt = Debt(
            debtId = 3,
            name = "",
            principalAmount = 0.0,
            interestRate = null,
            interestType = InterestType.SIMPLE,
            compoundingPeriod = CompoundingPeriod.MONTHLY,
            dueDate = "2025-12-31",
            remainingAmount = 0.0,
            creditor = "",
            status = DebtStatus.ACTIVE,
            usuarioId = 1
        )
        coEvery { remote.deleteDebt(3) } returns Resource.Error("Network")
        assertFailsWith<Exception> { repo.deleteDebt(debt) }
    }
}