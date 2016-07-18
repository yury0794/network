package kr.ac.sungkyul.network.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class TCPClient {
	private static final String SERVER_IP = "220.67.115.226";
	private static final int SERVER_PORT = 1000;

	public static void main(String[] args) {
		Socket socket = null;
		try {
			// 1. 소켓 생성
			socket = new Socket();

			// 2. 서버 연결
			InetSocketAddress serverSocketAddress = new InetSocketAddress(SERVER_IP, SERVER_PORT);
			socket.connect(serverSocketAddress);

			// 3. IOStream 받아오기
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();

			// 4. 데이터 쓰기
			String data = "Hello World\n";
			os.write(data.getBytes("utf-8"));

			// 5. 데이터 읽기
			byte[] buffer = new byte[256];
			int readBytes = is.read(buffer); // block
			if (readBytes <= -1) { // 서버가 연결을 끊음
				System.out.println("[client] close by server");
				return;
			}
			data = new String(buffer, 0, readBytes, "utf-8");
			System.out.println("[client] received:" + data);

		} catch (SocketException e) {
			System.out.println("[client] 비정상적으로 서버로부터 연결이 끊어졌습니다.");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (socket != null && socket.isClosed() == false) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}