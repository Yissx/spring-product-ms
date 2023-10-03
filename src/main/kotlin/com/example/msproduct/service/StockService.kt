package com.example.msproduct.service

import com.example.msproduct.dto.response.StockDto
import com.example.msproduct.dto.request.StockDtoRequest
import com.example.msproduct.dto.request.StockProducerDto
import com.example.msproduct.entity.ProductEntity
import java.util.UUID

interface StockService {

    fun create(product : ProductEntity)

    fun set(stockDto : StockDtoRequest, id : UUID) : StockDto

    fun update(stockProducerDto: StockProducerDto)

    fun findAll() : List<StockDto>
}