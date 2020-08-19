package com.xmn.myapplication.model

import com.squareup.moshi.JsonClass


data class ServicesResponse(
    val services: List<Service>,
    val categories: List<ServiceCategory>
)


data class Service(
    val id: String, //	Идентификатор услуги
    val title: String, //	Наименование услуги
    val categoryId: String,//	Идентификатор категории, в которой состоит услуга
    val price: Double, //	Цена на услугу
    val description: String, //	 Комментарий у услуге
    val masterIdList: List<String> //	Список ID сотрудников, оказывающих услугу
)


data class ServiceCategory(
    val id: String, //	Идентификатор категории
    val title: String //  Наименование категории
)


data class Master(
    val id: String, //	Идентификатор сотрудника
    val name: String, //	Имя сотрудника
    val rating: Int, //	Рейтинг сотрудника
    val votesCount: Int, //	Кол-во голос, оценивших сотрудника
    val commentsCount: Int, //	Кол-во комментариев к сотрунику
    val avatar: String, //	Путь к файлу аватарки сотрудника
    val avatarBig: String, //	Путь к файлу аватарки сотрудника в более высоком разрешении
    val information: String, //	Дополнительная информация о сотруднике
    val portfolioImages: List<String> //  Фотографии примеров работ
)


data class Comment(
    val id: Int, //	Id комментария
    val masterId: Int, //	ID мастера, если type = 1
    val text: String, //	Текст комментария
    val date: String, //	Дата, когда был оставлен комментарий
    val rating: Int, //	Оценка (от 1 до 5)
    val userId: Int, //	Id пользователя, оставившего комментарий
    val userName: String //	Имя пользователя, оставившего комментарий
)


data class BookingDay(
    val date: String, //  Дата оказания услуги
    val seances: List<Seance> //  Сеансы, доступные для бронирования
)


data class Seance(
    val startTime: String,  //  Начало сеанса
    val durationInMinutes: Int, //  Время сеанса в минутах
    val masterIdList: List<String> //	Список ID сотрудников, доступных для сеанса
)