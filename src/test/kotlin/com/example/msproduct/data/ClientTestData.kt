package com.example.msproduct.data

import com.example.msproduct.dto.response.ClientDto
import com.example.msproduct.entity.ClientEntity
import java.util.UUID

open class ClientTestData {

    fun msClient() : ClientEntity{
        return ClientEntity().apply {
            id = UUID.fromString("77666785-83be-453f-bdfb-7c45edbec5f7")
            name = "name"
            lastname = "lastname"
            cellphone = "cellphone"
            address = "no adress"
        }
    }

    fun msClientDto() : ClientDto {
        return ClientDto().apply {
            id = UUID.fromString("77666785-83be-453f-bdfb-7c45edbec5f7")
            name = "unnamed"
            lastname = "lastname"
            cellphone = "cellphone"
            address = "adress"
        }
    }

    fun msClientList() : List<ClientEntity>{
        return listOf(
            ClientEntity().apply {
                id = UUID.fromString("77666785-83be-453f-bdfb-7c45edbec5f7")
                name = "name"
                lastname = "lastname"
                cellphone = "cellphone"
                address = "no adress"
            })
    }

    fun msClientDtoList() : List<ClientDto> {
        return listOf(
            ClientDto().apply {
                id = UUID.fromString("77666785-83be-453f-bdfb-7c45edbec5f7")
                name = "name"
                lastname = "lastname"
                cellphone = "cellphone"
                address = "no adress"
            })
    }
}