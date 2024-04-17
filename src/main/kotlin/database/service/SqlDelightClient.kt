package database.service

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import config.Config
import database.DatabaseQueries
import database.initDemoEstudiantes
import dev.javierhvicente.database.AppDatabase
import org.lighthousegames.logging.logging

private val logger = logging()

class SqlDeLightClient(
    private val databaseUrl: String,
    private val databaseInMemory: String,
    private val databaseInitData: String,
) {
    val databaseQueries: DatabaseQueries by lazy { initQueries() }

    init {
        logger.debug { "Inicializando el gestor de Bases de Datos con SQLDelight" }
        initialize()
    }

    private fun initQueries(): DatabaseQueries {

        return if (databaseInMemory.toBoolean()) {
            //logger.debug { "SqlDeLightClient - InMemory" }
            JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        } else {
            //logger.debug { "SqlDeLightClient - File: ${databaseUrl}" }
            JdbcSqliteDriver(databaseUrl)
        }.let { driver ->
            // Creamos la base de datos
            //logger.debug { "Creando Tablas (si es necesario)" }
            AppDatabase.Schema.create(driver)
            AppDatabase(driver)
        }.databaseQueries

    }

    fun initialize() {
        if (Config.databaseInitData) {
            removeAllStudents()
            initDataExamples()
        }
    }

    private fun initDataExamples() {
        logger.debug { "Iniciando datos de ejemplo" }
        databaseQueries.transaction {
            demoEstudiantes()
        }
    }

    private fun demoEstudiantes() {
        logger.debug { "Datos de ejemplo de Productos" }
        initDemoEstudiantes().forEach {
            databaseQueries.inserIntoEstudiantesEntity(
                nombre = it.nombre,
                edad = it.edad,
            )
        }
    }

    private fun removeAllStudents() {
        logger.debug { "SqlDeLightClient.removeAllData()" }
        databaseQueries.transaction {
            databaseQueries.removeAllStudents()
        }
    }
}