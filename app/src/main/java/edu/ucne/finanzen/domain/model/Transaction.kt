package edu.ucne.finanzen.domain.model

data class Transaction(
    val transactionId: Int = 0,
    val type: TransactionType,
    val amount: Double,
    val category: CategoryType,
    val description: String,
    val date: String
)

enum class TransactionType {
    INCOME, EXPENSE
}

enum class CategoryType {
    // Ingresos
    SALARIO,
    BONIFICACIONES,
    FREELANCE,
    INVERSIONES,
    ALQUILERES,
    REGALOS,
    COMISIONES,
    PROYECTOS_ESPECIALES,
    REEMBOLSOS,
    SUBSIDIOS,

    // Gastos
    ALIMENTACION,
    VIVIENDA,
    TRANSPORTE,
    SALUD,
    ENTRETENIMIENTO,
    ROPA_CALZADO,
    EDUCACION,
    AHORRO_INVERSION,
    COMPRAS,
    CUIDADO_PERSONAL,
    OTROS_GASTOS
}

fun CategoryType.getDisplayName(): String {
    return when (this) {
        // Ingresos
        CategoryType.SALARIO -> "Salario"
        CategoryType.BONIFICACIONES -> "Bonificaciones"
        CategoryType.FREELANCE -> "Freelance"
        CategoryType.INVERSIONES -> "Inversiones"
        CategoryType.ALQUILERES -> "Alquileres"
        CategoryType.REGALOS -> "Regalos"
        CategoryType.COMISIONES -> "Comisiones"
        CategoryType.PROYECTOS_ESPECIALES -> "Proyectos Especiales"
        CategoryType.REEMBOLSOS -> "Reembolsos"
        CategoryType.SUBSIDIOS -> "Subsidios"

        // Gastos
        CategoryType.ALIMENTACION -> "Alimentación"
        CategoryType.VIVIENDA -> "Vivienda"
        CategoryType.TRANSPORTE -> "Transporte"
        CategoryType.SALUD -> "Salud"
        CategoryType.ENTRETENIMIENTO -> "Entretenimiento"
        CategoryType.ROPA_CALZADO -> "Ropa y Calzado"
        CategoryType.EDUCACION -> "Educación"
        CategoryType.AHORRO_INVERSION -> "Ahorro e Inversión"
        CategoryType.COMPRAS -> "Compras"
        CategoryType.CUIDADO_PERSONAL -> "Cuidado Personal"
        CategoryType.OTROS_GASTOS -> "Otros Gastos"
    }
}

