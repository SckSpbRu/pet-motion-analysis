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

    fun turnRight() = moveSecond("e")

    fun turnLeft() = moveSecond("w")

    fun turnUp() = moveSecond("n")

    fun turnDown() = moveSecond("s")

    fun recenter() {
        move("c", 5000)
    }

    fun calibratedPosition3() {
        recenter()
        move("e", 2200)
        move("s", 2650)
    }

    fun moveSecond(direction: String) = move(direction, 1000)

    private fun move(direction: String, execFor: Long) {
        executeSetTurnDirectionOp(direction, "start")
        Thread.sleep(execFor)
        executeSetTurnDirectionOp(direction, "stop")
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