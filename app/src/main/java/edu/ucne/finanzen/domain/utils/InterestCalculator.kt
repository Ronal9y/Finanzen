package edu.ucne.finanzen.domain.utils

import edu.ucne.finanzen.domain.model.CompoundingPeriod
import edu.ucne.finanzen.domain.model.InterestType
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.pow
object InterestCalculator {
    fun calculateCurrentBalance(
        principal: Double,
        interestRate: Double?,
        interestType: InterestType,
        compoundingPeriod: CompoundingPeriod,
        creationDate: String,
        currentDate: String = getCurrentDate(),
        penaltyRate: Double = 0.0,
        isOverdue: Boolean = false
    ): Double {
        if (interestRate == null || interestRate == 0.0) {
            return principal
        }

        val daysElapsed = diasEntre(creationDate, currentDate)
        if (daysElapsed <= 0) return principal

        val annualRate = interestRate / 100.0
        val penalty = if (isOverdue) penaltyRate / 100.0 else 0.0
        val totalRate = annualRate + penalty

        return when (interestType) {
            InterestType.SIMPLE -> calculateSimpleInterest(
                principal, totalRate, daysElapsed, compoundingPeriod
            )
            InterestType.COMPOUND -> calculateCompoundInterest(
                principal, totalRate, daysElapsed, compoundingPeriod
            )
        }
    }

    private fun calculateSimpleInterest(
        principal: Double,
        annualRate: Double,
        daysElapsed: Long,
        period: CompoundingPeriod
    ): Double {
        val periodsPerYear = when (period) {
            CompoundingPeriod.DAILY -> 365.0
            CompoundingPeriod.WEEKLY -> 52.0
            CompoundingPeriod.MONTHLY -> 12.0
            CompoundingPeriod.YEARLY -> 1.0
        }

        val ratePerPeriod = annualRate / periodsPerYear
        val numberOfPeriods = when (period) {
            CompoundingPeriod.DAILY -> daysElapsed.toDouble()
            CompoundingPeriod.WEEKLY -> daysElapsed / 7.0
            CompoundingPeriod.MONTHLY -> daysElapsed / 30.0
            CompoundingPeriod.YEARLY -> daysElapsed / 365.0
        }

        return principal * (1 + ratePerPeriod * numberOfPeriods)
    }

    private fun calculateCompoundInterest(
        principal: Double,
        annualRate: Double,
        daysElapsed: Long,
        period: CompoundingPeriod
    ): Double {
        val periodsPerYear = when (period) {
            CompoundingPeriod.DAILY -> 365.0
            CompoundingPeriod.WEEKLY -> 52.0
            CompoundingPeriod.MONTHLY -> 12.0
            CompoundingPeriod.YEARLY -> 1.0
        }

        val ratePerPeriod = annualRate / periodsPerYear
        val numberOfPeriods = when (period) {
            CompoundingPeriod.DAILY -> daysElapsed.toDouble()
            CompoundingPeriod.WEEKLY -> daysElapsed / 7.0
            CompoundingPeriod.MONTHLY -> daysElapsed / 30.0
            CompoundingPeriod.YEARLY -> daysElapsed / 365.0
        }

        return principal * (1 + ratePerPeriod).pow(numberOfPeriods)
    }


    fun calculateRenewalAmount(
        currentBalance: Double,
        penaltyRate: Double,
        additionalPenalty: Double = 0.0
    ): Double {
        val totalPenaltyRate = (penaltyRate + additionalPenalty) / 100.0
        return currentBalance * (1 + totalPenaltyRate)
    }

    fun getCurrentDate(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    private fun diasEntre(start: String, end: String): Long {
        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val startDate = fmt.parse(start) ?: return 0
        val endDate = fmt.parse(end) ?: return 0
        val ms = endDate.time - startDate.time
        return TimeUnit.DAYS.convert(ms, TimeUnit.MILLISECONDS)
    }
}