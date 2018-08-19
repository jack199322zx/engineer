package com.nino.engineer.utils.log;

import com.nino.engineer.utils.date.GetTodayTime;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 系统日志类
 * @author Administrator
 *
 */
public class LogClass {

	public static String log_path = "log";
	
    public static void logResult(String sWord) {
    	String time = new GetTodayTime().GetNetworkTodayTime("yyyy-MM-dd");
    	File file = new File(log_path+time);
    	if(!file.exists()){
    		file.mkdirs();
    	}
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + time + File.separator + new GetTodayTime().GetNetworkTodayTime("yyyy-MM-dd HH时mm分ss秒") +".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                	writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
}
