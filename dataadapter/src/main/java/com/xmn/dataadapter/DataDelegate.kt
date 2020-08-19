package com.xmn.dataadapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import java.lang.ClassCastException
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
class DataDelegate<T : Any>(private val renderer: ViewDataRenderer<T>) :
    AdapterDelegate<List<Any>>() {
    public override fun onCreateViewHolder(parent: ViewGroup): ViewDataRenderer.ViewHolder {
        return ViewDataRenderer.ViewHolder(renderer.run { parent.view() })
    }

    override fun isForViewType(items: List<Any>, position: Int): Boolean {
        val item = items[position]
        return renderer.clazz().isInstance(item)
    }

    public override fun onBindViewHolder(
        items: List<Any>,
        position: Int,
        holder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ) {
        val viewHolder = holder as ViewDataRenderer.ViewHolder
        val item = items[position]
        renderer.apply { viewHolder.bind(item as T, payloads.firstOrNull() as? T) }
    }

    fun areItemsTheSame(
        oldItem: Any,
        newItem: Any
    ): Boolean =
        try {
            (oldItem as T).identify() ==
                    (newItem as T).identify()
        } catch (e: ClassCastException) {
            false
        }

    private fun T.identify(): Any = renderer.run { identify() }

    fun clazz(): KClass<T> {
        return renderer.clazz()
    }
}

abstract class UniqueDelegate<T : Any> : ViewDataRenderer<T>() {
    override fun T.identify(): Any = this
}

abstract class StaticDelegate<T : Any> : UniqueDelegate<T>() {
    override fun ViewHolder.bind(
        item: T,
        oldItem: T?
    ) {
    }
}

interface DataAdapterOwner {
    var delegatesFactory: () -> List<DataDelegate<*>>
}

class DataAdapterOwnerImpl : DataAdapterOwner {
    override var delegatesFactory: () -> List<DataDelegate<*>> = { emptyList() }
}