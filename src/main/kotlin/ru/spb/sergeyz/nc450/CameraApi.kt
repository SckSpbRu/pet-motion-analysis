package ru.spb.sergeyz.nc450

import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.util.Base64
import com.google.gson.JsonParser
import java.net.HttpCookie
import java.nio.charset.Charset

class CameraApi(val base: String, val login: String, val pass: String){

    val cameraSession: String

    val cameraToken: String

    init {
        val passBase64 = Base64.encodeToString(pass.toByteArray(), Base64.NO_WRAP)
        val responseString = (base + "login.fcgi").httpPost(listOf(
                "Username" to login,
                "Password" to passBase64))
                .responseString(Charset.defaultCharset())
        val theString = responseString.third.component1()!!
        val root = JsonParser().parse(theString)
        cameraToken = root.asJsonObject.get("token").toString()

        val list = responseString.second.headers["Set-Cookie"]!!
        val firstCookieParsed = HttpCookie.parse(list[0])
        cameraSession = firstCookieParsed[0].value
    }

    override fun toString(): String {
        return "CameraApi(base='$base', cameraSession='$cameraSession', cameraToken='$cameraToken')"
    }


}