package com.loongme.activity.utils;

import android.annotation.SuppressLint;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

public class DiskCache {

	public static final char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	private SecureRandom SHA1PRNG;

	@SuppressLint("TrulyRandom")
	public DiskCache() throws NoSuchAlgorithmException {
		SHA1PRNG = SecureRandom.getInstance("SHA1PRNG");
	}

	public String generateSalt(int length) {
		byte[] bytes = new byte[length / 2];
		SHA1PRNG.nextBytes(bytes);
		String r = hex(bytes);
		return r;
	}

	public String generateSalt() {
		return generateSalt(40);
	}

	@SuppressLint("UseValueOf")
	public String generateNumericSalt(int length) {
		StringBuffer buffer = new StringBuffer();
		int n = 0;
		while(n < length) {
			buffer.append(new Integer(SHA1PRNG.nextInt(10)).toString());
			n++;
		}
		return buffer.toString();
	}

	public static String sha1(byte[] buffer) {
		String result = null;
		try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
			result = hex(sha1.digest(buffer));
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String sha1(String s) {
		return sha1(s.getBytes());
	}

	public static String md5(byte[] buffer) {
		String result = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			result = hex(md5.digest(buffer));
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String md5(String s) {
		return md5(s.getBytes());
	}

	public static String hex(byte[] buffer) {
		StringBuffer result = new StringBuffer();
		for (int idx = 0; idx < buffer.length; ++idx) {
			byte b = buffer[idx];
			result.append(digits[(b & 0xf0) >> 4]);
			result.append(digits[b & 0x0f]);
		}
		return result.toString();
	}

	public static String checksum(File file) throws IOException {
		return checksum(file, 0, file.length());
	}

	public static String checksum(File file, long from, long length) throws IOException {
		FileInputStream in = new FileInputStream(file);
		FileChannel ch = in.getChannel();
		MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, from, length);
		String result = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteBuffer);
			result = hex(md5.digest());
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String checksum5(File file) throws IOException {
		return checksum5(file, 0, file.length());
	}

	public static String checksum5(File file, long from, long length) throws IOException {
		FileInputStream in = null;
		String result = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			in  = new FileInputStream(file);
			in.skip(from);
			int remaining = (int)length;
			byte[] buffer = new byte[1024];
			int n = 0;
			while((n = in.read(buffer, 0, remaining < 1024 ? remaining : 1024)) != -1) {
				md5.update(buffer, 0, n);
				if((remaining -= n) <= 0) {
					break;
				}
			}
			result = hex(md5.digest());
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String checksum32(File file) throws IOException {
		return checksum32(file, 0, file.length());
	}

	public static String checksum32(File file, long from, long length) throws IOException {
		InputStream in = null;
		try {
			CRC32 crc32 = new CRC32();
			in  = new FileInputStream(file);
			in.skip(from);
			in = new CheckedInputStream(in, crc32);
			int remaining = (int)length;
			byte[] buffer = new byte[1024];
			int n = 0;
			while((n = in.read(buffer, 0, remaining < 1024 ? remaining : 1024)) != -1) {
				if((remaining -= n) <= 0) {
					break;
				}
			}
			return Long.toHexString(crc32.getValue());
		} finally {
			in.close();
		}
	}
	public static synchronized void saveStringToFile(String filename,String str){
		
		BufferedWriter bw=null;		
		try {
			
			bw=new BufferedWriter(new FileWriter(filename));
			if(null != bw){
				bw.write(str);			
				bw.flush();
			}
		} catch (IOException e) {			
			e.printStackTrace();		
		}finally{			
			try {
				if(null != bw){
					bw.close();
					bw = null;
				}
			} catch (IOException e) {				
				e.printStackTrace();			
			}
		}
	}
	public static synchronized String readStringFromFile(String filename){
		String str = "",tmp = null;
		FileReader fr = null;
		BufferedReader br = null;
		try{
			fr = new FileReader(filename);
			if(fr != null){ 
				br = new BufferedReader(fr);
	            if(br != null){
	            	while((tmp = br.readLine()) != null){ 
	            		str += tmp;
	            	}
	            }
			}
        } 
        catch (IOException e) 
        {
            e.getStackTrace();
        }finally{
			try {				
				if(br != null)
					br.close();	
				if(fr != null)
					fr.close();
			} catch (IOException e) {				
				e.printStackTrace();		
			}
        }
		return str;
    }
}
