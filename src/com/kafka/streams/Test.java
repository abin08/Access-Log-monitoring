package com.kafka.streams;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) {
//	String text = "64.242.88.10 - - [07/Mar/2004:16:47:12 -0800] \"GET /robots.txt HTTP/1.1\" 200 68";
//	ApiLogs logs = new ApiLogs(text);
//	System.out.println(logs.toString());
//	String str = "32323_a";
//	System.out.println(str.replaceAll("[^\\d]", ""));
	
	/*try {
	    BufferedReader br = new BufferedReader(new FileReader("/home/ubuntu/Documents/Abin/Kafka/access_log"));
	    String text="";
	    int i=1;
	    while((text = br.readLine()) != null) {
		System.out.print(i++ + " ");
		List<String> list = Arrays.asList(text.split(" "));
		System.out.println(list.get(9)+" ");
		Long bytes = Long.parseLong(list.get(9).replaceAll("[^\\d]", ""));
		System.out.println(bytes);
	    }
	    br.close();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}*/
	String str = "-";
//	str.replaceAll("[^\\d]", "");
//	System.out.println(str);
	
	Long bytes = getBytes(str);
	 System.out.println(bytes);
    }
    private static Long getBytes(String str) {
	Long bytes = 0L;
	try {
	    bytes = Long.parseLong(str);
	}catch (Exception e) {}
	return bytes;
    }
   
}
