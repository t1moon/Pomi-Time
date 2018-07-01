package apps.tim.pomos.base

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import apps.tim.pomos.base.ui.DEFAULT_INT
import apps.tim.pomos.base.ui.PREF_DAILY_KEY
import apps.tim.pomos.base.ui.PREF_REST_KEY
import apps.tim.pomos.base.ui.PREF_WORK_KEY
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


object PreferenceHelper {


    fun defaultPrefs(context: Context): SharedPreferences
            = PreferenceManager.getDefaultSharedPreferences(context)

    fun customPrefs(context: Context, name: String): SharedPreferences
            = context.getSharedPreferences(name, Context.MODE_PRIVATE)

    inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    /**
     * puts a key value pair in shared prefs if doesn't exists, otherwise updates value on given [key]
     */
    operator fun SharedPreferences.set(key: String, value: Any?) {
        when (value) {
            is String? -> edit({ it.putString(key, value) })
            is Int -> edit({ it.putInt(key, value) })
            is Boolean -> edit({ it.putBoolean(key, value) })
            is Float -> edit({ it.putFloat(key, value) })
            is Long -> edit({ it.putLong(key, value) })
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    /**
     * finds value on given key.
     * [T] is the type of value
     * @param defaultValue optional default value - will take null for strings, false for bool and -1 for numeric values if [defaultValue] is not specified
     */
    inline operator fun <reified T : Any> SharedPreferences.get(key: String, defaultValue: T): T {
        return when (T::class) {
            String::class -> getString(key, defaultValue as? String) as T
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T
            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    fun getDaily(context: Context) : Int {
        return defaultPrefs(context)[PREF_DAILY_KEY, DEFAULT_INT]
    }

    fun getWorkDuration(context: Context) : Int {
        return defaultPrefs(context)[PREF_WORK_KEY, DEFAULT_INT]
    }

    fun getRestDuration(context: Context) : Int {
        return defaultPrefs(context)[PREF_REST_KEY, DEFAULT_INT]
    }

    fun SharedPreferences.int(def: Int = 0, key: String? = null) =
            delegate(def, key, SharedPreferences::getInt, SharedPreferences.Editor::putInt)

    fun SharedPreferences.long(def: Long = 0, key: String? = null) =
            delegate(def, key, SharedPreferences::getLong, SharedPreferences.Editor::putLong)


    fun SharedPreferences.boolean(def: Boolean = false, key: String? = null) =
            delegate(def, key, SharedPreferences::getBoolean, SharedPreferences.Editor::putBoolean)



    private inline fun <T> SharedPreferences.delegate(
            defaultValue: T,
            key: String?,
            crossinline getter: SharedPreferences.(String, T) -> T,
            crossinline setter: SharedPreferences.Editor.(String, T) -> SharedPreferences.Editor
    ): ReadWriteProperty<Any, T> {
        return object : ReadWriteProperty<Any, T> {
            override fun getValue(thisRef: Any, property: KProperty<*>) =
                    getter(key ?: property.name, defaultValue)

            override fun setValue(thisRef: Any, property: KProperty<*>,
                                  value: T) =
                    edit().setter(key ?: property.name, value).apply()
        }
    }
}
