package com.example.msproduct.data

import com.example.msproduct.dto.response.ProductDto
import com.example.msproduct.entity.ProductEntity
import java.util.UUID

open class ProductTestData {
    fun msProduct() : ProductEntity {
        return ProductEntity().apply{
            id = UUID.fromString("77666785-83be-453f-bdfb-7c45edbec5f7")
            name = "name"
            price = 35.76
            description = "description"
        }
    }

    fun msProductList() : List<ProductEntity> {
        return listOf(
            ProductEntity().apply{
                id = UUID.fromString("77666785-83be-453f-bdfb-7c45edbec5f7")
                name = "name"
                price = 35.76
                description = "description"
            })
    }
    fun msProductDtoList() : List<ProductDto> {
        return listOf(
            ProductDto().apply{
                id = UUID.fromString("77666785-83be-453f-bdfb-7c45edbec5f7")
                name = "name"
                price = 35.76
                description = "description"
            })
    }
    fun msProductDto() : ProductDto {
        return ProductDto().apply{
            id = UUID.fromString("77666785-83be-453f-bdfb-7c45edbec5f7")
            name = "name"
            price = 35.76
            description = "description"
        }
    }
    fun msProductDtoUpdated() : ProductDto {
        return ProductDto().apply{
            id = UUID.fromString("77666785-83be-453f-bdfb-7c45edbec5f7")
            name = "no name"
            price = 89.0
            description = "no description"
        }
    }
}