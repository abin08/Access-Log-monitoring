/**
 * 
 */
package com.kafka.streams;

import java.util.Arrays;
import java.util.List;

/**
 * @author Abin K. Antony
 * 21-Mar-2019
 * @version 1.0
 */
public class Test2 {
    public static void main(String[] args) {
	String text = "64.242.88.10 - - [07/Mar/2004:16:47:12 -0800] "
		+ "\"GET /robots.txt HTTP/1.1\" 200 68";
	String pattern =  " ";
	List<String> list = Arrays.asList(text.split(pattern));
	int i =0 ;
	for(String str : list) {
	    System.out.println(i +  " : " + str);
	    i++;
	}
    }
}
