package com.example.msproduct.service.imp

import com.example.msproduct.dto.request.StockDtoRequest
import com.example.msproduct.dto.request.StockDtoRequestCreate
import com.example.msproduct.dto.response.StockDto
import com.example.msproduct.entity.ProductEntity
import com.example.msproduct.errors.EntityNotFoundException
import com.example.msproduct.mapper.StockMapper
import com.example.msproduct.repository.StockRepository
import com.example.msproduct.service.StockService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class StockServiceImp(
    private val stockMapper: StockMapper,
    private val stockRepository : StockRepository
) : StockService{

    override fun create(product : ProductEntity) {
        val entity = stockMapper.toEntity(StockDtoRequestCreate(product))
        stockRepository.save(entity)
    }

    override fun update(stockDto: StockDtoRequest, id: UUID): StockDto {
        val stock = stockRepository.findById(id).orElseThrow {
            EntityNotFoundException("Non-existent stock with id $id")
        }
        stock.stock = stockDto.stock
        val response = stockRepository.save(stock)
        return stockMapper.toDto(response)
    }

    override fun findAll(): List<StockDto> {
        val response = stockRepository.findAll()
        return stockMapper.toDto(response)
    }

}