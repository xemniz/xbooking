package com.xmn.dataadapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AdapterDelegatesManager
import kotlin.reflect.KClass

class DataAdapter(private val delegates: List<DataDelegate<*>>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val delegatesManager: AdapterDelegatesManager<List<Any>>
    val differ: AsyncListDiffer<Any>

    var items: List<Any>
        get() = differ.currentList
        set(items) = differ.submitList(items)

    init {
        val delegatesManager = DataDelegatesManager().apply {
            delegates.forEach { addDelegate(it) }
        }
        val diffCallback = object : DiffUtil.ItemCallback<Any>() {
            override fun areItemsTheSame(
                oldItem: Any,
                newItem: Any
            ): Boolean {
                val delegate = delegatesManager.delegateFor(newItem)
                return delegate?.areItemsTheSame(oldItem, newItem) ?: false
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: Any,
                newItem: Any
            ): Boolean {
                return oldItem == newItem
            }

            override fun getChangePayload(
                oldItem: Any,
                newItem: Any
            ): Any? {
                return oldItem
            }
        }
        this.differ = AsyncListDiffer(this, diffCallback)
        this.delegatesManager = delegatesManager
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return delegatesManager.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegatesManager.onBindViewHolder(differ.currentList, position, holder, null)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: List<*>
    ) {
        delegatesManager.onBindViewHolder(differ.currentList, position, holder, payloads)
    }

    override fun getItemViewType(position: Int): Int {

        return try {
            delegatesManager.getItemViewType(differ.currentList, position)
        } catch (e: Exception) {
            val item = items[position]
            throw IllegalArgumentException("has no delegate for ${item.javaClass.name}")
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        delegatesManager.onViewRecycled(holder)
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return delegatesManager.onFailedToRecycleView(holder)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        delegatesManager.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        delegatesManager.onViewDetachedFromWindow(holder)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    companion object {
        fun from(renderers: List<ViewDataRenderer<*>>) =
            DataAdapter(renderers.map { DataDelegate(it) })
    }
}

class DataDelegatesManager : AdapterDelegatesManager<List<Any>>() {
    private val delegatesMap: MutableMap<KClass<*>, DataDelegate<*>> = mutableMapOf()

    fun delegateFor(oldItem: Any): DataDelegate<*>? {
        return delegatesMap[oldItem::class]
    }

    override fun addDelegate(delegate: AdapterDelegate<List<Any>>): AdapterDelegatesManager<List<Any>> {
        delegatesMap[(delegate as DataDelegate<*>).clazz()] = delegate
        return super.addDelegate(delegate)
    }
}

fun RecyclerView.dataAdapter(
    lManager: RecyclerView.LayoutManager = LinearLayoutManager(context),
    initDelegates: DataDelegatesBuilder.() -> Unit
) {
    val delegatesBuilder = DataDelegatesBuilder()
    delegatesBuilder.initDelegates()
    dataAdapter(delegatesBuilder.build(), lManager)
}

fun RecyclerView.dataAdapterLinear(
    vararg delegates: DataDelegate<*>
) {
    dataAdapter(delegates.toList(), LinearLayoutManager(context))
}

fun RecyclerView.dataAdapter(
    delegates: List<DataDelegate<*>>,
    lManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
) {
    layoutManager = lManager
    adapter = DataAdapter(delegates)
}

var RecyclerView.dataAdapterItems
    get() = (adapter as DataAdapter).items
    set(items) {
        (adapter as DataAdapter).items = items
    }

class DataDelegatesBuilder {
    private val list = mutableListOf<DataDelegate<*>>()
    fun add(delegate: DataDelegate<*>) {
        list.add(delegate)
    }

    operator fun DataDelegate<*>.unaryPlus() {
        if (this is DataAdapterOwner) {
            delegatesFactory = { list }
        }
        list.add(this)
    }

    operator fun List<DataDelegate<*>>.unaryPlus() {
        list.forEach {
            if (it is DataAdapterOwner) {
                it.delegatesFactory = { list }
            }
        }
        list.addAll(this)
    }

    fun build(): List<DataDelegate<*>> {
        return list.toList()
    }
}

fun dataDelegatesBuilder(
    builder: DataDelegatesBuilder.() -> Unit
): List<DataDelegate<*>> {
    return DataDelegatesBuilder().apply(builder).build()
}

inline fun <reified U> Any?.cast(): U? = this as? U

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attach: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attach)
}