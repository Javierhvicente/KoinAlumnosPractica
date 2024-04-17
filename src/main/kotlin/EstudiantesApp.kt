import models.Estudiante
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.KoinAppDeclaration
import services.EstudiantesService

/**
 * Clase que ejecuta los métodos de la aplicación
 */
class EstudiantesApp: KoinComponent {
    val contDefault: EstudiantesService by inject()

    /**
     * Corre una serie de métodos de la aplicación.
     * getAll, create, delete
     */
    fun run(){
        val lista=contDefault.getAll().value
        lista.forEach { println(it) }
        val newStudent = Estudiante(nombre = "Carlos", edad = 29)
        contDefault.create(newStudent)
        contDefault.getAll().value.forEach { println(it) }
        val newStudent2 = Estudiante(nombre = "Lucía", edad = 22)
        contDefault.create(newStudent2)
        contDefault.getAll().value.forEach { println(it) }
        contDefault.delete(5)
        contDefault.getAll().value.forEach { println(it) }
    }
}
