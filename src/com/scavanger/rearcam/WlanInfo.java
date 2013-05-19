package com.scavanger.rearcam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;

public class WlanInfo {
	
	public static Context context;
	
	public static ArrayList<String> getIPsOfConnectedWlanClients() {
		BufferedReader br = null;
	    ArrayList<String> ips = new ArrayList<String>();
		try {
	        br = new BufferedReader(new FileReader("/proc/net/arp"));
	        String line;
	        while ((line = br.readLine()) != null) {
	            String[] splitted = line.split(" +");
	            if (splitted != null && 
	            	splitted.length >= 5 && 
	            	splitted[5].equals("wlan0") &&
	            	!splitted[3].equals("00:00:00:00:00:00")) 
	            {
	                ips.add(splitted[0]);	                
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            br.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    return ips;
	}
	
	public static String getIpOfDefaultGateway() {
		
	    WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcp = wifi.getDhcpInfo();
	    return intToIp(dhcp.gateway);
	}
	
	private static String intToIp(int addr) {
	    return  ((addr & 0xFF) + "." + 
	            ((addr >>>= 8) & 0xFF) + "." + 
	            ((addr >>>= 8) & 0xFF) + "." + 
	            ((addr >>>= 8) & 0xFF));
	}

}
