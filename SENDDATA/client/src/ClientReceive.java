import java.io.*;
import java.net.Socket;

public class ClientReceive {
    public static void main(String[] args) {
        String serverAddress = "localhost"; // 서버의 IP 주소
        int serverPort = 12347; // 서버의 포트 번호

        try {
            Socket socket = new Socket(serverAddress, serverPort);

            // 서버로부터 파일을 읽어 저장
            InputStream inputStream = socket.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream("D:\\\\coding_study\\\\javaSendDataApp\\\\SENDDATA\\\\client\\\\src/dataGet/test3.txt");

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }

            fileOutputStream.close();
            inputStream.close();
            socket.close();
            System.out.println("파일 수신 완료");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
