package mappers

import database.EstudianteEntity
import models.Estudiante

/**
 * Mapea un EstudianteEntity en un Estudiante
 * @return Estudiante
 */
fun EstudianteEntity.toEstudiante(): Estudiante {
    return Estudiante(
        id = this.id,
        nombre = this.nombre,
        edad = this.edad,
        is_deleted = this.is_deleted.toInt() == 1
    )
}