import ru.spb.sergeyz.nc450.CameraApi

fun main(args: Array<String>) {
    println("asd")


    val base = "http://192.168.101.243/"

//    val encodeToString =
//    println(encodeToString)

    val cameraApi = CameraApi(base, "admin", "12345")
    println(cameraApi)
}

