import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApp {

    public static void main(String[] args) {
        int port = 12345;

        try {
            // 서버 시작
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("서버가 시작되었습니다. 클라이언트 연결 대기 중...");

            // 클라이언트 연결 수락
            Socket clientSocket = serverSocket.accept();
            System.out.println("클라이언트가 연결되었습니다.");

            // 파일 수신
            InputStream inputStream = clientSocket.getInputStream();



// 파일 이름 얻기
byte[] fileNameBytes = new byte[1024];
System.out.println("fileNameBytes" + fileNameBytes);

int bytesRead = inputStream.read(fileNameBytes);
System.out.println("bytesRead" + bytesRead);

// String fileName = new String(fileNameBytes, 0, bytesRead);
String fileName = new String(fileNameBytes, 0, bytesRead).trim(); // 파일 이름은 공백을 제거
System.out.println("Server receives: " + fileName); // 클라이언트로부터 받은 파일 이름 확인

// 파일 저장
// String savePath = "D:\\coding_study\\javaSendDataApp\\SENDDATA\\server\\src\\data\\" + fileName;
String savePath = "C:\\check\\javaSendDataApp\\SENDDATA\\server\\src\\data\\" + fileName;

FileOutputStream fileOutputStream = new FileOutputStream(savePath);

byte[] buffer = new byte[1024];
while ((bytesRead = inputStream.read(buffer)) != -1) {
    fileOutputStream.write(buffer, 0, bytesRead);
}

fileOutputStream.close();



            // 파일 수신 및 저장 완료
            System.out.println("파일 수신 및 저장 완료.");

            // 소켓 닫기
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
