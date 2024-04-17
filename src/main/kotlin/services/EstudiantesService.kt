package services

import errors.EstudianteError
import models.Estudiante
import com.github.michaelbull.result.Result

interface EstudiantesService {
    fun getAll(): Result<List<Estudiante>, EstudianteError>
    fun getById(id: Long): Result<Estudiante, EstudianteError>
    fun create(estudiante: Estudiante): Result<Estudiante, Nothing>
    fun update(id: Long, estudiante: Estudiante): Result<Estudiante, EstudianteError>
    fun delete(id: Long): Result<Estudiante, EstudianteError>
}