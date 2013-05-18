package com.scavanger.gpio;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;


public class GPIOWatcher {

	private Thread thread;
	private ArrayList<GPIOListener> listeners = new ArrayList<GPIOListener>();
	
	public GPIOWatcher(int port) throws IOException, InterruptedException {
		thread = new Thread(new TCPThread(port));
	}
	
	public void start() {
		thread.start();
	}
	
	public void stop() {
		thread.interrupt();
	}
	
	private void raise(boolean state) {		
		notifyGPIO(new GPIOEvent(this, state));
	}
	
	public void addListener(GPIOListener listener) {
		listeners.add(listener);
	}
	
	public void removeListene(GPIOListener listener) {
		listeners.remove(listener);
	}
	
	protected synchronized void notifyGPIO(GPIOEvent event) {
		for (GPIOListener l: listeners) {
			l.onGPIOChanged(event);
		}
	}

	class TCPThread implements Runnable {

		private ServerSocket server = null;
		private String lastState = "";

		public TCPThread(int port) {
			try {
				server = new ServerSocket(port);
				server.setSoTimeout(150000);
			} catch (IOException e) {
				e.printStackTrace();
			}
		};
		
		/**
		 * Better safe than sorry
		 */
		protected void finalize() throws IOException {
			if (server != null)
				server.close();
		}

		protected void handleConnection(Socket client) throws IOException {
			Scanner in = new Scanner(client.getInputStream());
			String s = in.nextLine();
			
			if (!lastState.equals(s))  
				GPIOWatcher.this.raise(s.equals("ON") ? true : false);
			
			lastState = s;
		}

		private void doWork() throws IOException {
			Socket client = null;
			while (!Thread.currentThread().isInterrupted()) {
				try {
					Thread.yield();
					client = server.accept();
					handleConnection(client);
				} catch (SocketTimeoutException e) {
				} finally {
					if (client != null)
						client.close();
				}
			}
			if (server != null)
				server.close();
		}

		@Override
		public void run() {
			try {
				doWork();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}