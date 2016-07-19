package kr.ac.sungkyul.network.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class EchoClient2 {
	private static final String SERVER_IP = "220.67.115.226";
	private static final int SERVER_PORT = 1000;

	public static void main(String[] args) {
		Socket socket = null;
		Scanner scanner;
		try {
			// 1. 소켓 생성
			socket = new Socket();

			// 2. 서버 연결
			InetSocketAddress serverSocketAddress = new InetSocketAddress(SERVER_IP, SERVER_PORT);
			socket.connect(serverSocketAddress);

			// 3. IOStream 받아오기
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true);

			while (true) {
				// 4. 데이터 쓰기
				System.out.print(">>");
				scanner = new Scanner(System.in);
				String message = scanner.nextLine();
				pw.println(message);
				String messageEcho = br.readLine();
				
				if (messageEcho == null) { // 서버가 연결을 끊음
					System.out.println("[EchoClient] close by server");
					break;
				}

				System.out.println("<<" + messageEcho);
			}
			scanner.close();

		} catch (SocketException e) {
			System.out.println("[EchoClient] 비정상적으로 서버로부터 연결이 끊어졌습니다.");
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