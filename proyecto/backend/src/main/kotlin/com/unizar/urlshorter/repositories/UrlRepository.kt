package com.unizar.urlshorter.repositories

import com.unizar.urlshorter.models.Url

import org.springframework.data.mongodb.repository.MongoRepository

interface UrlRepository: MongoRepository<Url, String> {
    fun findOneByShorter(shorter: String): Url?
}