package com.example.sqlite_andrea

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class GestorBD(context: Context) : SQLiteOpenHelper(
    context,
    "Instituto.db",
    null,
    1
) {

    override fun onCreate(db: SQLiteDatabase) {
        val sql = """
            CREATE TABLE ${EsquemaBD.Alumno.TABLA} (
                ${EsquemaBD.Alumno.COL_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${EsquemaBD.Alumno.COL_NOMBRE} TEXT NOT NULL,
                ${EsquemaBD.Alumno.COL_EDAD} INTEGER NOT NULL,
                ${EsquemaBD.Alumno.COL_CARRERA} TEXT
            )
        """.trimIndent()

        db.execSQL(sql)
        Log.d("BDAndrea", "Tabla creada correctamente")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${EsquemaBD.Alumno.TABLA}")
        onCreate(db)
    }
}
