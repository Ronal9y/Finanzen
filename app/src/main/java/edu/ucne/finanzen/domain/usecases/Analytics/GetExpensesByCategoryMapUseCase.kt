package edu.ucne.finanzen.domain.usecases.Analytics

import edu.ucne.finanzen.domain.model.TransactionType
import edu.ucne.finanzen.domain.repository.TransactionRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetExpensesByCategoryMapUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(usuarioId: Int): Flow<Map<String, Double>> =
        repository.getAllTransactions(usuarioId).map { list ->
            list
                .filter { it.type == TransactionType.EXPENSE }
                .groupBy { it.category.name }
                .mapValues { (_, txs) -> txs.sumOf { it.amount } }
                .filterValues { it > 0.0 }
        }
}
