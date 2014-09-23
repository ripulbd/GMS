package mir.gla.ac.uk.gms;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import org.xbill.DNS.Address;


public class DNSResolver {
	private ArrayList<HashMap<String, String>> dnsList;
	private File file;
	
	public DNSResolver(String fileName) {
		// TODO Auto-generated constructor stub
		dnsList = new ArrayList<HashMap<String, String>>();
		try {
			file =new File(fileName);
			 
    		//if file doesnt exists, then create it
    		if(!file.exists()){
    			file.createNewFile();
    		}
    		
    		FileReader in = new FileReader(file);
    	    BufferedReader br = new BufferedReader(in);
    	    String line;
    	    while ((line = br.readLine()) != null) {
    	    	String [] stringArray = line.split("[:]");
    	        HashMap<String, String> tmpMap = new HashMap<String, String>();
    	        stringArray[0] = stringArray[0].substring(0, stringArray[0].length() - 1);    	        
    	        stringArray[1] = stringArray[1].substring(1);
    	        tmpMap.put(stringArray[0], stringArray[1]);
    	        dnsList.add(tmpMap);
    	    }
    	    in.close();
    	    
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String getIPAddress(String domain){
		String ipAddress = "";
		
		for(HashMap<String, String> tmpMap : dnsList){
			if(tmpMap.containsKey(domain))ipAddress = tmpMap.get(domain);
		}
		if(ipAddress.equals("")){
			try {
				InetAddress addr = Address.getByName(domain);
				ipAddress = addr.getHostAddress();
				HashMap<String, String> tmpMap = new HashMap<String, String>();
    	        tmpMap.put(domain, ipAddress);
    	        dnsList.add(tmpMap);
    	        
    	      //true = append file
				FileWriter fileWritter;
				try {
					fileWritter = new FileWriter(file, true);
					BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
					bufferWritter.write(domain + "[:]" + ipAddress + "\n");
					bufferWritter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
    	        
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ipAddress;
	}
}
