/**
 * Source: https://gist.github.com/Zhuinden/ea3189198938bd16c03db628e084a4fa#file-fragmentviewbindingdelegate-kt
 */

// https://github.com/Zhuinden/fragmentviewbindingdelegate-kt
package com.pluto.utilities

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private open class AutoClearedValue<T : Any>(protected val fragment: Fragment) :
    ReadWriteProperty<Fragment, T> {
    protected var value: T? = null

    init {
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
            val viewLifecycleOwnerLiveDataObserver = Observer<LifecycleOwner?> {
                it?.lifecycle?.addObserver(object : DefaultLifecycleObserver {
                    override fun onDestroy(owner: LifecycleOwner) {
                        value = null
                    }
                })
            }

            override fun onCreate(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.observeForever(
                    viewLifecycleOwnerLiveDataObserver
                )
            }

            override fun onDestroy(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.removeObserver(
                    viewLifecycleOwnerLiveDataObserver
                )
            }
        })
    }


    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return value
            ?: throw IllegalStateException("should never call auto-cleared-value get when it might not be available")
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        this.value = value
    }
}

private abstract class BaseAutoClearedInitializedValue<T : Any>(fragment: Fragment) :
    AutoClearedValue<T>(fragment) {
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        val lifecycle = fragment.viewLifecycleOwner.lifecycle
        check(lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
            "Should not attempt to get bindings when Fragment views are destroyed."
        }
        return value ?: initialize().also { this.value = it }
    }

    protected abstract fun initialize(): T
}

private class FragmentViewBindingDelegate<T : ViewBinding>(
    fragment: Fragment,
    private val viewBindingInitializerFactory: (View) -> T
) : BaseAutoClearedInitializedValue<T>(fragment) {
    override fun initialize(): T {
        return viewBindingInitializerFactory(fragment.requireView())
    }
}

private class AutoClearedInitializedValue<T : Any>(
    fragment: Fragment,
    private val initializerFactory: () -> T
) : BaseAutoClearedInitializedValue<T>(fragment) {
    override fun initialize(): T {
        return initializerFactory.invoke()
    }
}

fun <T : Any> Fragment.autoClearInitializer(initializerFactory: () -> T) =
    AutoClearedInitializedValue(this, initializerFactory) as ReadOnlyProperty<Fragment, T>

fun <T : ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> T) =
    FragmentViewBindingDelegate(this, viewBindingFactory) as ReadOnlyProperty<Fragment, T>
