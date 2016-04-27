package com.werb.mycalendardemo.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by acer-pc on 2016/3/10.
 */
public class StreamFormat {

    public static String getTextFromStream(InputStream is){

        //设置已1字节来存储到流中
        byte[] b=new byte[1024];
        int len=0;
        //创建字节数组输出流，读取输入流的文本数据时，同步把数据写入字节数组输出流
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        try {
            while ((len=is.read(b))!=-1){
                bos.write(b,0,len);
            }
            String text=new String(bos.toByteArray());//这里可以设置编码方式
            bos.close();
            return text;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
}
