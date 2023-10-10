package com.example.msproduct

import com.example.msproduct.data.OrderTestData
import com.example.msproduct.entity.ClientEntity
import com.example.msproduct.errors.EntityNotFoundException
import com.example.msproduct.errors.ProductNotAvailable
import com.example.msproduct.mapper.OrderMapper
import com.example.msproduct.repository.ClientRepository
import com.example.msproduct.repository.OrderRepository
import com.example.msproduct.repository.ProductRepository
import com.example.msproduct.service.imp.OrderServiceImp
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness
import java.util.Optional
import java.util.UUID

@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MsOrderTest : OrderTestData(){

    @InjectMocks
    private lateinit var orderServiceImp: OrderServiceImp

    @Mock
    private lateinit var orderMapper: OrderMapper
    @Mock
    private lateinit var orderRepository: OrderRepository
    @Mock
    private lateinit var productRepository: ProductRepository
    @Mock
    private lateinit var clientRepository: ClientRepository

    @Test
    fun create(){
        val entity = msOrder()
        val dtoRequest = msOrderDtoCreateRequest()
        val dtoResponse = msOrderDtoResponse()

        Mockito.`when`(clientRepository.findById(dtoRequest.clientId!!)).thenReturn(Optional.ofNullable(ClientEntity()))
        Mockito.`when`(orderMapper.toEntity(dtoRequest.orderDate!!)).thenReturn(entity)
        Mockito.`when`(orderRepository.save(entity)).thenReturn(entity)
        Mockito.`when`(orderMapper.toDto(entity)).thenReturn(dtoResponse)

        val response = orderServiceImp.create(dtoRequest)

        Mockito.verify(clientRepository).findById(dtoRequest.clientId!!)
        Mockito.verify(orderMapper).toEntity(dtoRequest.orderDate!!)
        Mockito.verify(orderRepository).save(entity)
        Mockito.verify(orderMapper).toDto(entity)

        Assertions.assertThat(dtoRequest.clientId).isEqualTo(response.clientId)
    }

    @Test
    fun `create order with non-existent client`(){
        val dtoRequest = msOrderDtoCreateRequest()

        Mockito.`when`(clientRepository.findById(dtoRequest.clientId!!)).thenReturn(Optional.empty())

        Assertions.assertThatExceptionOfType(EntityNotFoundException::class.java).isThrownBy { orderServiceImp.create(dtoRequest) }
    }

    @Test
    fun `find all`(){
        val entities = msOrderList()
        val dtos = msOrderDtoList()

        Mockito.`when`(orderRepository.findAll()).thenReturn(entities)
        Mockito.`when`(orderMapper.toDto(entities)).thenReturn(dtos)

        val response = orderServiceImp.findAll()

        Mockito.verify(orderRepository).findAll()
        Mockito.verify(orderMapper).toDto(entities)
        Assertions.assertThat(entities.size).isEqualTo(response.size)
    }

    @Test
    fun `find by id`(){
        val entity = msOrder()
        val dto = msOrderDtoResponse()

        Mockito.`when`(orderRepository.findById(dto.id!!)).thenReturn(Optional.of(entity))
        Mockito.`when`(orderMapper.toDto(entity)).thenReturn(dto)

        val response = orderServiceImp.findById(dto.id!!)

        Mockito.verify(orderRepository).findById(dto.id!!)
        Mockito.verify(orderMapper).toDto(entity)
        Assertions.assertThat(dto.id).isEqualTo(response.id)
    }

    @Test
    fun `find by id not found`(){
        val id = UUID.randomUUID()

        Mockito.`when`(orderRepository.findById(id)).thenReturn(Optional.empty())

        Assertions.assertThatExceptionOfType(EntityNotFoundException::class.java).isThrownBy { orderServiceImp.findById(id) }
    }

    @Test
    fun `add product`(){
        val entity = msOrder()
        val dto = msOrderDtoResponse()
        val product = msOrderProductStock()
        val ids = listOf<UUID>(product.id)

        Mockito.`when`(orderRepository.findById(dto.id!!)).thenReturn(Optional.of(entity))
        Mockito.`when`(productRepository.findById(ids[0])).thenReturn(Optional.of(product))
        Mockito.`when`(productRepository.save(product)).thenReturn(product)
        Mockito.`when`(orderRepository.save(entity)).thenReturn(entity.apply { products = mutableListOf(product) })
        Mockito.`when`(orderMapper.toDto(entity)).thenReturn(dto.apply { productsId = ids })

        val response = orderServiceImp.addProduct(ids, dto.id!!)


        Mockito.verify(orderRepository).findById(dto.id!!)
        Mockito.verify(productRepository).findById(ids[0])
        Mockito.verify(orderRepository).save(entity)
        Assertions.assertThat(response.productsId!!).containsAll(ids)
    }

    @Test
    fun `add product - order not found`(){
        val id = UUID.randomUUID()
        val productIds = mutableListOf(UUID.randomUUID())

        Mockito.`when`(orderRepository.findById(id)).thenReturn(Optional.empty())

        Assertions.assertThatExceptionOfType(EntityNotFoundException::class.java).isThrownBy { orderServiceImp.addProduct(productIds, id) }
    }

    @Test
    fun `add product - product not found`(){
        val entity = msOrder()
        val productIds = mutableListOf(UUID.randomUUID())

        Mockito.`when`(orderRepository.findById(entity.id!!)).thenReturn(Optional.of(entity))
        Mockito.`when`(productRepository.findById(productIds[0])).thenReturn(Optional.empty())

        Assertions.assertThatExceptionOfType(EntityNotFoundException::class.java).isThrownBy { orderServiceImp.addProduct(productIds, entity.id) }
    }

    @Test
    fun `add product - no product availability`(){
        val product = msOrderProductNoStock()
        val order = msOrder()
        val productIds = mutableListOf(product.id)

        Mockito.`when`(orderRepository.findById(order.id!!)).thenReturn(Optional.of(order))
        Mockito.`when`(productRepository.findById(productIds[0])).thenReturn(Optional.of(product))

        Assertions.assertThatExceptionOfType(ProductNotAvailable::class.java).isThrownBy { orderServiceImp.addProduct(productIds, order.id) }
    }

    @Test
    fun `delete product`(){
        val orderWithProducts = msOrderProduct()
        val order = msOrder()
        val dto = msOrderDtoResponse()
        val product = msOrderProductStock()
        val ids = listOf<UUID>(product.id)


        Mockito.`when`(orderRepository.findById(dto.id!!)).thenReturn(Optional.of(orderWithProducts))
        Mockito.`when`(productRepository.findById(ids[0])).thenReturn(Optional.of(product))
        Mockito.`when`(productRepository.save(product)).thenReturn(product)
        Mockito.`when`(orderRepository.save(orderWithProducts)).thenReturn(order)
        Mockito.`when`(orderMapper.toDto(order)).thenReturn(dto)

        val response = orderServiceImp.deleteProduct(ids, dto.id!!)

        Mockito.verify(orderRepository).findById(dto.id!!)
        Mockito.verify(productRepository).findById(ids[0])
        Mockito.verify(orderRepository).save(orderWithProducts)
        Assertions.assertThat(response.productsId)?.isNull()
    }

    @Test
    fun `delete product - order not found`(){
        val id = UUID.randomUUID()

        Mockito.`when`(orderRepository.findById(id)).thenReturn(Optional.empty())

        Assertions.assertThatExceptionOfType(EntityNotFoundException::class.java).isThrownBy { orderServiceImp.findById(id) }
    }

    @Test
    fun `delete product - product not found`(){
        val id = UUID.randomUUID()

        Mockito.`when`(orderRepository.findById(id)).thenReturn(Optional.empty())
        Mockito.`when`(productRepository.findById(id)).thenReturn(Optional.empty())

        Assertions.assertThatExceptionOfType(EntityNotFoundException::class.java).isThrownBy { orderServiceImp.findById(id) }
    }
}