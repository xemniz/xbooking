package com.xmn.dataadapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


interface DataView<State> {
    fun render(state: State)
}

inline fun <reified State : Any, DV> dataViewUniqueDelegate(
    crossinline createView: (Context) -> DV
) where DV : DataView<State>,
        DV : View =
    dataViewDelegate(createView, { this })

inline fun <reified State : Any, DV> dataViewDelegate(
    crossinline createView: (Context) -> DV,
    crossinline identify: State.() -> Any
) where DV : DataView<State>,
        DV : View = object : ViewDataRenderer<State>() {
    override fun clazz() = State::class

    override fun State.identify() = identify(this)

    override fun ViewGroup.view() = createView(context)
        .apply {
            layoutParams = layoutParams?.let {
                it.width = ViewGroup.LayoutParams.MATCH_PARENT
                it
            } ?: RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

    override fun ViewHolder.bind(
        item: State,
        oldItem: State?
    ) {
        containerView.tag = oldItem
        @Suppress("UNCHECKED_CAST")
        (containerView as DV).render(item)
    }
}