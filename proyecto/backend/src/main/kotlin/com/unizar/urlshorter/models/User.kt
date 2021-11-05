package com.unizar.urlshorter.models

import com.unizar.urlshorter.models.Url

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.ArrayList

@Document(collection = "users")
class User {
    @Id
    var id = "" 
    var name = ""
    var email = ""
    var password  = ""
    var urls = ArrayList<Url>()

    constructor(name: String, email: String, password: String){
        this.name = name
        this.email = email
        this.password = password
    }

    fun addUrl(url: Url){
        urls.add(url)
    }

    fun comparePassword(password: String): Boolean {
        return this.password == password
    }
}