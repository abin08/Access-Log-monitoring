package com.kafka.pojo;

import java.util.Arrays;
import java.util.List;

/**
 * @author Abin K. Antony
 * 20-Mar-2019
 * @version 1.0
 */
public class Logs {
    String ip;
    String timeStamp;
    String method;
    String api;
    String status;
    
    public Logs(String value){
	List<String> list = Arrays.asList(value.split(" "));
	this.ip = list.get(0);
	this.timeStamp = list.get(3);
	this.method = list.get(5);
	this.api = list.get(6);
	this.status = list.get(8);
    }
    
    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    @Override
    public String toString() {
	return ip + " " + status;
    }   
}
