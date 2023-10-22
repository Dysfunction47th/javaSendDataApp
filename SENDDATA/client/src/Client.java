import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int serverPort = 12345;

        try {
            Socket socket = new Socket(serverAddress, serverPort);
            System.out.println("서버에 연결되었습니다.");

            // 전송할 파일 선택
            String filePath = "data/sample.txt";
            File file = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            OutputStream outputStream = socket.getOutputStream();

            // 파일 전송
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            fileInputStream.close();
            socket.close();
            System.out.println("파일 전송 완료");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
