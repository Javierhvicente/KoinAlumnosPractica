import database.initDemoEstudiantes
import di.EstudiantesModule
import org.koin.core.context.startKoin
import org.koin.fileProperties
import org.koin.test.verify.verify
import kotlin.jvm.internal.Intrinsics.Kotlin

fun main(args: Array<String>) {
    println("Iniciando Koin")
    startKoin {
        fileProperties("/config.properties")
        EstudiantesModule.verify(extraTypes = listOf(Boolean::class))
        modules(EstudiantesModule)
    }
    val app = EstudiantesApp()
    app.run()
}