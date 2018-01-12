import com.github.kittinunf.fuel.util.Base64
import com.github.sarxos.webcam.Webcam
import com.github.sarxos.webcam.ds.ipcam.IpCamAuth
import com.github.sarxos.webcam.ds.ipcam.IpCamDeviceRegistry
import com.github.sarxos.webcam.ds.ipcam.IpCamDriver
import com.github.sarxos.webcam.ds.ipcam.IpCamMode
import javax.imageio.ImageIO
import java.io.File

fun main(args: Array<String>) {
    Webcam.setDriver(IpCamDriver())
    val name = "NC450"
    val url = "http://192.168.101.243:8080/stream/video/mjpeg"
    val user = "admin"
    val password = Base64.encodeToString("12345".toByteArray(), Base64.NO_WRAP)
    val mode = IpCamMode.PUSH
    val auth = IpCamAuth(user, password)
    IpCamDeviceRegistry.register(name, url, mode, auth)

    val webcam = Webcam.getDefault()
    webcam.open()

    val outputfile = File("image.jpg")
    ImageIO.write(webcam.image, "jpg", outputfile)

    webcam.close()
}