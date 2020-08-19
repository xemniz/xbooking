package com.xmn.myapplication.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import by.kirich1409.viewbindingdelegate.viewBinding
import com.xmn.dataadapter.DataAdapter
import com.xmn.dataadapter.dataAdapterItems
import com.xmn.myapplication.R
import com.xmn.myapplication.databinding.MainFragmentBinding
import com.xmn.myapplication.model.ServiceCategory
import com.xmn.myapplication.ui.main.renderers.CategoryRenderer
import com.xmn.myapplication.ui.main.renderers.ServiceRenderer
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    private val binding by viewBinding<MainFragment, MainFragmentBinding>()

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        binding.recyclerServices.layoutManager = LinearLayoutManager(context)
        binding.recyclerServices.adapter = DataAdapter.from(
            listOf(
                ServiceRenderer()
            )
        )

        binding.recyclerCategories.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerCategories.adapter = DataAdapter.from(
            listOf(
                CategoryRenderer()
            )
        )

        binding.btnSearch.setOnClickListener { viewModel.clickSearch() }
        binding.etSearch.doAfterTextChanged { viewModel.search(it?.toString() ?: "") }

        viewModel.stateHolder.observe(viewLifecycleOwner, ::render)
    }

    private fun render(state: ServicesState) {
        TransitionManager.beginDelayedTransition(binding.main, TransitionSet().apply {
            ordering = TransitionSet.ORDERING_SEQUENTIAL
            addTransition(ChangeBounds())
                .addTransition(Fade(Fade.IN))
        })

        if (state.isSearch) {
            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.main)
            constraintSet.connect(
                binding.btnSearch.id,
                ConstraintSet.START,
                binding.main.id,
                ConstraintSet.START
            )
            constraintSet.clear(binding.btnSearch.id, ConstraintSet.END)
            constraintSet.applyTo(binding.main)
            binding.title.isVisible = false
            binding.btnBack.isVisible = false
            binding.btnSearchCard.isVisible = false
            binding.etSearch.isVisible = true
            binding.btnSearch.updateLayoutParams<ConstraintLayout.LayoutParams> { marginStart = 24 }
        } else {
            val constraintSet = ConstraintSet()
            constraintSet.clone(binding.main)
            constraintSet.connect(
                binding.btnSearch.id,
                ConstraintSet.END,
                binding.main.id,
                ConstraintSet.END
            )
            constraintSet.clear(binding.btnSearch.id, ConstraintSet.START)
            constraintSet.applyTo(binding.main)
            binding.title.isVisible = true
            binding.btnBack.isVisible = true
            binding.btnSearchCard.isVisible = true
            binding.etSearch.isVisible = false
        }

        binding.recyclerServices.dataAdapterItems =
            state.servicesResponse.services.map { service ->
                ServiceRenderer.Item(
                    service,
                    state.selectedServices.contains(service)
                ) {
                    viewModel.clickService(
                        service
                    )
                }
            }.filter {
                val filter = if (state.selectedCategory == CATEGORY_ALL)
                    true
                else
                    it.service.categoryId == state.selectedCategory.id

                filter && it.service.title.contains(state.searchQuery)
            }

        binding.recyclerCategories.dataAdapterItems =
            mutableListOf<ServiceCategory>().apply {
                add(CATEGORY_ALL)
                addAll(state.servicesResponse.categories)
            }.map { category ->
                CategoryRenderer.Item(
                    category,
                    state.selectedCategory == (category)
                ) {
                    viewModel.clickCategory(
                        category
                    )
                }
            }
    }
}