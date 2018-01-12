import ru.spb.sergeyz.nc450.CameraApi

fun main(args: Array<String>) {
    val base = "http://192.168.101.243/"
    val cameraApi = CameraApi(base, "admin", "12345")
    cameraApi.calibratedPosition3()
}

