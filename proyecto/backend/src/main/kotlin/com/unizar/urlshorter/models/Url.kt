package com.unizar.urlshorter.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import com.google.common.hash.Hashing
import java.nio.charset.StandardCharsets
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.client.j2se.MatrixToImageWriter
import java.io.ByteArrayOutputStream
import java.util.*

@Document(collection = "urls")
class Url {
    @Id
    var id = "" 
    var url = ""
    var shorter = ""
    var isValid = false
    var clicks  = 0
    var qr  = ""

    constructor(url: String){
        this.url = url
        this.shorter = Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8).toString()
        val writer = QRCodeWriter()
        // Encodes the shorted url in a BitMatrix, using 'QR_CODE' format.
        val size = 512 // qr size in pixels
        val matQr = writer.encode(this.shorter, BarcodeFormat.QR_CODE, size, size)
        val bos = ByteArrayOutputStream()
        // Encodes BitMatrix as a PNG type Image, then its coded as a Base64 type array.
        MatrixToImageWriter.writeToStream(matQr, "PNG", bos)
        val qrString = Base64.getEncoder().encodeToString(bos.toByteArray())
        if (qrString != null){
            this.qr = qrString
        }
        // In the front, this could be displayed as a base64 coded png , like "<img src={`data:image/png;base64,${image}`} />"

    }

    fun validateUrl(){
        this.isValid = true
    }

    fun addClick(){
        clicks += 1
    }
}