package com.example.sqlite_andrea

// Objeto que define la estructura (esquema) de la base de datos
object EsquemaBD {

    // Objeto interno que representa la tabla "alumnos"
    object Alumno {

        const val TABLA = "alumnos" // Nombre de la tabla en SQLite
        const val COL_ID = "id" // Nombre de la columna ID (clave primaria)
        const val COL_NOMBRE = "nombre" // Columna para el nombre del alumno
        const val COL_EDAD = "edad" // Columna para la edad
        const val COL_CARRERA = "carrera" // Columna para la carrera universitaria
    }
}
