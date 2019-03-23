package com.kafka.streams;

import com.kafka.models.ApiLogs;

public class Test {
    public static void main(String[] args) {
//	String text = "64.242.88.10 - - [07/Mar/2004:16:47:12 -0800] \"GET /robots.txt HTTP/1.1\" 200 68";
//	ApiLogs logs = new ApiLogs(text);
//	System.out.println(logs.toString());
	String str = "32323_a";
	System.out.println(str.replaceAll("[^\\d]", ""));
    }
    
}
