/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper
import java.security.AccessControlContext

// TODO (01) Create an abstract class that extends RoomDatabase.
@Database(entities = [SleepNight::class], version = 1, exportSchema = false)
abstract class SleepDatabase : RoomDatabase() {

    abstract val sleepDatabaseDao: SleepDatabaseDao

    companion object {

        /**
         * Esta variable nos asegura que el valor de INSTANCE siempre a al altura de fecha
         * y para el resto de ejecuciones.
         * EL valor de una variable volatile nunca se almacena en cache, leer y escribir
         * se realizan desde y hacia la memoria principal.
         * Lo que significa que los cambios son visibles para los demas subprocesos
         */
        @Volatile
        private var INSTANCE: SleepDatabase? = null

        fun getInstance(context: Context): SleepDatabase {
            /**
             * envolviendo nuestro codigo en medios sincronizados
             * solo un hilo de ejecucion puede ingresar a la vez a este bloque de codigo
             * Lo   uw asegura que la bbdd solo se inicie una vez
             */
            synchronized(this) {

                var instance = INSTANCE
                if (null == instance) {

                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SleepDatabase::class.java,
                        "sleep_history_database")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}

