package kr.ac.sungkyul.network.chat;

import java.io.BufferedReader;
import java.io.IOException;

public class ChatClientReceiveThread extends Thread {
	private BufferedReader bufferedReader;

	ChatClientReceiveThread(BufferedReader bufferedReader) {
		this.bufferedReader = bufferedReader;
	}

	@Override
	public void run() {
		/* reader를 통해 읽은 데이터 콘솔에 출력하기 (message 처리) */
		try {
			while (true) {
				String line = bufferedReader.readLine();
				if (line == null) {
					break;
				}
				System.out.println(line);
			}
		} catch (IOException e) {
			System.out.println("안녕히 가세요");
		}
	}
}