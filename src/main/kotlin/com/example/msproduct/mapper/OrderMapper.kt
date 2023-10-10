package com.example.msproduct.mapper

import com.example.msproduct.entity.OrderEntity
import org.mapstruct.Mapper
import org.mapstruct.NullValueCheckStrategy
import org.mapstruct.NullValuePropertyMappingStrategy
import org.mapstruct.ReportingPolicy
import java.time.LocalDateTime
import com.example.msproduct.dto.response.OrderDto
import org.mapstruct.Mapping


@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
interface OrderMapper {
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(target = "productsId", ignore = true)
    fun toDto(orderEntity: OrderEntity) : OrderDto

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(target = "productsId", ignore = true)
    fun toDto(orderEntities: List<OrderEntity>) : List<OrderDto>

    fun toEntity(orderDate : LocalDateTime) : OrderEntity

}