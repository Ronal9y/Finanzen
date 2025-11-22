package edu.ucne.finanzen.domain.usecases.Debts

import edu.ucne.finanzen.domain.model.CompoundingPeriod
import edu.ucne.finanzen.domain.model.Debt
import edu.ucne.finanzen.domain.model.DebtStatus
import edu.ucne.finanzen.domain.repository.DebtRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.pow

class GetDebtsWithInterestUseCase @Inject constructor(
    private val repo: DebtRepository
) {
    operator fun invoke(usuarioId: Int): Flow<List<Debt>> =
        repo.getAllDebts(usuarioId).map { list ->
            list.map { debt ->
                debt.copy(remainingAmount = calculate(debt))
            }
        }

    private fun calculate(d: Debt): Double {
        if (d.interestRate == null || d.status != DebtStatus.ACTIVE) return d.remainingAmount

        val days = daysBetween(today(), d.dueDate)
        val periods = when (d.compoundingPeriod) {
            CompoundingPeriod.DAILY  -> days.toDouble()
            CompoundingPeriod.WEEKLY -> days / 7.0
            CompoundingPeriod.MONTHLY-> days / 30.0
            CompoundingPeriod.YEARLY -> days / 365.0
        }.coerceAtLeast(0.0)

        val ratePerPeriod = (d.interestRate!! / 100.0) / when (d.compoundingPeriod) {
            CompoundingPeriod.DAILY  -> 365.0
            CompoundingPeriod.WEEKLY -> 52.0
            CompoundingPeriod.MONTHLY-> 12.0
            CompoundingPeriod.YEARLY -> 1.0
        }

        val factor = (1 + ratePerPeriod).pow(periods)
        return d.principalAmount * factor
    }

    private fun today() =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    private fun daysBetween(start: String, end: String): Long {
        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return TimeUnit.DAYS.convert(
            fmt.parse(end).time - fmt.parse(start).time,
            TimeUnit.MILLISECONDS
        )
    }
}