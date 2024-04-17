package repositories

import mappers.toEstudiante
import models.Estudiante
import org.lighthousegames.logging.logging
import database.service.SqlDeLightClient
import java.time.LocalDateTime

private val logger = logging()

/**
 * Repositorio que se comunica con la base de datos de estudiante.db
 * @property dataBaseManager Manager de la base de datos estudiante.db
 */
class EstudianteRepositoryImpl(
    private val dataBaseManager: SqlDeLightClient
) : EstudiantesRepository {

    private val db = dataBaseManager.databaseQueries

    /**
     * Obtiene todos los estudiantes de la base de datos
     * @return una lista de estudiantes
     */
    override fun findAll(): List<Estudiante> {
        logger.debug { "Obteniendo todos los productos" }
        return db.selectAllStudents()
            .executeAsList()
            .map { it.toEstudiante() }
    }

    /**
     * Obtiene un estudiantes por id
     * @param id el id del registro que queremos encontrar
     * @return Estudiante si se encuentra o null en caso de no encontrarlo
     */
    override fun findById(id: Long): Estudiante? {
        logger.debug { "Obteniendo producto por id: $id" }
        return db.selectEstudiantesById(id)
            .executeAsOneOrNull()
            ?.toEstudiante()
    }

    /**
     * Guarda un estudiante en la base de datos
     * @param estudiante el estudiante que queremos guardar
     * @return El estudiante que hemos guardado
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
     * @param id el id del registro que queremos actualizar
     * @param estudiante el nuevo registro actualizado
     * @return El estudiante actualizado en caso de haber encotrado el id o null si no se encuentra
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
     * @param id id del registro que queremos borrar
     * @return El estudiante si hemos encontrado el id y null si no lo encuentra
     */
    override fun delete(id: Long): Estudiante? {
        logger.debug { "Borrando producto por id: $id" }
        val result = this.findById(id) ?: return null
        db.deleteEstudiante(id)
        return result
    }
}