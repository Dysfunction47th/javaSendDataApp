import java.io.*;
import java.net.Socket;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class ClientApp extends JFrame {

    private JTextField filePathTextField;
    private JButton sendButton;
    private JFileChooser fileChooser;
    private JButton receiveButton;

    // Socket 변수를 추가
    private Socket socket;
    
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
                // receiveFile();
                requestFile();
            }
        });

        add(filePathTextField);
        add(sendButton);
        add(receiveButton);
        setVisible(true);
    }


    private void sendFile() {
        String filePath = filePathTextField.getText();
        // fileChooser.setCurrentDirectory(new File("D:\\coding_study\\javaSendDataApp\\SENDDATA"));
        fileChooser.setCurrentDirectory(new File("C:\\check\\javaSendDataApp\\SENDDATA"));
        
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



    }
    


    
    private void requestFile() {
        // fileChooser.setCurrentDirectory(new File("D:\\coding_study\\javaSendDataApp\\SENDDATA\\server\\src/data"));
        fileChooser.setCurrentDirectory(new File("C:\\check\\javaSendDataApp\\SENDDATA\\client\\src\\data"));

        fileChooser.setSelectedFile(new File("receivedFile.txt"));
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    try {
                        if (socket == null || socket.isClosed()) {
                            socket = new Socket("localhost", 12345);
                            System.out.println("Connected to server.");
                        }

                        String requestFileName = selectedFile.getName(); // 선택한 파일 이름

                        OutputStream fileNameOutputStream = socket.getOutputStream();
                        fileNameOutputStream.write(requestFileName.getBytes());
                        fileNameOutputStream.flush();

                        // String savePath = "D:\\coding_study\\javaSendDataApp\\SENDDATA\\client\\src/dataGet\\" + requestFileName;
                        String savePath = "C:\\check\\javaSendDataApp\\SENDDATA\\client\\src\\dataGet\\" + requestFileName;

                        FileOutputStream fileOutputStream = new FileOutputStream(savePath);

                        InputStream inputStream = socket.getInputStream();
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, bytesRead);
                        }

                        fileOutputStream.close();
                        JOptionPane.showMessageDialog(ClientApp.this, "File received successfully: " + requestFileName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };

            worker.execute();
        }
    }
    
    
    public static void main(String[] args) {
        new ClientApp();
    }
}
