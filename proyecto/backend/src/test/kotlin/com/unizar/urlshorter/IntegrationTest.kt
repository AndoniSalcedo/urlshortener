package com.unizar.urlshorter

import com.unizar.urlshorter.controllers.ShortOut
import com.unizar.urlshorter.controllers.ShortIn
import com.unizar.urlshorter.controllers.QrIn
import com.unizar.urlshorter.controllers.QrOut
import com.unizar.urlshorter.controllers.RegisterIn
import com.unizar.urlshorter.controllers.LoginIn
import com.unizar.urlshorter.controllers.LoginOut
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
class IntegrationTest {

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

    ////////////////////////////////////////////////////
    // R2 : La URI no se acortará si no es alcanzable //
    ////////////////////////////////////////////////////

    // Test to create a url shorted with qr
    // The sorther return the hash of the shorted url
    // It always return the 201 code
    @Test
    fun createURI_rechable() {
        // Create the url short
        val response = shortUrl("https://www.netflix.com/", false)
        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        // Created ok
        assertThat(response.body?.url).isEqualTo("897a52b1")
        
        // Get the QR
        val response_qr = getQR("https://www.netflix.com/")
        assertThat(response_qr.statusCode).isEqualTo(HttpStatus.CREATED)
        // Empty response means that the url has been shorted correctly
        assertThat(response_qr.body?.qr).isEqualTo("")
    }

    
    // Test to create a url shorted with qr
    // The sorther return the hash of the shorted url
    // It always return the 201 code
    @Test
    fun createURI_not_recheable() {
        val response = shortUrl("http://pepito_que_sabe_mucho_de_ssh.com/", true)
        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        // Created ok
        assertThat(response.body?.url).isEqualTo("5dc6a05e")
        // Get the QR
        val response_qr = getQR("http://pepito_que_sabe_mucho_de_ssh.com/")
        assertThat(response_qr.statusCode).isEqualTo(HttpStatus.CREATED)
        // Null response means that is no recheable url,
        // the url hasn's been shorted
        assertThat(response_qr.body?.qr).isEqualTo(null)
    }

    //////////////////////////////////////////////////////////////////////
    // R3 : La URI acortada se puede obtener codificada en un código QR //
    //////////////////////////////////////////////////////////////////////

    // Get come qr created previously from a recheable
    @Test
    fun getQR_ok(){
        // Create the url short
        val response = shortUrl("https://www.thingiverse.com/", true)
        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        // Created ok
        assertThat(response.body?.url).isEqualTo("086c4f97")
        
        // Get the QR
        val response_qr = getQR("https://www.thingiverse.com/")
        assertThat(response_qr.statusCode).isEqualTo(HttpStatus.CREATED)
        // Check the qr generated is ok
        assertThat(response_qr.body?.qr).isEqualTo("iVBORw0KGgoAAAANSUhEUgAAAQAAAAEAAQAAAAB0CZXLAAABZElEQVR4Xu2YMXLDMAwEoUmhMk/QU/Q06ml8ip7g0oVGMO7AiccKJ6l1wytsGdyGwBmAbf6P7Bq4agCpAaQGkBpASgkwqpxfz2+vxQ9+nOQAPtcyHfPD1s2P+SeoBcTNlxp5IOfgrMgCCO2rMx3CAOzMN1UAL7UYyu1dV0sArUehzpEHZKXfxG4ONJ0RXnzLkyYlANdeKm6OVmwxePboyIIAfLxFHjBYJ4/56oJA2Nl3MzQnlBuuXlvVpQB/RghVjwR4bEth7ksTEwFQZ7TisHMU/5EeFwMYijckIF39+8srAODKe0yc6FF09Yx0fCZKBOBSTwDmhsclgVyTUGcCUXWeSAFN2apQZyy9egBujm0wT7AUWn9dvDfAZxaYSy+4rqvvDuRPtLZGvLOiCSAeE/XkYC08UQRwc44abhOdPNwdwAvzECeGNeLqagkAdaaPedL5h1MD+EMDSA0gNYDUAFIDSL0A6tDn5YuDneMAAAAASUVORK5CYII=")
    }

    // Create a url shorted but not the qr
    @Test
    fun getQR_notCreated(){
        // Create the url short
        val response = shortUrl("https://www.ebay.es/", false)
        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        // Created ok
        assertThat(response.body?.url).isEqualTo("ac984226")
        
        // Get the QR
        val response_qr = getQR("https://www.ebay.es/")
        assertThat(response_qr.statusCode).isEqualTo(HttpStatus.CREATED)
        // Empty response
        assertThat(response_qr.body?.qr).isEqualTo("")
    }

    ////////////////////////////
    // R6 : Login de usuarios //
    ////////////////////////////

    // Try login
    @Test
    fun getQR_notCreated(){

        val headers = HttpHeaders()

        val data = 

        restTemplate.postForEntity(
            "http://localhost:$port/api/shorter",
            HttpEntity(data, headers), ShortOut::class.java
        )
    }

    ////////////////////////
    // Auxiliar functions //
    ////////////////////////

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

    private fun getQR(u: String): ResponseEntity<QrOut>{
        val headers = HttpHeaders()

        val data = QrIn(u)

        return restTemplate.postForEntity(
            "http://localhost:$port/api/qr",
            HttpEntity(data, headers), QrOut::class.java
        )
    }

}