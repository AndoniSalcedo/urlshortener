package com.unizar.urlshorter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UrlshorterApplication

fun main(args: Array<String>) {
	runApplication<UrlshorterApplication>(*args)
}
