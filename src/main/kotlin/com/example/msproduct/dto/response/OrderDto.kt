package com.example.msproduct.dto.response

import com.example.msproduct.dto.enums.OrderStatusEnum
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime
import java.util.UUID

data class OrderDto (
    var id : UUID? = null,

    @NotNull
    var orderDate : LocalDateTime? = null,

    var clientId : UUID? = null,

    @NotNull
    var productsId : List<UUID>? = null,

    var status : OrderStatusEnum? = null
)