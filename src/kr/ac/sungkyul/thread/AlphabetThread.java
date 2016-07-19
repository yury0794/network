package kr.ac.sungkyul.thread;

public class AlphabetThread extends Thread {
	@Override
	public void run() {
		for (int i = 'a'; i < 'z'; i++) {
			System.out.print((char) i);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}