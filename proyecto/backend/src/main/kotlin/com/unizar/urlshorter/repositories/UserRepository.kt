package com.unizar.urlshorter.repositories

import com.unizar.urlshorter.models.User
import org.bson.types.ObjectId

import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository: MongoRepository<User, String> {
    fun findOneByEmail(email: String): User?
    fun findOneById(id: ObjectId): User?
}