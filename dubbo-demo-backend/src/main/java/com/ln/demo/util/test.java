package com.ln.demo.util;

import org.I0Itec.zkclient.ZkClient;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class test {

	public static void main(String[] args) {
		//配置连接地址
//		ZkClient client = new ZkClient("172.19.0.10:2181");
//		//创建一个目录
//		client.createPersistent("/hhh");
//		//向这个目录中写信息
//		client.writeData("/hhh", "fdasjkldfs");
//		//从这个目录中读信息
//		Object readData = client.readData("/hhh");
		//打印输出
		System.out.println("3823,4054,4055,3363,4728,4909,5088,5168,4729,4730".indexOf(0));
		  Logger logger = LogManager.getLogger("RollingRandomAccessFile"); // Logger的名称
		  logger.info("info");
	}
}
