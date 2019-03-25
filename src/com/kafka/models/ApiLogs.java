package com.kafka.models;

import java.util.Arrays;
import java.util.List;

/**
 * @author Abin K. Antony
 * 20-Mar-2019
 * @version 1.0
 */
public class ApiLogs {
    String ip;
    String timeStamp;
    String method;
    String api;
    String status;
    Long bytes;
    
    public ApiLogs(String value) throws NumberFormatException{
	List<String> list = Arrays.asList(value.split(" "));
	this.ip = list.get(0);
	this.timeStamp = list.get(3).replaceAll("\\[", "") + " " + list.get(4).replaceAll("\\]", "");
	this.method = list.get(5);
	this.api = list.get(6);
	this.status = list.get(8);
	this.bytes = getBytes(list.get(9).replaceAll("[^\\d]", ""));
    }
    public String getIp() {
        return ip;
    }
    
    public String getTimeStamp() {
        return timeStamp;
    }

    public String getMethod() {
        return method;
    }

    public String getApi() {
        return api;
    }

    public String getStatus() {
        return status;
    }
    
    public Long getBytes() {
        return bytes;
    }
    
    private Long getBytes(String str) {
	Long bytes = 0L;
	try {
	    bytes = Long.parseLong(str);
	}catch (Exception e) {}
	return bytes;
    }
    
    @Override
    public String toString() {
	return ip + " " + timeStamp + " " + method + " " + api + " " + status + " " + bytes;
    }
}
