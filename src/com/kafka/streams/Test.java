package com.kafka.streams;

import java.util.Arrays;
import java.util.List;

import com.kafka.pojo.Logs;

public class Test {
    public static void main(String[] args) {
	String text = "64.242.88.10 - - [07/Mar/2004:16:47:12 -0800] \"GET /robots.txt HTTP/1.1\" 200 68";
	Logs logs = getLogs(text);
	System.out.println(logs.getIp());
	System.out.println(logs.getStatus());
	System.out.println(logs.toString());
    }
    
    static Logs getLogs(String text) {
	List<String> list = Arrays.asList(text.split(" "));
	Logs logs = new Logs();
	logs.setIp(list.get(0));
	logs.setStatus(list.get(list.size()-2));
	return logs;
    }
}
