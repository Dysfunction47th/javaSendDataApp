import java.net.Socket;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;

public class FileClient {
    private String hostname;
    private int port;

    public static void main(String[] args) {
        FileClient fc = new FileClient("localhost", 25573);
    }

    public FileClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        connect();
    }

    private void connect() {
        Socket s = new Socket();
        try {
            s.connect( new InetSocketAddress(hostname, port) );
            InputStream in = s.getInputStream();
            FileOutputStream out = new FileOutputStream("data/test2.txt");

            byte[] buffer = new byte[8192];
            int bytesRead=0;
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                s.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}