package com.unizar.urlshorter.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.ArrayList
import org.bson.types.ObjectId

@Document(collection = "users")
class User {
    @Id
    var id = "" 
    var name = ""
    var email = ""
    var password  = ""
    var urls = ArrayList<ObjectId>()

    constructor(name: String, email: String, password: String){
        this.name = name
        this.email = email
        this.password = password
    }

    fun addUrl(url: ObjectId){
        urls.add(url)
    }

    fun comparePassword(password: String): Boolean {
        return this.password == password
    }
}