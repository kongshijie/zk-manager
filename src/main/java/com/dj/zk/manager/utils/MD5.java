package com.dj.zk.manager.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @description:	本代码业至于网络
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">刘德建</a>
 * @Date	 2013-10-30 下午3:05:44
 */
public abstract class MD5 {
	
	private final static String DM5 = "MD5";
	
	/**
	 * MD5加密
	 * @param password 密码串（未加密）
	 * @return 密码串（已加密）
	 */
    public static String encrypt(String password) {
        try {
            MessageDigest alg = MessageDigest.getInstance(DM5);
            alg.update(password.getBytes());
            byte[] digesta = alg.digest();
            return byte2hex(digesta);
        } catch (NoSuchAlgorithmException NsEx) {
            return null;
        }
    }
    
    /**
	 * MD5加密
	 * @param password 密码串（未加密）
	 * @return 密码串（已加密）
	 */
    public static String encrypt(byte [] data) {
        try {
            MessageDigest alg = MessageDigest.getInstance(DM5);
            alg.update(data);
            byte[] digesta = alg.digest();
            return byte2hex(digesta);
        } catch (NoSuchAlgorithmException NsEx) {
            return null;
        }
    }
    
    private static String byte2hex(byte[] bstr) {
        StringBuffer hs = new StringBuffer();
        String stmp = "";
        for (int n = 0; n < bstr.length; n++) {
            stmp = (java.lang.Integer.toHexString(bstr[n] & 0XFF));
            if (stmp.length() == 1){
            		hs.append("0");
            		hs.append(stmp);
            }else{
                hs.append(stmp);
            }    
        }
        return hs.toString();
    }
  
 
}