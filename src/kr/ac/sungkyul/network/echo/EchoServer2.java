package kr.ac.sungkyul.network.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class EchoServer2 {
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
			System.out.println("[EchoServer] bind-" + serverAddress + ":" + SERVER_PORT);

			// 3. accept 클라이언트로 부터 연결(요청)대기
			Socket socket = serverSocket.accept(); // blocking

			// 4. 연결성공
			InetSocketAddress remoteAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
			String remoteHostAddress = remoteAddress.getAddress().getHostAddress();
			int remoteHostPort = remoteAddress.getPort();
			System.out.println("[EchoServer] 연결성공-" + remoteHostAddress + ":" + remoteHostPort);
			try {
				// 5. IOStream
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true);

				while (true) {
					// 6.데이터 읽기
					String data = br.readLine();
					if (data == null) { // 클라이언트가 연결을 끊었다.(정상종료)
						System.out.println("[EchoServer] closed by client");
						break;
					}

					System.out.println("[server] received :" + data);

					// 7. 데이터 쓰기
					pw.println(data);
				}
			} catch (SocketException e) {
				System.out.println("[EchoServer] 비정상적으로 클라이언트가 연결을 끊었습니다.");

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