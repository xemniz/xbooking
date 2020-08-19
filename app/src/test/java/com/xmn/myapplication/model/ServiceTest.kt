package com.xmn.myapplication.model

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.xmn.myapplication.readJsonFile
import org.junit.Test

import org.junit.Assert.*

class ServiceTest {
    private val moshi: Moshi = Moshi.Builder().build()

    @Test
    fun servisesResponse() {
        val readJsonFile = readJsonFile("services.json")
        val servicesResponse = moshi.adapter(ServicesResponse::class.java).fromJson(readJsonFile)
        val expected = ServicesResponse(
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
                    "service2",
                    "service2",
                    "category2",
                    150.0,
                    "description2",
                    listOf("4", "2", "3")
                )
            ),
            categories = listOf(
                ServiceCategory("category1", "category1"),
                ServiceCategory("category2", "category2")
            )
        )
        assertEquals(expected, servicesResponse)
    }

    @Test
    fun mastersResponse() {
        val readJsonFile = readJsonFile("masters.json")
        val type = Types.newParameterizedType(List::class.java, Master::class.java)
        val mastersResponse = moshi.adapter<List<Master>>(type).fromJson(readJsonFile)
        val expected = listOf(
            Master(
                "master1",
                "master1",
                1,
                1,
                1,
                "avatar1",
                "avatarBig1",
                "information1",
                listOf("1", "2", "3")
            ),
            Master(
                "master2",
                "master2",
                2,
                2,
                2,
                "avatar2",
                "avatarBig2",
                "information2",
                listOf("4", "5", "6")
            )
        )
        assertEquals(expected, mastersResponse)
    }

    @Test
    fun bookingDaysResponse() {
        val readJsonFile = readJsonFile("bookingdays.json")
        val type = Types.newParameterizedType(List::class.java, BookingDay::class.java)
        val mastersResponse = moshi.adapter<List<Master>>(type).fromJson(readJsonFile)
        val expected = listOf(
            BookingDay(
                "date1", listOf(
                    Seance(
                        "14:30",
                        60,
                        listOf("1", "2", "3")
                    ),
                    Seance(
                        "15:30",
                        60,
                        listOf("4", "2", "3")
                    )
                )
            ),
            BookingDay(
                "date2", listOf(
                    Seance(
                        "14:30",
                        60,
                        listOf("1", "2", "3")
                    ),
                    Seance(
                        "15:30",
                        60,
                        listOf("4", "2", "3")
                    )
                )
            )

        )
        assertEquals(expected, mastersResponse)
    }
}