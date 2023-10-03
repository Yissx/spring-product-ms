package com.example.msproduct.service

import com.example.msproduct.dto.response.OrderDto
import com.example.msproduct.dto.request.OrderDtoRequestCreate
import java.util.UUID

interface OrderService {
    fun create(orderDto: OrderDtoRequestCreate) : OrderDto

    fun findAll() : List<OrderDto>

    fun findById(id : UUID) : OrderDto

    fun addProduct(products: List<UUID>, id : UUID) : OrderDto

    fun deleteProduct(products: List<UUID>, id : UUID) : OrderDto

    fun delete(id : UUID)
}