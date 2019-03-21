package com.kafka.pojo;

/**
 * @author Abin K. Antony
 * 20-Mar-2019
 * @version 1.0
 */
public class Logs {
    String ip;
    String status;
    
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
