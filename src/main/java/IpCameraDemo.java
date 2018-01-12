import java.awt.Dimension;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

import javax.swing.JFrame;

import org.apache.commons.codec.binary.Base64;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.ds.ipcam.IpCamAuth;
import com.github.sarxos.webcam.ds.ipcam.IpCamDeviceRegistry;
import com.github.sarxos.webcam.ds.ipcam.IpCamDriver;
import com.github.sarxos.webcam.ds.ipcam.IpCamMode;

/**
 * Copied from https://github.com/sarxos/webcam-capture/issues/534
 */
public class IpCameraDemo {

    static {
        Webcam.setDriver(new IpCamDriver());
    }

    private static class AppWindow extends JFrame {

        /**
         * Serial.
         */
        private static final long serialVersionUID = 1L;

        public AppWindow() {

            setTitle("Demo app");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setResizable(false);
            setPreferredSize(new Dimension(640, 480));

            Webcam webcam = Webcam.getDefault();
            WebcamPanel panel = new WebcamPanel(webcam);

            add(panel);
            pack();
            setVisible(true);
        }
    }

    public static void main(String[] args) throws MalformedURLException {

        String name = "NC450";
        String url = "http://192.168.101.243:8080/stream/video/mjpeg";

        String user = "admin";
        String password = encode("12345");
        IpCamMode mode = IpCamMode.PUSH;
        IpCamAuth auth = new IpCamAuth(user, password);


        IpCamDeviceRegistry.register(name, url, mode, auth);

        new AppWindow();
    }

    private static String encode(String pwd) {
        final byte[] bytes = pwd.getBytes(StandardCharsets.UTF_8);
        final byte[] encoded = Base64.encodeBase64(bytes);
        try {
            return new String(encoded, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }
}