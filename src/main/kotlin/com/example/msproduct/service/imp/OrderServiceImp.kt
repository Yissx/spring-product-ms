package com.example.msproduct.service.imp

import com.example.msproduct.dto.response.OrderDto
import com.example.msproduct.dto.request.OrderDtoRequestCreate
import com.example.msproduct.errors.EntityNotFoundException
import com.example.msproduct.errors.ProductNotAvailable
import com.example.msproduct.mapper.OrderMapper
import com.example.msproduct.repository.ClientRepository
import com.example.msproduct.repository.OrderRepository
import com.example.msproduct.repository.ProductRepository
import com.example.msproduct.service.OrderService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class OrderServiceImp(
    private val orderRepository: OrderRepository,
    private val orderMapper : OrderMapper,
    private val productRepository: ProductRepository,
    private val clientRepository: ClientRepository
) : OrderService{

    override fun create(orderDto: OrderDtoRequestCreate): OrderDto {
        val client = clientRepository.findById(orderDto.clientId!!).orElseThrow {
            EntityNotFoundException("Non-existent client entity with id ${orderDto.clientId}")
        }
        val orderEntity = orderMapper.toEntity(orderDto.orderDate!!)
        orderEntity.client = client
        val response = orderRepository.save(orderEntity)
        return orderMapper.toDto(response)
    }

    override fun findAll(): List<OrderDto> {
        val response = orderRepository.findAll()
        return response.map {
            orderMapper.toDto(it)
        }
    }

    override fun findById(id: UUID): OrderDto {
        val response = orderRepository.findById(id).orElseThrow{
            EntityNotFoundException("Non-existent entity order with id $id")
        }
        return orderMapper.toDto(response)
    }

    override fun addProduct(products: List<UUID>, id: UUID): OrderDto {
        val order = orderRepository.findById(id).orElseThrow{
            EntityNotFoundException("Non-existent entity order with id $id")
        }
        products.forEach{productId ->
            val product = productRepository.findById(productId).orElseThrow {
                EntityNotFoundException("Non-existent product entity with id $id")
            }
            if(product.stock!!.stock > 0){
                order.products?.add(product)
                product.stock!!.stock -= 1
                productRepository.save(product)
            }
            else{
                throw ProductNotAvailable("Product with id $productId is not available")
            }
        }
        val response = orderRepository.save(order)
        return orderMapper.toDto(response)
    }
    override fun deleteProduct(products: List<UUID>, id: UUID): OrderDto {
        val order = orderRepository.findById(id).orElseThrow{
            EntityNotFoundException("Non-existent entity order with id $id")
        }
        products.forEach{
            val product = productRepository.findById(it).orElseThrow {
                EntityNotFoundException("Non-existent entity product with id $id")
            }
            order.products?.remove(product)
            product.stock!!.stock += 1
        }
        val response = orderRepository.save(order)
        return orderMapper.toDto(response)
    }

    override fun delete(id: UUID) {
        orderRepository.deleteById(id)
    }
}