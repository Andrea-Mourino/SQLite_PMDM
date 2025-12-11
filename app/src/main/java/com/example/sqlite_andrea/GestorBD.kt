package com.example.sqlite_andrea

import android.content.Context // Necesario para acceder al contexto de la app
import android.database.sqlite.SQLiteDatabase // Representa la BD en sí
import android.database.sqlite.SQLiteOpenHelper // Clase base para gestionar SQLite
import android.util.Log // Para escribir mensajes en Logcat

// Clase que administra la creación y actualización de la base de datos
class GestorBD(context: Context) : SQLiteOpenHelper(
    context, // Contexto de la aplicación
    "Instituto.db", // Nombre del archivo de la base de datos
    null, // CursorFactory (normalmente null)
    1 // Versión de la base de datos
) {

    // Método que se ejecuta SOLO la primera vez que se crea la BD
    override fun onCreate(db: SQLiteDatabase) {

        // Sentencia SQL para crear la tabla "alumnos"
        val sql = """
            CREATE TABLE ${EsquemaBD.Alumno.TABLA} (
                ${EsquemaBD.Alumno.COL_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${EsquemaBD.Alumno.COL_NOMBRE} TEXT NOT NULL,
                ${EsquemaBD.Alumno.COL_EDAD} INTEGER NOT NULL,
                ${EsquemaBD.Alumno.COL_CARRERA} TEXT
            )
        """.trimIndent()

        db.execSQL(sql) // Ejecuta la sentencia SQL en la base de datos

        Log.d("BDAndrea", "Tabla creada correctamente") // Mensaje en Logcat
    }

    // Método que se ejecuta cuando se cambia la versión de la BD
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        // Borra la tabla si existe (para reconstruirla)
        db.execSQL("DROP TABLE IF EXISTS ${EsquemaBD.Alumno.TABLA}")

        // Vuelve a crear la tabla llamando a onCreate()
        onCreate(db)
    }
}
