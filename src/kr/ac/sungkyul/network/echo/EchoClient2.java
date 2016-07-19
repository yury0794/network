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
	private static final int SERVER_PORT = 3000;

	public static void main(String[] args) {
		Socket socket = null;
		Scanner scanner = null;

		try {

			// 키보드 연결
			scanner = new Scanner(System.in);

			// 소켓 생성
			socket = new Socket();

			// 서버연결
			InetSocketAddress serverSocketAddress = new InetSocketAddress(SERVER_IP, SERVER_PORT);
			socket.connect(serverSocketAddress);

			// IOStream 받아오기
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true);

			while (true) {
				// 메세지 입력
				System.out.print(">>");
				String message = scanner.nextLine();

				if ("exit".equals(message)) {
					break;
				}

				// 메세지 보내기
				pw.println(message);

				// 메세지 다시 받기
				String messageEcho = br.readLine();
				if (messageEcho == null) { // 서버가 연결을 끊음
					System.out.println("[client] close by server");
					break;
				}

				// 받은 메세지 출력
				System.out.println("<<" + messageEcho);
			}
		} catch (SocketException e) {
			System.out.println("[client] 비정상적으로 서버로 부터 연결이 끊어졌습니다." + e);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				// 소켓 닫기
				if (socket != null && socket.isClosed() == false) {
					socket.close();
				}

				// 키보드 닫기
				if (scanner != null) {
					scanner.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}