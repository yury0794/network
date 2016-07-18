package kr.ac.sungkyul.network.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TCPServer {
	private final static int SERVER_PORT = 1000;

	public static void main(String[] args) {
		ServerSocket serverSocket = null;

		try {
			// 1. 서버 소켓 생성
			serverSocket = new ServerSocket();

			// 2. 바인딩
			InetAddress inetAddress = InetAddress.getLocalHost();
			String serverAddress = inetAddress.getHostAddress();
			InetSocketAddress inetSocketAddress = new InetSocketAddress(serverAddress, SERVER_PORT);

			serverSocket.bind(inetSocketAddress);
			System.out.println("[server] bind - " + serverAddress + ":" + SERVER_PORT);

			// 3. accept 클라이언트로 부터 연결(요청)대기
			Socket socket = serverSocket.accept(); // blocking

			// 4. 연결성공
			InetSocketAddress remoteAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
			String remoteHostAddress = remoteAddress.getAddress().getHostAddress();
			int remoteHostPort = remoteAddress.getPort();
			System.out.println("[server] 연결 성공 from " + remoteHostAddress + ":" + remoteHostPort);

			try {
				// 5. IOStream
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();

				// 6.데이터 읽기
				byte[] buffer = new byte[256];
				int readBytes = is.read(buffer); // blocked
				if (readBytes <= -1) { // 클라이언트가 연결을 끊었다.(정상종료)
					System.out.println("[server] closed by client");
					return;
				}

				String data = new String(buffer, 0, readBytes, "utf-8");
				System.out.print("[server] received :" + data);

				// 7. 데이터 쓰기
				os.write(data.getBytes("utf-8"));
			} catch (SocketException e) {
				System.out.println("[server] 비정상적으로 클라이언트가 연결을 끊었습니다.");

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// 8. 데이터 통신 소켓 닫기
				if (socket != null && socket.isClosed() == false) {
					socket.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 9. 서버 소켓 닫기
				if (serverSocket != null && serverSocket.isClosed() == false) {
					serverSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}