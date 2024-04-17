package database

import kotlin.Long
import kotlin.String

public data class EstudianteEntity(
  public val id: Long,
  public val nombre: String,
  public val edad: Long,
  public val is_deleted: Long,
)
