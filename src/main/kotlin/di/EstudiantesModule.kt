package di

import cache.Cache
import cache.CacheImpl
import database.service.SqlDeLightClient
import models.Estudiante
import org.koin.dsl.module
import repositories.EstudianteRepositoryImpl
import repositories.EstudiantesRepository
import services.EstudiantesService
import services.EstudiantesServiceImpl

val EstudiantesModule = module {
    factory<Cache<Long, Estudiante>> { CacheImpl(getProperty("cache.size")) }

    single<SqlDeLightClient >{
        SqlDeLightClient(
            getProperty("database.url"),
            getProperty("database.inmemory"),
            getProperty("database.init.data")
        )
    }

    single<EstudiantesRepository> { EstudianteRepositoryImpl(get()) }



    factory<EstudiantesService> { EstudiantesServiceImpl(get()) }

}