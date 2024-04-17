import models.Estudiante
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.KoinAppDeclaration
import services.EstudiantesService

class EstudiantesApp: KoinComponent {
    val contDefault: EstudiantesService by inject()
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
