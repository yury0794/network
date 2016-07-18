package kr.ac.sungkyul.network.test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NSLookup {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			try {

				System.out.print(">>");
				String host = scanner.nextLine();
				if ("quit".equals(host)) {
					break;
				}

				// 사이트의 모든 호스트 주소를 불러와 배열에 담음
				InetAddress[] inetAddresses = InetAddress.getAllByName(host);
				for (InetAddress inetAddress : inetAddresses) {
					System.out.println(inetAddress.getHostAddress());
				}
			} catch (UnknownHostException e) {
				System.out.println("unknownhost");
			}
		}
		scanner.close(); // while문 안에 넣으면 IllegalStateException이 난다.
	}
}