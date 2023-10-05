package com.example.msproduct.service.imp

import com.example.msproduct.dto.response.ProductDto
import com.example.msproduct.errors.EntityNotFoundException
import com.example.msproduct.mapper.ProductMapper
import com.example.msproduct.repository.ProductRepository
import com.example.msproduct.service.ProductService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ProductServiceImp (
    private val productRepository: ProductRepository,
    private val productMapper: ProductMapper,
    private val stockServiceImp: StockServiceImp
) : ProductService {

    override fun  create(productDto: ProductDto) : ProductDto {
        val productEntity = productMapper.toEntity(productDto)
        val response = productRepository.save(productEntity)
        stockServiceImp.create(response)
        return productMapper.toDto(response)
    }

    override fun findAll() : List<ProductDto>{
        val entities = productRepository.findAll()
        return productMapper.toDto(entities)
    }

    override fun findById(id : UUID) : ProductDto {
        val response = productRepository.findById(id).orElseThrow{
            EntityNotFoundException("Non-existent product entity with id $id")
        }
        return productMapper.toDto(response)
    }

    override fun search(search : String) : List<ProductDto> {
        val entities = productRepository.findByNameOrDescriptionContaining(search)
        return productMapper.toDto(entities)
    }

    override fun update(productDto: ProductDto, id : UUID) : ProductDto {
        val entity = productRepository.findById(id).orElseThrow{
            EntityNotFoundException("Non-existent product entity with id $id")
        }
        productMapper.updateEntity(productDto, entity)
        val response = productRepository.save(entity)
        return productMapper.toDto(response)
    }

    override fun delete(id : UUID){
        val product = productRepository.findById(id).orElseThrow { EntityNotFoundException("Non-existent product entity with id $id") }
        product.orders?.forEach { order ->
            order.products!!.remove(product)
        }
        productRepository.deleteById(id)
    }
}