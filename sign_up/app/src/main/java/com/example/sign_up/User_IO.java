package com.example.sign_up;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class User_IO {

    public static boolean saveinfos(String Uid, String Upsd,String Uname, Context context){
        String msg=null;
        FileOutputStream fos=null;
        try{
        fos=context.openFileOutput("data",Context.MODE_PRIVATE);
        msg=Uid+":"+Upsd+":"+Uname;
        fos.write(msg.getBytes());
        return true;
    }  catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static List<String> get_userinfos(Context context){
        FileInputStream fis=null;
        try {
            fis=context.openFileInput("data");
            byte[] buff=new byte[fis.available()];
            fis.read(buff);
            String msg=new String(buff);
            String[] userinfos=msg.split(":");
            List<String> userList=new LinkedList<>();
            userList.add(0,userinfos[0]);
            userList.add(1,userinfos[1]);
            userList.add(2,userinfos[2]);
            return userList;
        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
