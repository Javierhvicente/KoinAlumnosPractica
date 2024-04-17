package repositories

import mappers.toEstudiante
import models.Estudiante
import org.lighthousegames.logging.logging
import database.service.SqlDeLightClient
import java.time.LocalDateTime

private val logger = logging()


class EstudianteRepositoryImpl(
    private val dataBaseManager: SqlDeLightClient
) : EstudiantesRepository {

    private val db = dataBaseManager.databaseQueries

    /**
     * Obtiene todos los estudiantes de la base de datos
     */
    override fun findAll(): List<Estudiante> {
        logger.debug { "Obteniendo todos los productos" }
        return db.selectAllStudents()
            .executeAsList()
            .map { it.toEstudiante() }
    }

    /**
     * Obtiene un estudiantes por id
     */
    override fun findById(id: Long): Estudiante? {
        logger.debug { "Obteniendo producto por id: $id" }
        return db.selectEstudiantesById(id)
            .executeAsOneOrNull()
            ?.toEstudiante()
    }

    /**
     * Guarda un estudiante en la base de datos
     */
    override fun save(estudiante: Estudiante): Estudiante {
        logger.debug { "Guardando producto: $estudiante" }
        db.transaction {
            db.inserIntoEstudiantesEntity(
                nombre = estudiante.nombre,
                edad = estudiante.edad,
            )
        }
        return db.selectLastInserted()
            .executeAsOne()
            .toEstudiante()
    }

    /**
     * Actualiza un estudiante en la base de datos
     */
    override fun update(id: Long, estudiante: Estudiante): Estudiante? {
        logger.debug { "Actualizando producto por id: $id" }
        var result = this.findById(id) ?: return null
        val timeStamp = LocalDateTime.now()

        result = result.copy(
            nombre = estudiante.nombre,
            edad = estudiante.edad,
            is_deleted = estudiante.is_deleted,
        )

        db.updateEstudiante(
            nombre = result.nombre,
            edad = result.edad,
            is_deleted = if (result.is_deleted) 1 else 0,
            id = estudiante.id,
        )
        return result
    }

    /**
     * Borra un estudiante de la base de datos
     */
    override fun delete(id: Long): Estudiante? {
        logger.debug { "Borrando producto por id: $id" }
        val result = this.findById(id) ?: return null
        db.deleteEstudiante(id)
        return result
    }
}