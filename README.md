# FinanZen

Aplicación Android que permite llevar el control total de tus ingresos, gastos, presupuestos, metas de ahorro y deudas.
Es una aplicacion desarrollada en Kotlin con Android Studio para facilitar la gestion de gastos financieros.
Este README ofrece una descripcion general del proyecto,
instrucciones de uso e informacion sobre las caracteristicas principales

## Descripción

FinanZen es una app offline-first que te ayuda a:
- Registrar ingresos y gastos por categoría.
- Crear presupuestos mensuales con alertas de límite.
- Establecer metas de ahorro y ver su progreso.
- Controlar deudas con cálculo de intereses simples/compuestos.
- Recibir notificaciones de vencimientos y límites de presupuesto.
- Sincronizar tus datos con la nube cuando tengas red

La aplicación ofrece una interfaz intuitiva facil para usar controlar gastos, registrar ingresos y realizar un seguimiento del balance financiero

## Características

- ✅ Integración de API con Retrofit
- ✅ MockK para simulaciones
- ✅ Uso de ViewModel para gestión del estado
- ✅ Navegación entre pantallas con Jetpack Compose Navigation
- ✅ Material Design 3

## Tecnologías Utilizadas

- **Jetpack Compose**: UI moderna y declarativa
- **Jetpack Navigation**: Navegación entre pantallas
- **Retrofit**: Cliente HTTP para consumir la API
- **Storage**: DataStore 

## Estructura del Código

Patrón MVI + Clean Architecture + Capas unidireccionales

- **Data Models**: `Transaction`, `Budget`, `Goal`, `Debt`, `Usuario`
- **MainActivity**: Activity principal
- **Composables**:
  - `LoginScreen `: formulario Material3 con validación y botón de crear usuario.
  - `TransactionListScreen `: lista con filtros por tipo/categoría y FAB para agregar.
  - `BudgetListScreen `: cards con barra de progreso y alerta visual cuando se supera el límite.
  - `GoalListScreen `: meta, fecha límite (formateada), barra de progreso y diálogo para agregar ahorros parciales.
  - `DebtListScreen `: card por deuda, fecha limpia (take(10)), botones Abonar y Renovar con cálculo de intereses.

## Requisitos

- Android Studio Arctic Fox o superior
- Android SDK API 24 o superior
- Kotlin 1.9.10 o superior
- Conexión a Internet para acceder a la API de GitHub

## Instalación

1. Clonar el repositorio
2. Abrir el proyecto en Android Studio
3. Ejecutar la aplicación en un emulador o dispositivo físico

## Uso
1. Crea tu usuario en la pantalla de Login.
2. Registra tus primeros ingresos/gastos.
3. Establece un presupuesto mensual por categoría.
4. Crea metas de ahorro y ve su progreso.
5. Registra deudas y recibe avisos antes del vencimiento.
6. ¡Listo! La app sincroniza tus cambios cuando haya red.

> [!NOTE]  
> La aplicación requiere permisos de Internet.
