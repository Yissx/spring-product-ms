package com.example.msproduct.kafka.consumer

import com.example.msproduct.config.AppConstants
import com.example.msproduct.dto.request.StockProducerDto
import com.example.msproduct.service.StockService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.KafkaListeners
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class UpdateStockConsumer (
        val objectMapper: ObjectMapper,
        val stockService: StockService){
    @KafkaListeners(value = [
        KafkaListener(topics = [AppConstants.TOPIC_NAME])
    ])
    fun updateStock(data : String){
        val productId = StockProducerDto(UUID.fromString(data))
        println(productId)
        stockService.update(productId)
    }
}