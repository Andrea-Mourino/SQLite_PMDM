package com.example.sqlite_andrea

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var gestor: GestorBD
    private lateinit var conexion: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gestor = GestorBD(this)
        conexion = gestor.writableDatabase

        Log.d("BDAndrea", "Iniciando operaciones...")

        insertar()
        mostrar()
        modificar()
        mostrar()
        borrar()
        mostrar()

        conexion.close()
        Log.d("BDAndrea", "Proceso finalizado")
    }

    private fun insertar() {
        Log.d("BDAndrea", "Insertando alumnos...")

        val alumno1 = ContentValues().apply {
            put(EsquemaBD.Alumno.COL_NOMBRE, "ANA")
            put(EsquemaBD.Alumno.COL_EDAD, 19)
            put(EsquemaBD.Alumno.COL_CARRERA, "BIOLOGÍA")
        }

        val alumno2 = ContentValues().apply {
            put(EsquemaBD.Alumno.COL_NOMBRE, "MARIO")
            put(EsquemaBD.Alumno.COL_EDAD, 23)
            put(EsquemaBD.Alumno.COL_CARRERA, "ARQUITECTURA")
        }

        conexion.insert(EsquemaBD.Alumno.TABLA, null, alumno1)
        conexion.insert(EsquemaBD.Alumno.TABLA, null, alumno2)
    }

    private fun mostrar() {
        Log.d("BDAndrea", "Consultando alumnos...")

        val columnas = arrayOf(
            EsquemaBD.Alumno.COL_ID,
            EsquemaBD.Alumno.COL_NOMBRE,
            EsquemaBD.Alumno.COL_EDAD,
            EsquemaBD.Alumno.COL_CARRERA
        )

        val cursor = conexion.query(
            EsquemaBD.Alumno.TABLA,
            columnas,
            null,
            null,
            null,
            null,
            EsquemaBD.Alumno.COL_ID
        )

        Log.d("BDAndrea", "Total: ${cursor.count}")

        while (cursor.moveToNext()) {
            val id = cursor.getInt(0)
            val nombre = cursor.getString(1)
            val edad = cursor.getInt(2)
            val carrera = cursor.getString(3)

            Log.d("BDAndrea", "$id | $nombre | $edad | $carrera")
        }

        cursor.close()
    }

    private fun modificar() {
        Log.d("BDAndrea", "Actualizando alumno...")

        val valores = ContentValues().apply {
            put(EsquemaBD.Alumno.COL_EDAD, 20)
        }

        conexion.update(
            EsquemaBD.Alumno.TABLA,
            valores,
            "${EsquemaBD.Alumno.COL_NOMBRE} = ?",
            arrayOf("ANA")
        )
    }

    private fun borrar() {
        Log.d("BDAndrea", "Eliminando alumno...")

        conexion.delete(
            EsquemaBD.Alumno.TABLA,
            "${EsquemaBD.Alumno.COL_NOMBRE} = ?",
            arrayOf("MARIO")
        )
    }
}
