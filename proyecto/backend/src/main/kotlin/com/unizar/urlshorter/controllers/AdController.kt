package com.unizar.urlshorter.controllers

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import org.springframework.scheduling.concurrent.*
import org.springframework.scheduling.annotation.*
import org.springframework.context.annotation.*
import java.util.concurrent.*

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.*

data class AdsData (
    var ad1: String,
    var ad2: String
)

@RestController
@RequestMapping("/ad")
class AdController {
    val POSSIBLE_ADS = listOf(
        "https://i.imgur.com/HzlTQsZ.gif",
        "https://i.imgur.com/QWFTcyq.png",
        "https://i.imgur.com/A83DbtO.gif",
        "https://i.imgur.com/FwHSQkw.png"
    )

    //Devuelve dos imagenes de anuncios
    //Como no pensamos comunicarnos con servicios de anuncios de verdad, devuelve dos imagenes alreatorias de una lista predefinida
    //@Async("taskExecutor") //Comprobar que se puede usar directamente esta etiqueta sin problemas
    @PostMapping("/obtain")
    fun obtainAds() : ResponseEntity<AdsData> {
        var random1 = POSSIBLE_ADS.random()
        var random2 = POSSIBLE_ADS.random()
        var res = AdsData(
            ad1 = random1,
            ad2 = random2
        )

        return ResponseEntity<AdsData>(res,HttpHeaders(), HttpStatus.OK)
    }
}