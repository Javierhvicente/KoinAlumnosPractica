package database

import models.Estudiante

/**
 * Crea una lista de estudiantes
 */
fun initDemoEstudiantes() = listOf(
    Estudiante(nombre = "Manuel", edad = 19, is_deleted = false),
    Estudiante(nombre = "Dani", edad = 18, is_deleted = false),
    Estudiante(nombre = "Jesu", edad = 20, is_deleted = false)
    )