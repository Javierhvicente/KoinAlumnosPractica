package models

/**
 * Representa a un estudiante
 * @property id id del estudiante para busqueda
 * @property nombre nombre del estudiante
 * @property edad edad del estudiante
 * @property is_deleted indica si el estudiante ha sufrido un borrado lógico
 */
data class Estudiante(
    val id: Long = -1,
    val nombre: String,
    val edad: Long,
    val is_deleted: Boolean = false,

    ) {
    override fun toString(): String {
        return "Estudiante(nombre=$nombre, edad=$edad)"
    }
}