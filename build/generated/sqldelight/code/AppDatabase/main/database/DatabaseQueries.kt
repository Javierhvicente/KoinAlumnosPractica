package database

import app.cash.sqldelight.Query
import app.cash.sqldelight.TransacterImpl
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import kotlin.Any
import kotlin.Long
import kotlin.String

public class DatabaseQueries(
  driver: SqlDriver,
) : TransacterImpl(driver) {
  public fun <T : Any> selectAllStudents(mapper: (
    id: Long,
    nombre: String,
    edad: Long,
    is_deleted: Long,
  ) -> T): Query<T> = Query(-1_595_077_035, arrayOf("EstudianteEntity"), driver, "Database.sq",
      "selectAllStudents",
      "SELECT EstudianteEntity.id, EstudianteEntity.nombre, EstudianteEntity.edad, EstudianteEntity.is_deleted FROM EstudianteEntity") {
      cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getLong(2)!!,
      cursor.getLong(3)!!
    )
  }

  public fun selectAllStudents(): Query<EstudianteEntity> = selectAllStudents { id, nombre, edad,
      is_deleted ->
    EstudianteEntity(
      id,
      nombre,
      edad,
      is_deleted
    )
  }

  public fun <T : Any> selectEstudiantesById(id: Long, mapper: (
    id: Long,
    nombre: String,
    edad: Long,
    is_deleted: Long,
  ) -> T): Query<T> = SelectEstudiantesByIdQuery(id) { cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getLong(2)!!,
      cursor.getLong(3)!!
    )
  }

  public fun selectEstudiantesById(id: Long): Query<EstudianteEntity> = selectEstudiantesById(id) {
      id_, nombre, edad, is_deleted ->
    EstudianteEntity(
      id_,
      nombre,
      edad,
      is_deleted
    )
  }

  public fun <T : Any> selectLastInserted(mapper: (
    id: Long,
    nombre: String,
    edad: Long,
    is_deleted: Long,
  ) -> T): Query<T> = Query(1_792_272_722, arrayOf("EstudianteEntity"), driver, "Database.sq",
      "selectLastInserted",
      "SELECT EstudianteEntity.id, EstudianteEntity.nombre, EstudianteEntity.edad, EstudianteEntity.is_deleted FROM EstudianteEntity WHERE id = (SELECT MAX(id) FROM EstudianteEntity)") {
      cursor ->
    mapper(
      cursor.getLong(0)!!,
      cursor.getString(1)!!,
      cursor.getLong(2)!!,
      cursor.getLong(3)!!
    )
  }

  public fun selectLastInserted(): Query<EstudianteEntity> = selectLastInserted { id, nombre, edad,
      is_deleted ->
    EstudianteEntity(
      id,
      nombre,
      edad,
      is_deleted
    )
  }

  public fun removeAllStudents() {
    driver.execute(-1_122_417_203, """DELETE FROM EstudianteEntity""", 0)
    notifyQueries(-1_122_417_203) { emit ->
      emit("EstudianteEntity")
    }
  }

  public fun inserIntoEstudiantesEntity(nombre: String, edad: Long) {
    driver.execute(-972_202_063, """INSERT INTO EstudianteEntity(nombre, edad) VALUES (?, ?)""", 2)
        {
          bindString(0, nombre)
          bindLong(1, edad)
        }
    notifyQueries(-972_202_063) { emit ->
      emit("EstudianteEntity")
    }
  }

  public fun updateEstudiante(
    nombre: String,
    edad: Long,
    is_deleted: Long,
    id: Long,
  ) {
    driver.execute(-1_932_572_189,
        """UPDATE EstudianteEntity SET nombre = ?, edad = ?, is_deleted = ? WHERE id = ?""", 4) {
          bindString(0, nombre)
          bindLong(1, edad)
          bindLong(2, is_deleted)
          bindLong(3, id)
        }
    notifyQueries(-1_932_572_189) { emit ->
      emit("EstudianteEntity")
    }
  }

  public fun deleteEstudiante(id: Long) {
    driver.execute(2_125_227_077, """DELETE FROM EstudianteEntity WHERE id  = ?""", 1) {
          bindLong(0, id)
        }
    notifyQueries(2_125_227_077) { emit ->
      emit("EstudianteEntity")
    }
  }

  public fun crearTabla() {
    driver.execute(-879_365_141, """
        |CREATE TABLE IF NOT EXISTS EstudianteEntity (
        |    id INTEGER PRIMARY KEY,
        |    nombre TEXT NOT NULL,
        |    edad INTEGER NOT NULL,
        |    is_deleted INTEGER NOT NULL DEFAULT 0
        |)
        """.trimMargin(), 0)
  }

  private inner class SelectEstudiantesByIdQuery<out T : Any>(
    public val id: Long,
    mapper: (SqlCursor) -> T,
  ) : Query<T>(mapper) {
    override fun addListener(listener: Query.Listener) {
      driver.addListener("EstudianteEntity", listener = listener)
    }

    override fun removeListener(listener: Query.Listener) {
      driver.removeListener("EstudianteEntity", listener = listener)
    }

    override fun <R> execute(mapper: (SqlCursor) -> QueryResult<R>): QueryResult<R> =
        driver.executeQuery(-2_032_172_465,
        """SELECT EstudianteEntity.id, EstudianteEntity.nombre, EstudianteEntity.edad, EstudianteEntity.is_deleted FROM EstudianteEntity WHERE id = ?""",
        mapper, 1) {
      bindLong(0, id)
    }

    override fun toString(): String = "Database.sq:selectEstudiantesById"
  }
}
