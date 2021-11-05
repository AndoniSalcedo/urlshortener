package com.unizar.urlshorter.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import com.google.common.hash.Hashing
import java.nio.charset.StandardCharsets

@Document(collection = "urls")
class Url {
    @Id
    var id = "" 
    var url = ""
    var shorter = ""
    var isValid = false
    var clicks  = 0

    constructor(url: String){
        this.url = url
        this.shorter = Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8).toString()
    }

    fun validateUrl(){
        this.isValid = true
    }

    fun addClick(){
        clicks += 1
    }
}