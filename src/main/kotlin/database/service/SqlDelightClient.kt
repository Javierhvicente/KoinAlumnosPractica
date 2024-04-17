package database.service

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import database.DatabaseQueries
import database.initDemoEstudiantes
import dev.javierhvicente.database.AppDatabase
import org.lighthousegames.logging.logging

private val logger = logging()

/**
 * Manager de estudiante.db
 * @property databaseUrl conexion de la base de datos con sqlite
 * @property databaseInMemory indica si la base de datos va a estar en memoria
 * @property databaseInitData indica si se van a iniciar los datos de la base de datos
 */
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

    /**
     * Inicia las consultas de Database.sq en memoria
     * @return DatabaseQueries
     */
    private fun initQueries(): DatabaseQueries {

        return if (databaseInMemory.toBoolean()) {
            logger.debug { "SqlDeLightClient - InMemory" }
            JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        } else {
            logger.debug { "SqlDeLightClient - File: ${databaseUrl}" }
            JdbcSqliteDriver(databaseUrl)
        }.let { driver ->
            logger.debug { "Creando Tablas (si es necesario)" }
            AppDatabase.Schema.create(driver)
            AppDatabase(driver)
        }.databaseQueries

    }

    /**
     * Borra los datos que se encuentren en la base de datos e inicia los registros para la base de datos
     */
    fun initialize() {
        if (databaseInitData.toBoolean()) {
            removeAllStudents()
            initDataExamples()
        }
    }

    /**
     * Inserta los datos a la queries mediante una lista interna
     */
    private fun initDataExamples() {
        logger.debug { "Iniciando datos de ejemplo" }
        databaseQueries.transaction {
            demoEstudiantes()
        }
    }

    /**
     * A través de una lista interna inserta los estudiantes en la tabla EstudiantesEntity
     */
    private fun demoEstudiantes() {
        logger.debug { "Datos de ejemplo de Productos" }
        initDemoEstudiantes().forEach {
            databaseQueries.inserIntoEstudiantesEntity(
                nombre = it.nombre,
                edad = it.edad,
            )
        }
    }

    /**
     * Elimina todos los registro de la tabla EstudiantesEntity
     */
    private fun removeAllStudents() {
        logger.debug { "SqlDeLightClient.removeAllData()" }
        databaseQueries.transaction {
            databaseQueries.removeAllStudents()
        }
    }
}