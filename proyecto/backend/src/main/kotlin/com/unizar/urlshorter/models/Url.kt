package com.unizar.urlshorter.models

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import com.google.common.hash.Hashing
import java.nio.charset.StandardCharsets
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix

@Document(collection = "urls")
class Url {
    @Id
    var id = "" 
    var url = ""
    var shorter = ""
    var isValid = false
    var clicks  = 0
    var qr: BitMatrix? = null

    constructor(url: String){
        this.url = url
        this.shorter = Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8).toString()
        val writer = QRCodeWriter()
        // Encodes the shorted url in a BitMatrix, using 'QR_CODE' format.
        // In the frontend, the QR image could be displayed by converting the BitMatrix to a black&white Bitmap.
        this.qr = writer.encode(this.shorter, BarcodeFormat.QR_CODE, 512, 512)
    }

    fun validateUrl(){
        this.isValid = true
    }

    fun addClick(){
        clicks += 1
    }
}