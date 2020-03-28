import java.io.*;
import java.net.*;

public class Test {
    public static void main(String[] args) {
        try{
            Socket s=new Socket("localhost",9876);
            DataOutputStream dout=new DataOutputStream(s.getOutputStream());
            DataInputStream dis=new DataInputStream(s.getInputStream());

            dout.writeChar('T'); // objectTYpe
            dout.writeInt(0); // matchNum
            dout.writeUTF("fourHits"); // attribute
            dout.writeBoolean(false); //modify

//            dout.writeBoolean(true); //modify
//            dout.writeInt(1); // value

            dout.writeInt(0); // teamNum
//            dout.writeInt(2); // playerNum

//            dout.writeInt(4); // gameSet

//            String  str=(String)dis.readUTF();
//            System.out.print("Resp: ");
//            System.out.println(str);

            int  num=dis.readInt();
            System.out.print("Resp: ");
            System.out.println(num);


            dout.flush();
            dout.close();
            dis.close();
            s.close();
        } catch(Exception e){System.out.println(e);}
    }
}  