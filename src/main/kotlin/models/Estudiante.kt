package models


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