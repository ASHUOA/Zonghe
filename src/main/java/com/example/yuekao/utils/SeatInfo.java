package com.example.yuekao.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by fanyishuo on 2017/7/26.
 */

public class SeatInfo {
    public static String add(InputStream is) throws IOException {
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        byte[] buffer=new byte[1024];
        int len=0;
        while ((len=is.read(buffer))!=-1){
            os.write(buffer,0,len);
        }
        is.close();
        os.close();
        return os.toString();
    }
}
