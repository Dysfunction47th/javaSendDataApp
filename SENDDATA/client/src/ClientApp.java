import java.io.*;
import java.net.Socket;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClientApp extends JFrame {

    private JTextField filePathTextField;
    private JButton sendButton;
    private JFileChooser fileChooser;
    private JButton receiveButton;

    public ClientApp() {
        super("File Send Client");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        filePathTextField = new JTextField(20);
        sendButton = new JButton("Send File");
        fileChooser = new JFileChooser();
        receiveButton = new JButton("Receive File");

        // 클라이언트에서 서버로부터 데이터를 저장하는 버튼
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendFile();
            }
        });
        

        // 클라이언트에서 서버로부터 데이터를 받아 저장하는 버튼
        receiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                receiveFile();
            }
        });

        add(filePathTextField);
        add(sendButton);
        add(receiveButton);
        setVisible(true);
    }


    private void sendFile() {
        String filePath = filePathTextField.getText();
        fileChooser.setCurrentDirectory(new File("D:\\coding_study\\javaSendDataApp\\SENDDATA"));
        if (filePath.isEmpty()) {
            int result = fileChooser.showOpenDialog(sendButton);
            if (result == JFileChooser.APPROVE_OPTION) {
                filePath = fileChooser.getSelectedFile().getAbsolutePath();
            } else {
                return;
            }
        }
    
        File selectedFile = new File(filePath);
        String fileName = selectedFile.getName(); // 파일 이름 추출
        System.out.println(fileName);
        try {
            Socket socket = new Socket("localhost", 12345);
            System.out.println("Connected to server.");
    
            // 파일 이름을 서버에 전송
            OutputStream fileNameOutputStream = socket.getOutputStream();
            fileNameOutputStream.write(fileName.getBytes());
            fileNameOutputStream.flush();
    
            FileInputStream fileInputStream = new FileInputStream(filePath);
            OutputStream outputStream = socket.getOutputStream();
    
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
    
            fileInputStream.close();
            outputStream.close();
            socket.close();
    
            JOptionPane.showMessageDialog(this, "File sent successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 데이터베이스 연결 정보 설정
        String dbUrl = "jdbc:mariadb://localhost:3306/filenamedb"; // MariaDB 서버 주소와 데이터베이스 이름
        String dbUser = "root"; // 데이터베이스 사용자 이름
        String dbPassword = "root"; // 데이터베이스 암호

        // 파일 이름
        String fileNameSql = fileName;

        Connection connection = null;
        try {
            // 데이터베이스 연결
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            // SQL 쿼리 작성 (파일 이름을 files 테이블에 삽입)
            String insertQuery = "INSERT INTO fname ( " + fileNameSql + " ) VALUES (?)";

            // PreparedStatement를 사용하여 SQL 쿼리 실행
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, fileName);

            // SQL 쿼리 실행
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("파일 이름이 데이터베이스에 성공적으로 저장되었습니다.");
            } else {
                System.out.println("파일 이름 저장에 실패했습니다.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 연결 종료
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


    }
    

    private void receiveFile() {
        // JFileChooser 객체를 생성합니다.
        JFileChooser fileChooser = new JFileChooser();
    
        // JFileChooser의 기본 위치를 설정합니다.
        fileChooser.setCurrentDirectory(new File("D:\\coding_study\\javaSendDataApp\\SENDDATA\\client\\src/dataGet"));
    
        // JFileChooser의 기본 파일 이름을 설정합니다.
        fileChooser.setSelectedFile(new File("receivedFile.txt"));
    
        // JFileChooser 대화 상자를 표시합니다.
        int result = fileChooser.showSaveDialog(this);
    
        // 사용자가 "저장" 버튼을 클릭한 경우
        if (result == JFileChooser.APPROVE_OPTION) {
            // 선택한 파일을 가져옵니다.
            File selectedFile = fileChooser.getSelectedFile();
    
            try {
                // 서버에 연결합니다.
                Socket socket = new Socket("localhost", 12345);
                System.out.println("Connected to server.");
    
                // 서버로부터 데이터를 읽을 입력 스트림을 생성합니다.
                InputStream inputStream = socket.getInputStream();
    
                // 선택한 파일에 데이터를 쓸 출력 스트림을 생성합니다.
                FileOutputStream fileOutputStream = new FileOutputStream(selectedFile);
    
                // 서버로부터 데이터를 읽고 선택한 파일에 씁니다.
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }
    
                // 출력 스트림과 입력 스트림을 닫습니다.
                fileOutputStream.close();
                inputStream.close();
                socket.close();
    
                // 파일이 성공적으로 수신되었음을 사용자에게 알립니다.
                JOptionPane.showMessageDialog(this, "File received successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        new ClientApp();
    }
}
