package com.xmn.myapplication.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xmn.myapplication.model.Service
import com.xmn.myapplication.model.ServiceCategory
import com.xmn.myapplication.model.ServicesResponse

class MainViewModel : ViewModel() {
    val stateHolder: MutableLiveData<ServicesState> = MutableLiveData()

    init {
        stateHolder.value = ServicesState(
            ServicesResponse(
                services = listOf(
                    Service(
                        "service1",
                        "service1",
                        "category1",
                        100.0,
                        "description1",
                        listOf("1", "2", "3")
                    ),
                    Service(
                        "service3",
                        "service3",
                        "category1",
                        100.0,
                        "description1",
                        listOf("1", "2", "3")
                    ),
                    Service(
                        "service2",
                        "service2",
                        "category2",
                        150.0,
                        "description2",
                        listOf("4", "2", "3")
                    ),
                    Service(
                        "service4",
                        "service4",
                        "category2",
                        150.0,
                        "description2",
                        listOf("4", "2", "3")
                    ),
                    Service(
                        "service5",
                        "service5",
                        "category5",
                        150.0,
                        "description2",
                        listOf("4", "2", "3")
                    ),
                    Service(
                        "service6",
                        "service6",
                        "category6",
                        100.0,
                        "description1",
                        listOf("1", "2", "3")
                    ),
                    Service(
                        "service7",
                        "service7",
                        "category7",
                        100.0,
                        "description1",
                        listOf("1", "2", "3")
                    )
                ),
                categories = listOf(
                    ServiceCategory("category1", "category1"),
                    ServiceCategory("category2", "category2"),
                    ServiceCategory("category3", "category3"),
                    ServiceCategory("category4", "category4"),
                    ServiceCategory("category5", "category5"),
                    ServiceCategory("category6", "category6"),
                    ServiceCategory("category7", "category7")
                )
            )
        )
    }

    fun clickService(service: Service) = updateState { state ->
        state.copy(selectedServices =
        if (state.selectedServices.contains(service))
            state.selectedServices.filterNot { it == service }
        else state.selectedServices + service)
    }

    fun clickCategory(category: ServiceCategory) = updateState { state ->
        state.copy(selectedCategory = category)
    }

    fun changeSearchQuery(searchQuery: String) = updateState { state ->
        state.copy(searchQuery = searchQuery)
    }

    fun clickSearch() = updateState { state ->
        state.copy(isSearch = !state.isSearch, searchQuery = "")
    }

    private fun updateState(updateState: (state: ServicesState) -> ServicesState) {
        val state = stateHolder.value ?: return
        stateHolder.value = updateState(state)
    }

    fun search(text: String) {

    }
}

data class ServicesState(
    val servicesResponse: ServicesResponse,
    val selectedServices: List<Service> = emptyList(),
    val selectedCategory: ServiceCategory = CATEGORY_ALL,
    val searchQuery: String = "",
    val isSearch: Boolean = false
)

val CATEGORY_ALL = ServiceCategory(id = "CATEGORY_ALL", title = "Все")