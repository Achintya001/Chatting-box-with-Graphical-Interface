import java.net.*;
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
//import javax.swing.border.Border;
class Server extends JFrame{
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    private JLabel heading = new JLabel("Server area");
    private JTextArea messageArea =new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font = new Font("Roboto",Font.PLAIN,20);

    //constructor
    public Server()
    {
        try {
            server= new ServerSocket(7777);
            System.out.println("Server is ready for connection");
            System.out.println("Waiting...");
            socket=server.accept();
            br=new BufferedReader(new InputStreamReader(socket.getInputStream()) );
            out=new PrintWriter(socket.getOutputStream());
                createGUI();
                handleEvent();
            startReading();

             //startWriteing();


    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
    
    
         
    }
    private void createGUI()
    {//main gui create ki ha  yaha initital
        this.setTitle("Server Messenger");
        this.setSize(600,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //ab gui mai jo components liye ha uspe kaam karenge
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("logo.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
         //addingborder
         this.setLayout(new BorderLayout());
        //adding component to gui
        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);
        this.setVisible(true);

    }
   private void handleEvent()
    {
        messageInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                
                if(e.getKeyCode()==10)
                {
                    String contenttosend = messageInput.getText();
                    messageArea.append("me: "+contenttosend+"\n");
                    out.println(contenttosend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }
                        
                    
                
            


        }
    
        
        );


    }

    public void startReading()
    {
        Runnable r1 =()->{
           try{
            System.out.println("Reader Started ");
            while(!socket.isClosed())
            {
                String msg =br.readLine();
                if(msg.equals("exit"))
                {
                    System.out.println("Clint disconnected");

                    JOptionPane.showMessageDialog(this,"Client disconnected");
                    messageInput.setEnabled(false);
                    socket.close();
                    break;

                }
                // System.out.println("Client: "+msg);
                messageArea.append("Client: "+msg+"\n");
            }
        
        }
            catch(Exception e)
            {
                System.out.println("Clint closed");
            }

        };new Thread(r1).start();
    }
    public void startWriteing()
    {
        Runnable r2=()->{
           System.out.println("WRiter Started");
           try{ 
           while(!socket.isClosed())
            {
                
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content =br1.readLine();
                    

                    out.println(content);
                    out.flush();
                    if(content.equals("exit"))
            {
                socket.close();
                break;
            }
        }
    }
                
                catch(Exception e)
                {
                    System.out.println("Connection closed");
                }
            

        };new Thread(r2).start();

    }

    public static void main(String []args)
    {
        System.out.print("this is server... going to start server");
        new Server();
    }
}