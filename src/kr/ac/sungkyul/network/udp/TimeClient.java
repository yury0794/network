package kr.ac.sungkyul.network.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class TimeClient {
	private static final String SERVER_IP = "220.67.115.226";
	private static final int SERVER_PORT = 1000;
	private static final int BUFFER_SIZE = 1024;

	public static void main(String[] args) {
		Scanner scanner = null;
		DatagramSocket socket = null;

		try {
			// 0. 키보드 연결
			scanner = new Scanner(System.in);

			// 1. 소켓생성
			socket = new DatagramSocket(); // 보내는 놈은 포트 안써도 됨

			while (true) {
				// 2. 사용자 입력값 받음
				System.out.print(">>");
				String message = scanner.nextLine();
				if ("quit".equals(message)) {
					break;
				}

				// 3. 데이터 송신
				byte[] sendData = message.getBytes(StandardCharsets.UTF_8);
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
						new InetSocketAddress(SERVER_IP, SERVER_PORT)); // 받는 놈
																		// 주소
				socket.send(sendPacket);

				// 4. 데이터 수신
				DatagramPacket receivePacket = new DatagramPacket(new byte[BUFFER_SIZE], BUFFER_SIZE);
				socket.receive(receivePacket); // blocking
				SimpleDateFormat format = new SimpleDateFormat(" (yyyy-MM-dd HH:mm:ss a)");
				String timeData = format.format(new Date());
				String data = new String(receivePacket.getData(), 0, receivePacket.getLength(), StandardCharsets.UTF_8);
				System.out.println("<<" + data + timeData);
			}

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (scanner != null) {
				scanner.close();
			}

			if (socket != null && socket.isClosed() == false) {
				socket.close();
			}
		}
	}
}