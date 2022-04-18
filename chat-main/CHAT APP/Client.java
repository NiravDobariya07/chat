import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

class Client extends JFrame{

    Socket socket;

    BufferedReader br;
    PrintWriter out;
    //declare component

    private JLabel heading=new JLabel("Client Area");
    private JTextArea messaArea=new JTextArea();
    private JTextField messageinput=new JTextField();
    private Font font=new Font("Roboto",Font.PLAIN,20);

    //constructor

    public Client() {
        try {
            System.out.println("Sending request to server");
            socket = new Socket("127.0.0.1", 7777);
            //socket = new Socket("192.168.43.216", 7777);
            //socket = new Socket("enter ip of server's pc", port);
            System.out.println("connection done.");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvents();
            startReading();
            startWriting();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
        private void handleEvents() {
            messageinput.addKeyListener(new KeyListener(){

                @Override
                public void keyTyped(KeyEvent e) {
                
                    
                }

                @Override
                public void keyPressed(KeyEvent e) {
                
                    
                }

                @Override
                public void keyReleased(KeyEvent e) {
                
                    //System.out.println("key released"+e.getKeyCode());
                    if(e.getKeyCode()==10)
                    {
                       // System.out.println("you have pressed enter button");
                       String contentToSend=messageinput.getText();
                       messaArea.append("Me :"+contentToSend+"\n");
                       out.println(contentToSend);
                       out.flush();
                       messageinput.setText("");
                       messageinput.requestFocus();
                    }
                }

            });
    }
        private void createGUI() {
            //GUI code...
            this.setTitle("Client Message[END]");
            this.setSize(600,700);
            this.setLocationRelativeTo(null);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            //coding for component
            heading.setFont(font);
            messaArea.setFont(font);
            messageinput.setFont(font);

            //frame ka layout set karenge
            this.setLayout(new BorderLayout());

            //adding the components to frame
            this.add(heading,BorderLayout.NORTH);
            JScrollPane jScrollPane=new JScrollPane(messaArea);
            this.add(jScrollPane,BorderLayout.CENTER);
            this.add(messageinput,BorderLayout.SOUTH);


            heading.setIcon(new ImageIcon("logo.jpg"));
            heading.setHorizontalTextPosition(SwingConstants.CENTER);
            heading.setVerticalTextPosition(SwingConstants.BOTTOM);
            heading.setHorizontalAlignment(SwingConstants.CENTER);
            heading.setBorder(BorderFactory.createEmptyBorder(20, 20,20, 20));
            messaArea.setEditable(false);
            messaArea.setCaretPosition(messaArea.getDocument().getLength());
            messageinput.setHorizontalAlignment(SwingConstants.CENTER);
            this.setVisible(true);
    }
    //start reading
    public void startReading() {

        Runnable r1 = () -> {
            System.out.println("reader started..");
            try {
                while (true) {
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        System.out.println("Server terminated the chat");
                        JOptionPane.showMessageDialog(this, "server terminated the chat");
                        messageinput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    // System.out.println("Client :" + msg);
                    messaArea.append("Server :" + msg+"\n");
                }
            } catch (Exception e) {
                // System.out.println("connection is closed");
            }
        };

        new Thread(r1).start();
    }
        //start writing send[method]
    public void startWriting() {

        Runnable r2 = () -> {
            System.out.println("writer started..");

            try {
                while (!socket.isClosed()) {

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if (content.equals("exit")) {
                        socket.close();

                        break;
                    }
                }
               
            } catch (Exception e) {
            }
            System.out.println("connection is closed");
        };

        new Thread(r2).start();
    }

    public static void main(String[] args) {
        System.out.println("this is client...");
        new Client();
    }
}