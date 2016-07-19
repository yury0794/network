package kr.ac.sungkyul.network.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class EchoServer3ReceiveThread extends Thread {
	private Socket socket;

	public EchoServer3ReceiveThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		// 4. 연결성공
		InetSocketAddress remoteAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
		String remoteHostAddress = remoteAddress.getAddress().getHostAddress();
		int remoteHostPort = remoteAddress.getPort();
		consoleLog("연결성공-" + remoteHostAddress + ":" + remoteHostPort);
		try {
			// 5. IOStream
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "utf-8"));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"), true);

			while (true) {
				// 6.데이터 읽기
				String data = br.readLine();
				if (data == null) { // 클라이언트가 연결을 끊었다.(정상종료)
					consoleLog("closed by client");
					return;
				}

				consoleLog("received :" + data);

				// 7. 데이터 쓰기
				pw.println(data);
			}
		} catch (SocketException e) {
			consoleLog("비정상적으로 클라이언트가 연결을 끊었습니다.");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 8. 데이터 통신 소켓 닫기
			try {
				if (socket != null && socket.isClosed() == false) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void consoleLog(String message){
		System.out.println("[ehoc server thread#"+getId()+"] "+message);
	}
}