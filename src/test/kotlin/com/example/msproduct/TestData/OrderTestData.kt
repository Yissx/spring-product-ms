package com.example.msproduct.TestData

import com.example.msproduct.dto.enums.OrderStatusEnum
import com.example.msproduct.dto.request.OrderDtoRequestCreate
import com.example.msproduct.dto.response.OrderDto
import com.example.msproduct.entity.OrderEntity
import com.example.msproduct.entity.ProductEntity
import com.example.msproduct.entity.StockEntity
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import java.time.LocalDateTime
import java.util.UUID

open class OrderTestData {
    fun msOrder() : OrderEntity {
        return OrderEntity().apply {
            id = UUID.fromString("77666785-83be-453f-bdfb-7c45edbec5f7")
            orderDate = LocalDateTime.of(200, 1, 1, 8, 15)
            status = OrderStatusEnum.PENDING
        }
    }
    fun msOrderDtoResponse() : OrderDto{
        return OrderDto().apply {
            id = UUID.fromString("77666785-83be-453f-bdfb-7c45edbec5f7")
            orderDate = LocalDateTime.of(200, 1, 1, 8, 15)
            status = OrderStatusEnum.PENDING
            clientId = UUID.fromString("418b7b5a-91df-498f-8fa4-fc789fe58be3")
        }
    }
    fun msOrderDtoCreateRequest() : OrderDtoRequestCreate{
        return OrderDtoRequestCreate().apply {
            orderDate = LocalDateTime.of(200, 1, 1, 8, 15)
            clientId = UUID.fromString("418b7b5a-91df-498f-8fa4-fc789fe58be3")
        }
    }

    fun msOrderProductStock() : ProductEntity{
        return ProductEntity().apply {
            id = UUID.fromString("bef40e93-1e76-4bce-a003-7266f7f720a5")
            stock = StockEntity().apply {
                stock = 20
                id = UUID.fromString("3fee9e18-19d8-44c8-af6c-635a6b9eedd8")
            }
        }
    }

    fun msOrderList() : List<OrderEntity> {
        return listOf(
            OrderEntity().apply {
                id = UUID.fromString("77666785-83be-453f-bdfb-7c45edbec5f7")
                orderDate = LocalDateTime.of(200, 1, 1, 8, 15)
                status = OrderStatusEnum.PENDING
            })
    }

    fun msOrderDtoList() : List<OrderDto> {
        return listOf(
            OrderDto().apply {
                id = UUID.fromString("77666785-83be-453f-bdfb-7c45edbec5f7")
                orderDate = LocalDateTime.of(200, 1, 1, 8, 15)
                status = OrderStatusEnum.PENDING
            })
    }
}