package com.example.msproduct

import com.example.msproduct.TestData.ProductTestData
import com.example.msproduct.errors.EntityNotFoundException
import com.example.msproduct.mapper.ProductMapper
import com.example.msproduct.repository.ProductRepository
import com.example.msproduct.service.imp.ProductServiceImp
import com.example.msproduct.service.imp.StockServiceImp
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness
import org.junit.jupiter.api.Test
import java.util.Optional

@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MsProductTest : ProductTestData(){

    @InjectMocks
    private lateinit var productServiceImp : ProductServiceImp

    @Mock
    private lateinit var productRepository : ProductRepository
    @Mock
    private lateinit var productMapper: ProductMapper
    @Mock
    private lateinit var stockServiceImp: StockServiceImp

    @Test
    fun `find all`(){
        val entities = msProductList()
        val dtos = msProductDtoList()

        Mockito.`when`(productRepository.findAll()).thenReturn(entities)
        Mockito.`when`(productMapper.toDto(entities)).thenReturn(dtos)

        val response = productServiceImp.findAll()

        Mockito.verify(productRepository).findAll()
        Assertions.assertThat(entities.size).isEqualTo(response.size)
    }

    @Test
    fun `find by id`(){
        val entity = msProduct()
        val dto = msProductDto()

        Mockito.`when`(productRepository.findById(dto.id!!)).thenReturn(Optional.of(entity))
        Mockito.`when`(productMapper.toDto(entity)).thenReturn(dto)

        val response = productServiceImp.findById(dto.id!!)

        Mockito.verify(productRepository).findById(dto.id!!)
        Assertions.assertThat(entity.id).isEqualTo(response.id)
    }
    @Test
    fun `find by id not found`(){
        val entity = msProduct()

        Mockito.`when`(productRepository.findById(entity.id)).thenReturn(Optional.empty())
        Assertions.assertThatExceptionOfType(EntityNotFoundException::class.java).isThrownBy {
            productServiceImp.findById(entity.id)
        }
    }

    @Test
    fun create(){
        val dto = msProductDto()
        val entity = msProduct()

        Mockito.`when`(productMapper.toEntity(dto)).thenReturn(entity)
        Mockito.`when`(productRepository.save(entity)).thenReturn(entity)
        Mockito.`when`(productMapper.toDto(entity)).thenReturn(dto)
        Mockito.doNothing().`when`(stockServiceImp).create(product = entity)

        val response = productServiceImp.create(dto)

        Mockito.verify(productMapper).toEntity(dto)
        Mockito.verify(productRepository).save(entity)
        Mockito.verify(productMapper).toDto(entity)
        Assertions.assertThat(response.id).isEqualTo(dto.id)
    }

    @Test
    fun update(){
        val entity = msProduct()
        val dto = msProductUpdateDto()

        Mockito.`when`(productRepository.findById(dto.id!!)).thenReturn(Optional.of(entity))
        Mockito.`when`(productMapper.updateEntity(dto, entity)).thenAnswer { entity.description to dto.description }
        Mockito.`when`(productRepository.save(entity)).thenReturn(entity)
        Mockito.`when`(productMapper.toDto(entity)).thenReturn(dto)

        val response = productServiceImp.update(dto, dto.id!!)

        Mockito.verify(productRepository).findById(dto.id!!)
        Mockito.verify(productMapper).updateEntity(dto, entity)
        Mockito.verify(productRepository).save(entity)
        Mockito.verify(productMapper).toDto(entity)
        Assertions.assertThat(entity.id).isEqualTo(response.id)
        Assertions.assertThat(response.description).isNotEqualTo(entity.description)
    }
    @Test
    fun `update not found`(){
        val dto = msProductDto()

        Mockito.`when`(productRepository.findById(dto.id!!)).thenReturn(Optional.empty())
        Assertions.assertThatExceptionOfType(EntityNotFoundException::class.java).isThrownBy {
            productServiceImp.update(dto, dto.id!!)
        }
    }

    @Test
    fun delete(){
        val dto = msProductDto()

        Mockito.`when`(productRepository.findById(dto.id!!)).thenReturn(Optional.empty())
        Assertions.assertThatExceptionOfType(EntityNotFoundException::class.java).isThrownBy {
            productServiceImp.update(dto, dto.id!!)
        }
    }

    @Test
    fun `delete with id not found`(){
        val entity = msProduct()

        Mockito.`when`(productRepository.findById(entity.id)).thenReturn(Optional.of(entity))
        Mockito.doNothing().`when`(productRepository).deleteById(entity.id)

        productServiceImp.delete(entity.id)

        Mockito.verify(productRepository).deleteById(entity.id)
    }

    private fun <T> any(): T {
        return org.mockito.ArgumentMatchers.any()
    }
}