package com.xmn.dataadapter

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
abstract class ViewDataRenderer<T : Any> {

    abstract fun clazz(): KClass<T>
    abstract fun ViewGroup.view(): View
    abstract fun ViewHolder.bind(
        item: T,
        oldItem: T?
    )

    abstract fun T.identify(): Any

    class ViewHolder(val containerView: View) :
        RecyclerView.ViewHolder(containerView) {
        private val viewsCache: MutableMap<Int, View> = mutableMapOf()
        private var viewBinding: Any? = null
        fun <T : View> view(@IdRes id: Int): T {
            return this.viewsCache[id] as T? ?: containerView.findViewById<T>(id)
                .also { this.viewsCache[id] = it }
        }

        fun <T> getViewBinding(init: () -> T): T {
            return viewBinding as T ?: init().also { viewBinding = it }
        }
    }
}