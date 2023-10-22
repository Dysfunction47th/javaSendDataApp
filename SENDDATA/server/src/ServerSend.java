import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSend {
    public static void main(String[] args) {
        int port = 12347; // 사용할 포트 번호

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("서버가 시작되었습니다. 클라이언트 연결 대기 중...");

            // 클라이언트 연결 대기
            Socket clientSocket = serverSocket.accept();
            System.out.println("클라이언트가 연결되었습니다.");

            // 파일을 읽어 클라이언트에게 전송
            String filePath = "D:\\\\coding_study\\\\javaSendDataApp\\\\SENDDATA\\\\server\\\\src\\\\data/test3.txt";
            File file = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            OutputStream outputStream = clientSocket.getOutputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            fileInputStream.close();
            clientSocket.close();
            serverSocket.close();
            System.out.println("파일 전송 완료");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
