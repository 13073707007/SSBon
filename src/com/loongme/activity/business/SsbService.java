package com.loongme.activity.business;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.loongme.activity.utils.Logger;

/**
 * 连接服务器
 * 
 * @author Dachun Li
 * 
 */
public class SsbService {
	// 服务器ip地址
	private final String TAG = "SSBon_Service";
	private String ip = "114.113.148.187";
	private String logMsg = "";
	private Socket socket;

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	private BufferedWriter writer;
	private InetSocketAddress isa = null;
	public static final int port = 7977;

	// 接收服务器信息
	public String ReceiveMsg(Socket socket) throws IOException {
		if (socket.getInputStream() != null) {
			Logger.e(TAG, "获取到输入流");
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "GBK"));
		String line;
		String txt = "";
		/*
		 * while ((line = reader.readLine()) != null) { txt += line + "\n"; }
		 */
		char[] temp = new char[800];
		reader.read(temp);
		txt = new String(temp).toString().trim();
		txt += "\n";
		Logger.e(TAG, "获取到了：：：" + txt);
		reader.close();
		return txt;
	}

	// 在向服务器发送消息之前，必须先链接到服务器。
	public void connecttoserver() throws UnknownHostException, IOException {
		socket = RequestSocket(ip, port);
		// socket = new Socket(ip, port);
		// 判断是否链接成功
		if (socket.isConnected()) {
			/*
			 * Message msg = new Message(); msg.what = CONNENTED;
			 * mHandler.sendMessage(msg);
			 */
			Logger.e(TAG, "服务器连接成功");
		} else {
			Logger.e(TAG, "连接服务器失败，请检查原因");
		}
	}

	/**
	 * 链接服务器
	 * 
	 * @param host
	 *            主机地址
	 * @param port
	 *            端口号
	 * @return
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	private Socket RequestSocket(String host, int port) throws UnknownHostException, IOException {
		Socket ConSocket = new Socket(); // 创建套接字地址，其中 IP
		isa = new InetSocketAddress(host, port); // 建立一个远程链接
		ConSocket.connect(isa);
		return ConSocket;
	}

	// 向服务器发送信息
	public void SendMsg(Socket socket, String msg) throws IOException {
		writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "GBK"));
		int len = msg.getBytes("GBK").length + 8;
		String bit = stringFill(len + "", 8, '0', true);
		msg = bit + msg;
		Logger.i("msg=========", msg);
		writer.write(msg);
		writer.flush();
	}

	/**
	 * 字符串补齐
	 * 
	 * @param source
	 *            源字符串
	 * @param fillLength
	 *            补齐长度
	 * @param fillChar
	 *            补齐的字符
	 * @param isLeftFill
	 *            true为左补齐，false为右补齐
	 * @return
	 */
	public static String stringFill(String source, int fillLength, char fillChar, boolean isLeftFill) {
		if (source == null || source.length() >= fillLength)
			return source;

		StringBuilder result = new StringBuilder(fillLength);
		int len = fillLength - source.length();
		if (isLeftFill) {
			for (; len > 0; len--) {
				result.append(fillChar);
			}
			result.append(source);
		} else {
			result.append(source);
			for (; len > 0; len--) {
				result.append(fillChar);
			}
		}
		return result.toString();
	}

	/**
	 * 转化字符串格式为GBK
	 * 
	 * @param msg
	 *            目标字符串
	 */
	public String converMsgToGBK(String msg) {
		String message;
		if (msg == null) {
			return "";
		}
		try {
			byte[] bytesStr = msg.getBytes();
			message = new String(bytesStr, "UTF-8");

			return message;
		} catch (Exception ex) {
			Logger.e(TAG, "catch a Exception!!");
			return msg;
		}
	}

}
