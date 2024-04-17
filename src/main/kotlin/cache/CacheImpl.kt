package cache

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import org.lighthousegames.logging.logging
import cache.error.CacheError

private val logger = logging()

/**
 * Cache de almacenamiento genérico
 * @property size indica el número de registros que admite la cache
 */
open class CacheImpl<K, T>(
    private val size: String
) : Cache<K, T> {
    private val cache = mutableMapOf<K, T>()

    /**
     * Obtiene un registro de la cache mediante su key
     * @param key la clave del registro que queremos buscar
     * @return El registro si se ha encontrado y un CacheError si no se encuentra
     */
    override fun get(key: K): Result<T, CacheError> {
        logger.debug { "Obteniendo valor de la cache" }
        return if (cache.containsKey(key)) {
            Ok(cache.getValue(key))
        } else {
            Err(CacheError("No existe el valor en la cache"))
        }
    }

    /**
     * Introduce un registro en la cache. En caso de estar llena borrara el primer registro de la cola
     * @param key la clave del registro a introducir
     * @param value el registro a introducir
     * @return el registro que introducimos
     */
    override fun put(key: K, value: T): Result<T, Nothing> {
        logger.debug { "Guardando valor en la cache" }
        if (cache.size >= size.toInt() && !cache.containsKey(key)) {
            logger.debug { "Eliminando valor de la cache" }
            cache.remove(cache.keys.first())
        }
        cache[key] = value
        return Ok(value)
    }

    /**
     * Elimina un registro de la cache
     * @param key la clave del registro a eliminar
     * @return devuelve el registro eliminado y en caso de no encontrar el registro devuelve un CacheError
     */
    override fun remove(key: K): Result<T, CacheError> {
        logger.debug { "Eliminando valor de la cache" }
        return if (cache.containsKey(key)) {
            Ok(cache.remove(key)!!)
        } else {
            Err(CacheError("No existe el valor en la cache"))
        }
    }

    /**
     * Elimina todos los registro de la cache
     * @return unit
     */
    override fun clear(): Result<Unit, Nothing> {
        logger.debug { "Limpiando cache" }
        cache.clear()
        return Ok(Unit)
    }
}