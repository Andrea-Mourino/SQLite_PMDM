package com.example.sqlite_andrea // Indica el paquete donde vive esta clase

import android.content.ContentValues // Permite crear pares clave‑valor para insertar/actualizar datos
import android.database.sqlite.SQLiteDatabase // Representa la conexión a la base de datos SQLite
import android.os.Bundle // Necesario para el ciclo de vida de actividades
import android.util.Log // Permite escribir mensajes en Logcat
import androidx.appcompat.app.AppCompatActivity // Actividad base compatible con AppCompat

class MainActivity : AppCompatActivity() { // Clase principal de la app, hereda de AppCompatActivity

    private lateinit var gestor: GestorBD // Declaramos el helper que gestiona la BD
    private lateinit var conexion: SQLiteDatabase // Objeto que representa la conexión abierta a la BD

    override fun onCreate(savedInstanceState: Bundle?) { // Método que se ejecuta al iniciar la actividad
        super.onCreate(savedInstanceState)  // Llama al comportamiento base
        setContentView(R.layout.activity_main) // Carga el diseño XML en pantalla

        gestor = GestorBD(this) // Creamos una instancia del gestor de BD
        conexion = gestor.writableDatabase // Abrimos la BD en modo escritura

        Log.d("BDAndrea", "Iniciando operaciones...") // Mensaje informativo en Logcat

        insertar() // Llama a la función que inserta registros
        mostrar() // Llama a la función que consulta e imprime los datos
        modificar() // Actualiza un registro existente
        mostrar() // Vuelve a consultar para ver los cambios
        borrar() // Elimina un registro
        mostrar() // Consulta final para ver el resultado

        conexion.close() // Cerramos la conexión a la BD
        Log.d("BDAndrea", "Proceso finalizado") // Mensaje final en Logcat
    }

    private fun insertar() { // Función que inserta datos en la tabla
        Log.d("BDAndrea", "Insertando alumnos...") // Mensaje informativo

        val alumno1 = ContentValues().apply {  // Creamos un registro para insertar
            put(EsquemaBD.Alumno.COL_NOMBRE, "ANA") // Nombre del alumno
            put(EsquemaBD.Alumno.COL_EDAD, 19) // Edad
            put(EsquemaBD.Alumno.COL_CARRERA, "BIOLOGÍA") // Carrera
        }

        val alumno2 = ContentValues().apply { // Segundo registro
            put(EsquemaBD.Alumno.COL_NOMBRE, "MARIO")
            put(EsquemaBD.Alumno.COL_EDAD, 23)
            put(EsquemaBD.Alumno.COL_CARRERA, "ARQUITECTURA")
        }

        conexion.insert(EsquemaBD.Alumno.TABLA, null, alumno1) // Inserta el primer alumno
        conexion.insert(EsquemaBD.Alumno.TABLA, null, alumno2) // Inserta el segundo alumno
    }

    private fun mostrar() { // Función que consulta y muestra los datos de la tabla
        Log.d("BDAndrea", "Consultando alumnos...")  // Mensaje informativo

        val columnas = arrayOf( // Array con los nombres de columnas que queremos leer
            EsquemaBD.Alumno.COL_ID,
            EsquemaBD.Alumno.COL_NOMBRE,
            EsquemaBD.Alumno.COL_EDAD,
            EsquemaBD.Alumno.COL_CARRERA
        )

        val cursor = conexion.query( // Realiza la consulta SELECT
            EsquemaBD.Alumno.TABLA, // Tabla a consultar
            columnas,  // Columnas a devolver
            null, // WHERE (null = sin filtro)
            null, // Argumentos del WHERE
            null, // GROUP BY
            null, // HAVING
            EsquemaBD.Alumno.COL_ID // ORDER BY (ordenado por ID)
        )

        Log.d("BDAndrea", "Total: ${cursor.count}") // Muestra cuántos registros hay

        while (cursor.moveToNext()) {  // Recorre cada fila del resultado
            val id = cursor.getInt(0) // Lee la columna 0 (ID)
            val nombre = cursor.getString(1) // Lee la columna 1 (nombre)
            val edad = cursor.getInt(2) // Lee la columna 2 (edad)
            val carrera = cursor.getString(3) // Lee la columna 3 (carrera)

            Log.d("BDAndrea", "$id | $nombre | $edad | $carrera") // Imprime los datos
        }

        cursor.close() // Cierra el cursor para liberar memoria
    }

    private fun modificar() { // Función que actualiza un registro
        Log.d("BDAndrea", "Actualizando alumno...") // Mensaje informativo

        val valores = ContentValues().apply { // Valores nuevos para actualizar
            put(EsquemaBD.Alumno.COL_EDAD, 20) // Cambiamos la edad a 20
        }

        conexion.update( // Ejecuta UPDATE
            EsquemaBD.Alumno.TABLA, // Tabla
            valores, // Nuevos valores
            "${EsquemaBD.Alumno.COL_NOMBRE} = ?", // WHERE
            arrayOf("ANA") // Argumento del WHERE
        )
    }

    private fun borrar() { // Función que elimina un registro
        Log.d("BDAndrea", "Eliminando alumno...") // Mensaje informativo

        conexion.delete(   // Ejecuta DELETE
            EsquemaBD.Alumno.TABLA, // Tabla
            "${EsquemaBD.Alumno.COL_NOMBRE} = ?", // WHERE
            arrayOf("MARIO") // Argumento del WHERE
        )
    }
}
