package kr.ac.sungkyul.network.echo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class EchoClient {
	private static final String SERVER_IP = "220.67.115.226";
	private static final int SERVER_PORT = 1000;

	public static void main(String[] args) {
		Socket socket = null;
		Scanner scanner = null;
		try {
			// 1. 소켓 생성
			socket = new Socket();

			// 2. 서버 연결
			InetSocketAddress serverSocketAddress = new InetSocketAddress(SERVER_IP, SERVER_PORT);
			socket.connect(serverSocketAddress);

			// 3. IOStream 받아오기
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();

			while (true) {
				// 4. 데이터 쓰기
				scanner = new Scanner(System.in);
				System.out.print(">>");
				String data = scanner.nextLine();
				os.write(data.getBytes("utf-8"));

				// 5. 데이터 읽기
				byte[] buffer = new byte[256];
				int readBytes = is.read(buffer); // block
				if (readBytes <= -1) { // 서버가 연결을 끊음
					System.out.println("[EchoClient] close by server");
					break;
				}
				data = new String(buffer, 0, readBytes, "utf-8");
				if (data.equals("quit")) {
					socket.close();
					break;
				}
				System.out.println("<<" + data);
			}

		} catch (SocketException e) {
			System.out.println("[EchoClient] 비정상적으로 서버로부터 연결이 끊어졌습니다.");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (scanner != null) {
					scanner.close();
				}

				if (socket != null && socket.isClosed() == false) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}