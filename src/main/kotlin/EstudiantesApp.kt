import models.Estudiante
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.dsl.KoinAppDeclaration
import services.EstudiantesService

class EstudiantesApp: KoinComponent {
    val contDefault: EstudiantesService by inject()
    fun run(){
        contDefault.create(
            Estudiante(
                id = 10,
                nombre = "Juan",
                edad = 20
            )
        )

        val lista=contDefault.getAll().value
        lista.forEach { println(it) }
    }
}
