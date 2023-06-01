@file:Suppress("UsePropertyAccessSyntax")

package com.price_of_command.relfection

import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType
import java.net.URL
import java.net.URLClassLoader

object ReflectionUtils {
    // Thanks to Lukas for putting this together. I've kept the below acknowledgements from the original

    //Notes for future me because i keep forgeting how this works.

    //Lots of credits to lyravega as most of this is based on his.

    //The first argument in "MethodType.methodType()" is the return type, all afterwards are a Vararg for the method arguments

    //The method invoke always requires the Field itself. This is why despite some MethodHandles only mentioning 2 parameters in MethodType.methodType(),
    //the method actually requires 3 to be invoked.

    fun set(fieldName: String, instanceToModify: Any, newValue: Any?) {
        val fieldClass = Class.forName("java.lang.reflect.Field", false, Class::class.java.classLoader)
        val setMethod = MethodHandles.lookup().findVirtual(fieldClass, "set", MethodType.methodType(Void.TYPE, Any::class.java, Any::class.java))
        val getNameMethod =
            MethodHandles.lookup().findVirtual(fieldClass, "getName", MethodType.methodType(String::class.java))
        val setAccessMethod = MethodHandles.lookup().findVirtual(
            fieldClass,
            "setAccessible",
            MethodType.methodType(Void.TYPE, Boolean::class.javaPrimitiveType)
        )

        val instancesOfFields: Array<out Any> = instanceToModify.javaClass.getDeclaredFields()
        for (obj in instancesOfFields) {
            setAccessMethod.invoke(obj, true)
            val name = getNameMethod.invoke(obj)
            if (name.toString() == fieldName) {
                setMethod.invoke(obj, instanceToModify, newValue)
            }
        }
    }

    fun get(fieldName: String, instanceToGetFrom: Any): Any? {
        val fieldClass = Class.forName("java.lang.reflect.Field", false, Class::class.java.classLoader)
        val getMethod = MethodHandles.lookup().findVirtual(fieldClass, "get", MethodType.methodType(Any::class.java, Any::class.java))
        val getNameMethod =
            MethodHandles.lookup().findVirtual(fieldClass, "getName", MethodType.methodType(String::class.java))
        val setAccessMethod = MethodHandles.lookup().findVirtual(
            fieldClass,
            "setAccessible",
            MethodType.methodType(Void.TYPE, Boolean::class.javaPrimitiveType)
        )

        val instancesOfFields: Array<out Any> = instanceToGetFrom.javaClass.getDeclaredFields()
        for (obj in instancesOfFields) {
            setAccessMethod.invoke(obj, true)
            val name = getNameMethod.invoke(obj)
            if (name.toString() == fieldName) {
                return getMethod.invoke(obj, instanceToGetFrom)
            }
        }
        return null
    }

    fun getInt(fieldName: String, instanceToGetFrom: Any) = get(fieldName, instanceToGetFrom) as Int?
    fun getDouble(fieldName: String, instanceToGetFrom: Any) = get(fieldName, instanceToGetFrom) as Double?
    fun getFloat(fieldName: String, instanceToGetFrom: Any) = get(fieldName, instanceToGetFrom) as Float?
    fun getLong(fieldName: String, instanceToGetFrom: Any) = get(fieldName, instanceToGetFrom) as Long?
    fun getString(fieldName: String, instanceToGetFrom: Any) = get(fieldName, instanceToGetFrom) as String?
    fun getBoolean(fieldName: String, instanceToGetFrom: Any) = get(fieldName, instanceToGetFrom) as Boolean?

    fun createClass(claz: Class<*>) : MethodHandle {
        var loader = this::class.java.classLoader
        val urls: Array<URL> = (loader as URLClassLoader).urLs
        val reflectionLoader: Class<*> = ReflectionClassLoader(urls, ClassLoader.getSystemClassLoader()).loadClass(claz.name)
        var handle = MethodHandles.lookup().findConstructor(reflectionLoader, MethodType.methodType(Void.TYPE))
        return handle
    }

    fun invoke(methodName: String, instance: Any, vararg arguments: Any?) : Any? {
        val methodClass = Class.forName("java.lang.reflect.Method", false, Class::class.java.classLoader)
        val getNameMethod = MethodHandles.lookup().findVirtual(methodClass, "getName", MethodType.methodType(String::class.java))
        val invokeMethod = MethodHandles.lookup().findVirtual(methodClass, "invoke", MethodType.methodType(Any::class.java, Any::class.java, Array<Any>::class.java))

        var foundMethod: Any? = null
        for (method in instance::class.java.methods as Array<Any>) {
            if (getNameMethod.invoke(method) == methodName) {
                foundMethod = method
            }
        }

        return invokeMethod.invoke(foundMethod, instance, arguments)
    }

    /*fun Any.reflectionGet(fieldName: String) = get(fieldName, this)
    fun Any.reflectionSet(fieldName: String, newValue: Any?) = set(fieldName, this, newValue)*/

    fun listMethods(instance: Any): Array<Any> {
        val methodClass = Class.forName("java.lang.reflect.Method", false, Class::class.java.classLoader)
        val getNameMethod =
            MethodHandles.lookup().findVirtual(methodClass, "getName", MethodType.methodType(String::class.java))

        return instance::class.java.methods as Array<Any>
    }
}