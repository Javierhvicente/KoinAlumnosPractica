package services

import cache.Cache
import cache.CacheImpl
import com.github.michaelbull.result.*
import errors.EstudianteError
import models.Estudiante
import org.lighthousegames.logging.logging
import repositories.EstudiantesRepository

private val logger = logging()

/**
 * Servicio que maneja el repositorio de estudiante y una cache
 * @param estudiantesRepository el repositorio de estudiante
 */
class EstudiantesServiceImpl(
    private val estudiantesRepository: EstudiantesRepository,
    private val cache: Cache<Long, Estudiante>
) : EstudiantesService {
    override fun getAll(): Result<List<Estudiante>, EstudianteError> {
        logger.debug { "Obteniendo todos los productos" }
        return Ok(estudiantesRepository.findAll())
    }

    /**
     * Busca a un estudiante por su id
     * @param id el id del estudiante que buscamos
     * @return Devuelve el estudiante si lo ecnuentra y si no devuelve EstudianteError
     */
    override fun getById(id: Long): Result<Estudiante, EstudianteError> {
        logger.debug { "Obteniendo producto por id: $id" }
        return cache.get(id).mapBoth(
            success = {
                logger.debug { "Estudiante encontrado en la cache" }
                Ok(it)
            },
            failure = {
                logger.debug { "Estudiante no encontrado en la cache" }
                estudiantesRepository.findById(id)
                    ?.let { Ok(it) }
                    ?: Err(EstudianteError.EstudianteNoEncontrado("Estudiante no encontrado con id: $id"))
            }
        )
    }

    /**
     * Guarda un pesonaje
     * @param estudiante el estudiante que queremos guardar
     * @return devuelve un Estudiante
     */
    override fun create(estudiante: Estudiante): Result<Estudiante, Nothing> {
        logger.debug { "Guardando estudiante: $estudiante" }
        return Ok(estudiantesRepository.save(estudiante))
    }

    /**
     * Actualiza un estudiante que haya sido guardado
     * @param id el id del estudiante que queremos actualizar
     * @param estudiante el estudiante actualizado
     * @return el estudiante actualizado en caso de haber encontrado el id y
     * un EstudianteError en caso de no encontrarlo
     */
    override fun update(id: Long, estudiante: Estudiante): Result<Estudiante, EstudianteError> {
        logger.debug { "Actualizando estudiante por id: $id" }
        return estudiantesRepository.update(id, estudiante)
            ?.let { Ok(it) }
            ?: Err(EstudianteError.EstudianteNoActualizado("Estudiante no actualizado con id: $id")).andThen {
                cache.put(id, estudiante)
            }
    }

    /**
     * Borra un estudiante
     * @param id el id del estudiante que queremos borrar
     * @return el estudiante que queremos borrar en caso de encontrar el id yç
     * un EstudianteError en caso de no encontrarlo
     */
    override fun delete(id: Long): Result<Estudiante, EstudianteError> {
        logger.debug { "Borrando estudiante por id: $id" }
        return estudiantesRepository.delete(id)
            ?.let {
                cache.remove(id)
                Ok(it)
            }
            ?: Err(EstudianteError.EstudianteNoEliminado("Estudiante no eliminado con id: $id"))
    }

}