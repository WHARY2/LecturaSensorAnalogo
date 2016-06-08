package com.digi.xzmk.receiveanalogdata;

import com.digi.xbee.api.RemoteXBeeDevice;
import com.digi.xbee.api.XBeeDevice;
import com.digi.xbee.api.exceptions.XBeeException;
import com.digi.xbee.api.io.IOLine;
import com.digi.xbee.api.io.IOSample;
import com.digi.xbee.api.listeners.IIOSampleReceiveListener;

/**
 * XBee ZigBee Mesh Kit Receive Analog Data Sample application.
 * 
 * <p>This sample Java application shows how to receive analog data from 
 * another XBee device on the same network using the XBee Java Library.</p>
 */
public class MainApp {
	
	/* Constants */
	
	// TODO Replace with the port where your coordinator module is connected to.
	private static final String PORT = "/dev/cu.usbserial-AL02BB5H";
	// TODO Replace with the baud rate of your coordinator module.
	private static final int BAUD_RATE = 9600;
	// Analog lines to monitor.
	private static final IOLine[] LINES = new IOLine[] {IOLine.DIO2_AD2, IOLine.DIO3_AD3};
	// Analog sample listener.
	private static AnalogSampleListener listener = new AnalogSampleListener();
	
	/**
	 * Application main method.
	 * 
	 * @param args Command line arguments.
	 */
	public static void main(String[] args) {
		System.out.println("+----------------------------------------------+");
		System.out.println("|          Receive Analog Data Sample          |");
		System.out.println("+----------------------------------------------+\n");
		
		XBeeDevice myDevice = new XBeeDevice(PORT, BAUD_RATE);
		
		try {
			myDevice.open();
			
			System.out.println("\nListening for IO samples... Rotate the potentiometer of any remote device.\n");
			
			myDevice.addIOSampleListener(listener);
			
		} catch (XBeeException e) {
			e.printStackTrace();
			myDevice.close();
			System.exit(1);
		}
	}
	
	/**
	 * Class to manage the received IO data.
	 */
	private static class AnalogSampleListener implements IIOSampleReceiveListener {
		@Override
		public void ioSampleReceived(RemoteXBeeDevice remoteDevice, IOSample ioSample) {
			for (IOLine line : LINES) {
				if (ioSample.hasAnalogValue(line)) {
					int value = ioSample.getAnalogValue(line);
					System.out.println("Analog data from '" + remoteDevice.get64BitAddress() + 
							"': " + value);
				}
			}
		}
	}
}