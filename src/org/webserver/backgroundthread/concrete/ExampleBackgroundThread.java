package org.webserver.backgroundthread.concrete;

public class ExampleBackgroundThread extends Thread {

	public ExampleBackgroundThread() {
	}

	@Override
	public void run() {
		try {
			sleep(10000);
			System.out.println("Hello world from parallel thread!");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
