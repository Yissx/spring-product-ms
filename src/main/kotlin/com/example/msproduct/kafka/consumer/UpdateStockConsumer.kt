package com.example.msproduct.kafka.consumer

import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.KafkaListeners
import org.springframework.stereotype.Component

@Component
class UpdateStockConsumer {
    @KafkaListeners(value = [
        KafkaListener(topics = ["product.ms.stock.update"])
    ])
    fun updateStock(data : String){
        println(data)
    }
}