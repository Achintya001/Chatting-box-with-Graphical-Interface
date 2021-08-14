import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

import java.net.*;

import javax.swing.*;




public class clint extends JFrame
 {
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    //Declare component
private JLabel heading=new JLabel("Clint area");
private JTextArea messageArea=new JTextArea();
private JTextField messageInput=new JTextField();
private Font font = new Font("Roboto",Font.PLAIN,20);
//constructor
    public clint()
    {
        try{
             System.out.println("Sending request to server");
             socket=new Socket("127.0.0.1",7777);
             System.out.println("Connection done!'_'!");
             br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
             out=new PrintWriter(socket.getOutputStream());
            createGUI();
            handleEvents();
             startReading();
            // startWriteing();
    
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    private void createGUI()
    {

        this.setTitle("CLIENT MEssenger[END]");
        this.setSize(600,700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //codeing for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("logo.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
        //frame border layout
        this.setLayout(new BorderLayout());
        //adding component to window
        this.add(heading,BorderLayout.NORTH);
        JScrollPane  jScrollPane =new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);
        this.setVisible(true);

    }
    private void handleEvents()
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
                // TODO Auto-generated method stub
                System.out.println("key release "+e.getKeyCode());
                if(e.getKeyCode()==10)
                {
                    String contentToSend=messageInput.getText();
                    messageArea.append("me: "+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
                
            }});
            
        
    }
    public void startReading()
    { System.out.println("Reader Started");
        
            Runnable r1=()->{
              try{
                while(!socket.isClosed()){
                
                String msg = br.readLine();
                
                if(msg.equals("exit"))
                {
                    System.out.println("server disconnexted");
                    JOptionPane.showMessageDialog(this,"Server Terminate the chat");
                    messageInput.setEnabled(false);
                    socket.close();
                    
                    break;
                }
                // System.out.println("Server: "+msg);
                messageArea.append("Server: "+msg+"\n");
            }
        }
            catch(Exception e)
        {

           System.out.println("server closed");
        }

           
    
         } ;new Thread(r1).start();
   
    }
    public void startWriteing(){
      
        System.out.println("Writer started");
      Runnable r2=()->{ 
        try{
        while(!socket.isClosed()){  
        
            
            BufferedReader br1 =new BufferedReader(new InputStreamReader(System.in));
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
           System.out.print("connection closed") ;
        }
    
    };new Thread(r2).start();

    }
    
    public static void main(String []args)
    {
        System.out.print("this is clint");
        new clint();
    }
}
