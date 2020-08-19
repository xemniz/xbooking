package com.xmn.myapplication.ui.main.renderers

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.xmn.dataadapter.ViewDataRenderer
import com.xmn.dataadapter.inflate
import com.xmn.myapplication.R
import com.xmn.myapplication.databinding.ItemCategoryBinding
import com.xmn.myapplication.databinding.ItemServiceBinding
import com.xmn.myapplication.model.Service
import com.xmn.myapplication.model.ServiceCategory

class ServiceRenderer : ViewDataRenderer<ServiceRenderer.Item>() {

    override fun clazz() = Item::class

    override fun ViewGroup.view() = inflate(R.layout.item_service)

    override fun ViewHolder.bind(item: Item, oldItem: Item?) {
        val viewBinding = getViewBinding { ItemServiceBinding.bind(containerView) }
        viewBinding.tvServiceTitle.text = item.service.title
        viewBinding.price.text = """${item.service.price} Р"""
        viewBinding.btnAddService.setOnClickListener { item.click() }
        viewBinding.btnAddService.setCardBackgroundColor(
            ContextCompat.getColor(
                containerView.context,
                if (item.selected) R.color.blue else android.R.color.white
            )
        )
        viewBinding.btnIcon.setImageResource(if (item.selected) R.drawable.ic_trash else R.drawable.ic_plus)
    }

    override fun Item.identify() = service.id

    data class Item(val service: Service, val selected: Boolean = false, val click: () -> Unit)
}

class CategoryRenderer : ViewDataRenderer<CategoryRenderer.Item>() {

    override fun clazz() = Item::class

    override fun ViewGroup.view() = inflate(R.layout.item_category)

    override fun ViewHolder.bind(item: Item, oldItem: Item?) {
        val viewBinding = getViewBinding { ItemCategoryBinding.bind(containerView) }
        viewBinding.tvTitle.text = item.category.title
        viewBinding.tvTitle.setTextColor(
            ContextCompat.getColor(
                containerView.context,
                if (item.selected) android.R.color.white else android.R.color.black
            )
        )
        viewBinding.cardView.setOnClickListener { item.click() }
        viewBinding.cardView.setCardBackgroundColor(
            ContextCompat.getColor(
                containerView.context,
                if (item.selected) android.R.color.black else android.R.color.white
            )
        )
    }

    override fun Item.identify() = category.id

    data class Item(
        val category: ServiceCategory,
        val selected: Boolean = false,
        val click: () -> Unit
    )
}