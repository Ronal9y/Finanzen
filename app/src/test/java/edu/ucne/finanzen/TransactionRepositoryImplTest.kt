package edu.ucne.finanzen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import edu.ucne.finanzen.data.local.dao.TransactionDao
import edu.ucne.finanzen.data.local.entity.TransactionEntity
import edu.ucne.finanzen.data.remote.RemoteDataSource
import edu.ucne.finanzen.data.remote.Resource
import edu.ucne.finanzen.data.remote.dto.TransactionResponse
import edu.ucne.finanzen.data.repository.TransactionRepositoryImpl
import edu.ucne.finanzen.domain.model.CategoryType
import edu.ucne.finanzen.domain.model.Transaction
import edu.ucne.finanzen.domain.model.TransactionType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@ExperimentalCoroutinesApi
class TransactionRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repo: TransactionRepositoryImpl
    private val dao = mockk<TransactionDao>(relaxed = true)
    private val remote = mockk<RemoteDataSource>(relaxed = true)

    @Before
    fun setup() {
        repo = TransactionRepositoryImpl(dao, remote)
    }

    @Test
    fun `upsertTransaction usa id devuelto por la API`() = runTest {
        val tx = Transaction(
            transactionId = 0,
            usuarioId = 1,
            type = TransactionType.EXPENSE,
            amount = 10.0,
            category = CategoryType.ENTRETENIMIENTO,
            description = "Cine",
            date = "2025-11-26"
        )
        val apiResp = TransactionResponse(
            transactionId = 55,
            type = "EXPENSE",
            amount = 10.0,
            category = "ENTRETENIMIENTO",
            description = "Cine",
            date = "2025-11-26",
            usuarioId = 1
        )
        coEvery { remote.postTransaction(any()) } returns Resource.Success(apiResp)

        repo.upsertTransaction(tx)

        val slot = slot<TransactionEntity>()
        coVerify { dao.upsert(capture(slot)) }
        assertEquals(55, slot.captured.transactionId)
    }

    @Test
    fun `deleteTransactionById tira excepcion cuando la API falla`() = runTest {
        coEvery { remote.deleteTransaction(7) } returns Resource.Error("401")
        assertFailsWith<Exception> { repo.deleteTransactionById(7) }
    }

    @Test
    fun `getBalance calcula ingresos menos gastos`() = runTest {
        val list = listOf(
            TransactionEntity(
                transactionId = 1,
                usuarioId = 1,
                amount = 100.0,
                description = "Sueldo",
                type = TransactionType.INCOME,
                category = CategoryType.SALARIO,
                date = "2025-11-26"
            ),
            TransactionEntity(
                transactionId = 2,
                usuarioId = 1,
                amount = 30.0,
                description = "Comida",
                type = TransactionType.EXPENSE,
                category = CategoryType.ALIMENTACION,
                date = "2025-11-26"
            )
        )
        coEvery { dao.observeAll() } returns flowOf(list)

        val balance = repo.getBalance(1)

        assertEquals(70.0, balance)
    }
}