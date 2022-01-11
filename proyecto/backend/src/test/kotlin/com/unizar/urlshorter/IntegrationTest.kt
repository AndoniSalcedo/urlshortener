package com.unizar.urlshorter

import com.unizar.urlshorter.controllers.ShortOut
import com.unizar.urlshorter.controllers.ShortIn
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.*
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.net.URI
import org.springframework.http.*
import org.springframework.util.*

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AddressBookServiceTest {

    @LocalServerPort
    var port = 0

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    // Checks that don't s
    @Test
    fun notFound() {
        val response = restTemplate.getForEntity("http://localhost:$port/f684a3c4", String::class.java)
        assertThat(response.statusCode).isEqualTo(HttpStatus.FORBIDDEN)
    }

    
    // Test if shorther return the hash of the shorted url
    // It always return the 201 code
    @Test
    fun createURI() {
        val response = shortUrl("http://pepito_que_sabe_mucho_de_ssh.com/", true)
        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        // Created ok
        assertThat(response.body?.url).isEqualTo("5dc6a05e")
    }

    private fun shortUrl(u: String, q: Boolean): ResponseEntity<ShortOut> {
        val headers = HttpHeaders()
        
        val data = ShortIn(
            url = u,
            qr = q
        ) 

        return restTemplate.postForEntity(
            "http://localhost:$port/api/shorter",
            HttpEntity(data, headers), ShortOut::class.java
        )
    }

}