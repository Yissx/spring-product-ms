package com.example.msproduct.controller

import com.example.msproduct.dto.request.StockDtoRequest
import com.example.msproduct.dto.response.StockDto
import com.example.msproduct.service.StockService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/stock")
class StockController (val stockService : StockService){

    @PutMapping("/{id}/add")
    fun set(@RequestBody stockDto: StockDtoRequest, @PathVariable("id") id : UUID) : StockDto
        = stockService.set(stockDto, id)

    @RequestMapping
    fun findAll() : List<StockDto>
        = stockService.findAll()
}