package com.example.myapplication;

import java.io.UnsupportedEncodingException;

public class StringTrans {
    /**
     * 16进制直接转换成为字符串(无需Unicode解码)
     * @param hexStr  Byte字符串(Byte之间无分隔符
     * @author xxs
     * @return 对应的字符串
     */
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF"; //16进制能用到的所有字符 0-15
        char[] hexs = hexStr.toCharArray();//toCharArray() 方法将字符串转换为字符数组。
        int length = (hexStr.length() / 2);//1个byte数值 -> 两个16进制字符
        byte[] bytes = new byte[length];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            int position = i * 2;//两个16进制字符 -> 1个byte数值
            n = str.indexOf(hexs[position]) * 16;
            n += str.indexOf(hexs[position + 1]);
            // 保持二进制补码的一致性 因为byte类型字符是8bit的  而int为32bit 会自动补齐高位1  所以与上0xFF之后可以保持高位一致性
            //当byte要转化为int的时候，高的24位必然会补1，这样，其二进制补码其实已经不一致了，&0xff可以将高的24位置为0，低8位保持原样，这样做的目的就是为了保证二进制数据的一致性。
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }
    /**
     * 字符串转换成为16进制(无需Unicode编码)
     * @param str 待转换的ASCII字符串
     * @author xxs
     * @return byte字符串 （每个Byte之间空格分隔）
     */
    public static String str2HexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();//toCharArray() 方法将字符串转换为字符数组。
        StringBuilder sb = new StringBuilder(); //StringBuilder是一个类，可以用来处理字符串,sb.append()字符串相加效率高
        byte[] bs = new byte[0];//String的getBytes()方法是得到一个操作系统默认的编码格式的字节数组
        try {
            bs = str.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4; // 高4位, 与操作 1111 0000
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;  // 低四位, 与操作 0000 1111
            sb.append(chars[bit]);
            //sb.append(' ');//每个Byte之间空格分隔
        }
        return sb.toString().trim();
    }

    /**
     * @Title:hexString2String
     * @Description:16进制字符串转字符串
     * @param src
     * 16进制字符串
     * @return 字节数组
     * @throws
     */
    public static String hexString2String(String src,String oldchartype, String chartype) {
        byte[] bts=hexString2Bytes(src);
        try{if(oldchartype.equals(chartype))
            return new String(bts,oldchartype);
        else
            return new String(new String(bts,oldchartype).getBytes(),chartype);
        }
        catch (Exception e){

            return"";
        }
    }

    /**
     * @Title:hexString2Bytes
     * @Description:16进制字符串转字节数组
     * @param src
     * 16进制字符串
     * @return 字节数组
     * @throws
     */
    public static byte[] hexString2Bytes(String src) {
        int l = src.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            ret[i] = Integer.valueOf(src.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return ret;
    }

    /**
     * @Title:hexGBK2String
     * @Description:16进制GBK字符串转字符串
     * @param src
     * 16进制字符串
     * @return 字节数组
     * @throws
     */
    public static String hexGBK2String(String src) {

        return hexString2String(src,"GBK","UTF-8");
    }

    /**
     * @Title:hexUTF82String
     * @Description:16进制UTF-8字符串转字符串
     * @param src
     * 16进制字符串
     * @return 字节数组
     * @throws
     */
    public static String hexUTF82String(String src) {

        return hexString2String(src,"UTF-8","UTF-8");
    }



}
