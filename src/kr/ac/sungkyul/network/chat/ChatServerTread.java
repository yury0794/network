package kr.ac.sungkyul.network.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ChatServerTread extends Thread {
	private String nickname;
	private Socket socket;
	List<Writer> listWriters;

	public ChatServerTread(Socket socket, List<Writer> listWriters) {
		this.socket = socket;
		this.listWriters = listWriters;
	}

	@Override
	public void run() {
		BufferedReader br = null;
		PrintWriter pw = null;

		try {
			// 1. Remote Host Information
			InetSocketAddress inetSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
			String remoteHostAddress = inetSocketAddress.getHostName();
			int remoteHostPort = inetSocketAddress.getPort();
			System.out.println("연결됨 from " + remoteHostAddress + ":" + remoteHostPort);

			// 2. 스트림 얻기
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);

			// 3. 요청 처리
			while (true) {
				String request = br.readLine();
				if (request == null) {
					System.out.println("클라이언트로 부터 연결 끊김");
					doQuit(pw);
					break;
				}

				// 4. 프로토콜 분석
				String[] tokens = request.split(":");
				if ("join".equals(tokens[0])) {
					doJoin(tokens[1], pw);
				} else if ("message".equals(tokens[0])) {
					doMessage(tokens[1]);
				} else if ("quit".equals(tokens[0])) {
					doQuit(pw);
					break;
				} else {
					System.out.println("에러:알수 없는 요청(" + tokens[0] + ")");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			doQuit(pw);
		} finally {
			try {
				// 자원정리
				br.close();
				pw.close();
				if (socket.isClosed() == false) {
					socket.close();
				}
			} catch (IOException ex) {
				System.out.println("error:" + ex);
			}
		}
	}

	private void doJoin(String nickName, Writer writer) {
		this.nickname = nickName;

		String data = nickName + "님이 참여하였습니다.";
		broadcast(data);

		/* writer pool에 저장 */
		addWriter(writer);

		// ack
		PrintWriter pw = (PrintWriter) writer;
		pw.println("join:ok");
		pw.flush();
	}

	private void addWriter(Writer writer) {
		synchronized (listWriters) {
			listWriters.add(writer);
		}
	}

	private void broadcast(String data) {
		for (Writer writer : listWriters) {
			PrintWriter printWriter = (PrintWriter) writer;
			printWriter.println(data);
			printWriter.flush();
		}
	}

	private void doMessage(String message) {
		String data = nickname + ":" + message;

		for (Writer writer : listWriters) {
			PrintWriter printWriter = (PrintWriter) writer;
			printWriter.println(data);
			printWriter.flush();
		}
	}

	private void doQuit(Writer writer) {
		removeWriter(writer);

		String data = nickname + "님이 퇴장 하였습니다.";
		broadcast(data);
	}

	private void removeWriter(Writer writer) {
		listWriters.remove(writer);
	}
}