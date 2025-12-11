# Proyecto: SQLite en Android

## 1. Objetivo del proyecto

El objetivo de este proyecto es:

* Crear una base de datos SQLite dentro de una app Android
* Crear una tabla de alumnos con campos: `id`, `nombre`, `edad`, `carrera`
* Realizar un CRUD completo:

  * Insertar registros
  * Consultar registros
  * Actualizar un registro
  * Eliminar un registro
* Ver todo el funcionamiento a través de mensajes en Logcat, sin interfaz gráfica compleja

En resumen: es un ejemplo práctico de cómo usar SQLite en Android

---

## 2. Estructura general del proyecto

Las partes principales del proyecto son:

* **EsquemaBD.kt**: Define cómo es la tabla (nombres de columnas, tabla, etc.)
* **GestorBD.kt**: Crea y gestiona la base de datos (`SQLiteOpenHelper`)
* **MainActivity.kt**: Abre la base de datos y realiza las operaciones (insertar, consultar, actualizar, borrar)
* **themes.xml + AndroidManifest.xml**: Configuran el tema y la actividad principal
* **activity_main.xml**: Layout básico de la pantalla (aunque todo se ve por Logcat)

---

## 3. EsquemaBD.kt – Definir la estructura de la base de datos

Este archivo se encarga de **definir la estructura lógica de la tabla**. No crea nada por sí solo, solo guarda nombres de tabla y columnas para evitar errores y mejorar el mantenimiento

```kotlin
package com.example.sqlite_andrea

// Objeto que define la estructura de la base de datos
object EsquemaBD {

    // Objeto interno que representa la tabla "alumnos"
    object Alumno {

        const val TABLA = "alumnos" // Nombre de la tabla en SQLite
        const val COL_ID = "id" // Columna ID (clave primaria)
        const val COL_NOMBRE = "nombre" // Columna para el nombre del alumno
        const val COL_EDAD = "edad" // Columna para la edad
        const val COL_CARRERA = "carrera" // Columna para la carrera universitaria
    }
}
```

### Para qué sirve

* Evita **escribir a mano los nombres** de las columnas en múltiples lugares del código
* Previene **errores de escritura**
* Facilita **mantenimiento y cambios** futuros: si cambias un nombre de columna, solo lo modificas aquí
* Mantiene **coherencia** en toda la app cuando se trabaja con la base de datos

### Notas adicionales

* `object` en Kotlin se usa para declarar un **singleton**, un objeto único que no necesita instanciarse
* `EsquemaBD.Alumno` funciona como un contenedor de **constantes**, representando la tabla y sus columnas
* Este patrón se llama **contrato de base de datos** y es muy usado en Android para trabajar con SQLite

---

## 4. GestorBD.kt – Crear y gestionar la base de datos

```kotlin
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
```

**Para qué sirve cada parte:**

* **Constructor:** define nombre del archivo (`Instituto.db`) y versión (1)
* **onCreate():** se ejecuta la primera vez que se abre la base de datos; crea la tabla `alumnos`
* **onUpgrade():** se ejecuta cuando cambia la versión; borra la tabla existente y la vuelve a crear

---

## 5. MainActivity.kt – Ejecutar las operaciones sobre la base de datos

```kotlin
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
```

**Resumen del flujo:**

1. Se abre la app: `onCreate()`
2. Se abre la base de datos (`GestorBD`)
3. `insertar()`:Inserta ANA y MARIO
4. `mostrar()`: Muestra los 2 alumnos
5. `modificar()`: Cambia la edad de ANA a 20
6. `mostrar()`: Muestra los cambios
7. `borrar()`: Borra a MARIO
8. `mostrar()`: Muestra solo a ANA
9. Se cierra la base de datos

Todo se ve en Logcat con el tag **BDAndrea**

---

## 6. Tema y configuración de la actividad

### 6.1. values/themes.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources xmlns:tools="http://schemas.android.com/tools">
    <style name="Theme.SQLite_Andrea" parent="Theme.AppCompat.Light.DarkActionBar">
        <item name="colorPrimary">@color/purple_500</item>
        <item name="colorPrimaryDark">@color/purple_700</item>
        <item name="colorAccent">@color/teal_200</item>
        <item name="android:statusBarColor">@color/purple_700</item>
    </style>
</resources>
```

### 6.2. AndroidManifest.xml

```xml
<application
    android:theme="@style/Theme.SQLite_Andrea"
    ... >

    <activity android:name=".MainActivity">
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
    </activity>

</application>
```

---

## 7. Cómo probar el proyecto

1. Ejecuta la app en el emulador o dispositivo
2. Abre **Logcat**
3. Filtra por el tag: `com.example.sqlite_andrea`
4. Verás algo como:

```
Tabla creada correctamente
Insertando alumnos…
Total: 2
1 | ANA | 19 | BIOLOGÍA
2 | MARIO | 23 | ARQUITECTURA
Actualizando alumno…
Eliminando alumno…
Total: 1
1 | ANA | 20 | BIOLOGÍA
```
