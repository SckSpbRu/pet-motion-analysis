import com.github.kittinunf.fuel.util.Base64
import com.github.sarxos.webcam.Webcam
import com.github.sarxos.webcam.ds.ipcam.IpCamAuth
import com.github.sarxos.webcam.ds.ipcam.IpCamDeviceRegistry
import com.github.sarxos.webcam.ds.ipcam.IpCamDriver
import com.github.sarxos.webcam.ds.ipcam.IpCamMode
import org.openimaj.image.ImageUtilities
import org.openimaj.image.MBFImage
import org.openimaj.video.Video
import org.openimaj.video.VideoDisplay

fun main(args: Array<String>) {
    VideoDisplay.createVideoDisplay<MBFImage>(MyIpCamVideo)
}

object MyIpCamVideo : Video<MBFImage>() {
    val webcam: Webcam

    var myCurrentFrame: MBFImage

    var myCurrentRealTs: Long = 0L

    var myInitialTs: Long = 0L

    var requestingFirstTime = true

    init {
        Webcam.setDriver(IpCamDriver())
        val name = "NC450"
        val url = "http://192.168.101.243:8080/stream/video/mjpeg"
        val user = "admin"
        val password = Base64.encodeToString("12345".toByteArray(), Base64.NO_WRAP)
        val mode = IpCamMode.PUSH
        val auth = IpCamAuth(user, password)
        IpCamDeviceRegistry.register(name, url, mode, auth)
        webcam = Webcam.getDefault()
        webcam.open()

        myCurrentFrame = ImageUtilities.createMBFImage(webcam.image, false)
    }

    private fun doReadFrameFromWebCamera() {
        if (requestingFirstTime) {
            myInitialTs = System.currentTimeMillis()
            requestingFirstTime = false
        }
        val image = webcam.image
        myCurrentFrame = ImageUtilities.createMBFImage(image, false)
        myCurrentRealTs = System.currentTimeMillis()
    }

    override fun hasNextFrame(): Boolean {
        return true
    }

    override fun reset() {
    }

    override fun countFrames(): Long {
        return -1
    }

    override fun getFPS(): Double = 10.0

    override fun getNextFrame(): MBFImage {
        doReadFrameFromWebCamera()
        return myCurrentFrame
    }

    override fun getTimeStamp(): Long {
        return myCurrentRealTs - myInitialTs
    }

    override fun getCurrentFrame(): MBFImage {
        return myCurrentFrame
    }

    override fun getWidth(): Int = myCurrentFrame.width

    override fun getHeight(): Int = myCurrentFrame.height
}
