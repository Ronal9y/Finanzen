package edu.ucne.finanzen.domain.utils

import edu.ucne.finanzen.domain.model.CategoryType
import edu.ucne.finanzen.domain.model.TransactionType

object CategoryUtils {

    fun getIncomeCategories(): List<CategoryType> = listOf(
        CategoryType.SALARIO,
        CategoryType.BONIFICACIONES,
        CategoryType.FREELANCE,
        CategoryType.INVERSIONES,
        CategoryType.ALQUILERES,
        CategoryType.REGALOS,
        CategoryType.COMISIONES,
        CategoryType.PROYECTOS_ESPECIALES,
        CategoryType.REEMBOLSOS,
        CategoryType.SUBSIDIOS
    )

    fun getExpenseCategories(): List<CategoryType> = listOf(
        CategoryType.ALIMENTACION,
        CategoryType.VIVIENDA,
        CategoryType.TRANSPORTE,
        CategoryType.SALUD,
        CategoryType.ENTRETENIMIENTO,
        CategoryType.ROPA_CALZADO,
        CategoryType.EDUCACION,
        CategoryType.AHORRO_INVERSION,
        CategoryType.COMPRAS,
        CategoryType.CUIDADO_PERSONAL,
        CategoryType.OTROS_GASTOS
    )

    fun getCategoriesByType(type: TransactionType): List<CategoryType> {
        return when (type) {
            TransactionType.INCOME -> getIncomeCategories()
            TransactionType.EXPENSE -> getExpenseCategories()
        }
    }
}