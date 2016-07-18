package kr.ac.sungkyul.network.test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalHost {

	public static void main(String[] args) {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();

			String hostname = inetAddress.getHostName();
			String hostaddress = inetAddress.getHostAddress();
			byte[] addresses = inetAddress.getAddress();

			System.out.println("Host Name : " + hostname);
			System.out.println("Host Address : " + hostaddress);

			for (int i = 0; i < addresses.length; i++) {
				// 정수형으로 형변환을 해주거나 해주지 않아도 결과값이 같다.
				System.out.print(addresses[i] & 0x0000000ff);
				if (i < addresses.length - 1) {
					System.out.print(".");
				}
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}