import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        int port = 12345;

        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("서버가 시작되었습니다. 클라이언트 연결 대기 중...");

            Socket clientSocket = serverSocket.accept();
            System.out.println("클라이언트가 연결되었습니다.");

            // 파일 수신
            String savePath = "D:\\coding_study\\javaSendDataApp\\SENDDATA\\server\\src\\data/test3.txt";
            InputStream inputStream = clientSocket.getInputStream();

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                FileOutputStream fileOutputStream = new FileOutputStream(savePath, true);
                fileOutputStream.write(buffer, 0, bytesRead);
                fileOutputStream.close();
            }

            System.out.println("파일 수신 및 저장 완료");
        } catch (IOException e) {
            e.printStackTrace();
        }

        
    }

    // 클라이언트로 파일을 전송하는 메서드
    private static void sendFileToClient(Socket clientSocket, String filePath) throws IOException {
        OutputStream outputStream = clientSocket.getOutputStream();
        FileInputStream fileInputStream = new FileInputStream(filePath);

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        fileInputStream.close();
        outputStream.close();
    }
}
