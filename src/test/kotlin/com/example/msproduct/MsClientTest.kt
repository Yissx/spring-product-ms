package com.example.msproduct

import com.example.msproduct.data.ClientTestData
import com.example.msproduct.errors.EntityNotFoundException
import com.example.msproduct.mapper.ClientMapper
import com.example.msproduct.repository.ClientRepository
import com.example.msproduct.service.imp.ClientServiceImp
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
class MsClientTest : ClientTestData(){

    @InjectMocks
    private lateinit var clientServiceImp: ClientServiceImp

    @Mock
    private lateinit var clientRepository: ClientRepository
    @Mock
    private lateinit var clientMapper: ClientMapper

    @Test
    fun create(){
        val dto = msClientDto()
        val entity = msClient()

        Mockito.`when`(clientMapper.toEntity(dto)).thenReturn(entity)
        Mockito.`when`(clientRepository.save(entity)).thenReturn(entity)
        Mockito.`when`(clientMapper.toDto(entity)).thenReturn(dto)

        val response = clientServiceImp.create(dto)

        Mockito.verify(clientMapper).toEntity(dto)
        Mockito.verify(clientRepository).save(entity)
        Mockito.verify(clientMapper).toDto(entity)
        Assertions.assertThat(response.name).isEqualTo(dto.name)
    }

    @Test
    fun `find all`(){
        val entities = msClientList()
        val dtos = msClientDtoList()

        Mockito.`when`(clientRepository.findAll()).thenReturn(entities)
        Mockito.`when`(clientMapper.toDto(entities)).thenReturn(dtos)

        val response = clientServiceImp.findAll()

        Mockito.verify(clientRepository).findAll()
        Mockito.verify(clientMapper).toDto(entities)
        Assertions.assertThat(response.size).isEqualTo(entities.size)
    }

    @Test
    fun `find by id`(){
        val entity = msClient()
        val dto = msClientDto()

        Mockito.`when`(clientRepository.findById(dto.id!!)).thenReturn(Optional.of(entity))
        Mockito.`when`(clientMapper.toDto(entity)).thenReturn(dto)

        val response = clientServiceImp.findById(dto.id!!)

        Mockito.verify(clientRepository).findById(dto.id!!)
        Assertions.assertThat(entity.id).isEqualTo(response.id)
    }

    @Test
    fun `find by id not found`(){
        val id = UUID.randomUUID()

        Mockito.`when`(clientRepository.findById(id)).thenReturn(Optional.empty())
        Assertions.assertThatExceptionOfType(EntityNotFoundException::class.java).isThrownBy {
            clientServiceImp.findById(id)
        }
    }

    @Test
    fun `update`(){
        val entity = msClient()
        val dto = msClientDto()

        Mockito.`when`(clientRepository.findById(dto.id!!)).thenReturn(Optional.of(entity))
        Mockito.`when`(clientMapper.updateEntity(dto, entity)).thenAnswer { entity.name to dto.name }
        Mockito.`when`(clientRepository.save(entity)).thenReturn(entity)
        Mockito.`when`(clientMapper.toDto(entity)).thenReturn(dto)

        val response = clientServiceImp.update(dto, dto.id!!)

        Mockito.verify(clientRepository).findById(dto.id!!)
        Mockito.verify(clientMapper).updateEntity(dto, entity)
        Mockito.verify(clientRepository).save(entity)
        Mockito.verify(clientMapper).toDto(entity)
        Assertions.assertThat(entity.id).isEqualTo(response.id)
        Assertions.assertThat(response.name).isNotEqualTo(entity.name)
    }

    @Test
    fun `update - id not found`(){
        val dto = msClientDto()

        Mockito.`when`(clientRepository.findById(dto.id!!)).thenReturn(Optional.empty())
        Assertions.assertThatExceptionOfType(EntityNotFoundException::class.java).isThrownBy {
            clientServiceImp.update(dto, dto.id!!)
        }
    }

    @Test
    fun `delete`(){
        val id = UUID.randomUUID()

        Mockito.doNothing().`when`(clientRepository).deleteById(id)

        clientServiceImp.delete(id)

        Mockito.verify(clientRepository).deleteById(id)
    }
}