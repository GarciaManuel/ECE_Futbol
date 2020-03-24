import java.io.*;  
import java.net.*;

public class MyClient {  
    public static void main(String[] args) {  
        try{      
            Socket s=new Socket("localhost",9876);  
            DataOutputStream dout=new DataOutputStream(s.getOutputStream());  
            DataInputStream dis=new DataInputStream(s.getInputStream());  
            
            dout.writeChar('M');
            dout.writeInt(0);
            dout.writeUTF("date");
            dout.writeBoolean(false);

            String  str=(String)dis.readUTF();
            System.out.println(str);  

            dout.flush();  
            dout.close();  
            dis.close();
            s.close();  
        } catch(Exception e){System.out.println(e);
        }  
    }  
}  