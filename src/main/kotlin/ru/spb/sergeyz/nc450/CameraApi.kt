package ru.spb.sergeyz.nc450

import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.util.Base64
import com.google.gson.JsonParser
import java.net.HttpCookie
import java.nio.charset.Charset

class CameraApi(val base: String, val login: String, val pass: String){

    val cameraSessionCookie: HttpCookie

    val cameraToken: String

    init {
        val passBase64 = Base64.encodeToString(pass.toByteArray(), Base64.NO_WRAP)
        val responseString = (base + "login.fcgi").httpPost(listOf(
                "Username" to login,
                "Password" to passBase64))
                .responseString(Charset.defaultCharset())
        val theString = responseString.third.component1()!!
        val root = JsonParser().parse(theString)
        cameraToken = root.asJsonObject.get("token").toString().trim('"')

        val list = responseString.second.headers["Set-Cookie"]!!
        cameraSessionCookie = HttpCookie.parse(list[0])[0]
    }

    fun turnRight() = move("e")

    fun turnLeft() = move("w")

    fun turnUp() = move("n")

    fun turnDown() = move("s")

    private fun move(direction: String) {
        executeSetTurnDirectionOp(direction, "start")
        Thread.sleep(1000L)
        executeSetTurnDirectionOp(direction, "stop")
    }

    fun recenter() {
        executeSetTurnDirectionOp("c", "start")
        Thread.sleep(4000L)
        executeSetTurnDirectionOp("c", "stop")
    }

    private fun executeSetTurnDirectionOp(direction: String, operation: String) {
        (base + "setTurnDirection.fcgi")
                .httpPost(listOf(
                        "operation" to operation,
                        "token" to cameraToken,
                        "direction" to direction))
                .header("Cookie"
                        to "${cameraSessionCookie.name}=${cameraSessionCookie.value}")
                .response()
    }
}