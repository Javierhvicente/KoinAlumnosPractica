package services

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import errors.EstudianteError
import models.Estudiante
import org.lighthousegames.logging.logging
import repositories.EstudiantesRepository
import com.github.michaelbull.result.Result

private val logger = logging()
class EstudiantesServiceImpl(
    private val estudiantesRepository: EstudiantesRepository
) : EstudiantesService {
    override fun getAll(): Result<List<Estudiante>, EstudianteError> {
        logger.debug { "Obteniendo todos los productos" }
        return Ok(estudiantesRepository.findAll())
    }

    override fun getById(id: Long): Result<Estudiante, EstudianteError> {
        logger.debug { "Obteniendo producto por id: $id" }
        return estudiantesRepository.findById(id)
            ?.let { Ok(it) }
            ?: Err(EstudianteError.EstudianteNoEncontrado("Estudiante no encontrado con id: $id"))
    }

    override fun create(estudiante: Estudiante): Result<Estudiante, EstudianteError> {
        logger.debug { "Guardando estudiante: $estudiante" }
        return Ok(estudiantesRepository.save(estudiante))
    }

    override fun update(id: Long, estudiante: Estudiante): Result<Estudiante, EstudianteError> {
        logger.debug { "Actualizando estudiante por id: $id" }
        return estudiantesRepository.update(id, estudiante)
            ?.let { Ok(it) }
            ?: Err(EstudianteError.EstudianteNoActualizado("Estudiante no actualizado con id: $id"))
    }

    override fun delete(id: Long): Result<Estudiante, EstudianteError> {
        logger.debug { "Borrando estudiante por id: $id" }
        return estudiantesRepository.delete(id)
            ?.let { Ok(it) }
            ?: Err(EstudianteError.EstudianteNoEliminado("Estudiante no eliminado con id: $id"))
    }

}