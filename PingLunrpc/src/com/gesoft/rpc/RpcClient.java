package com.gesoft.rpc;

import java.net.MalformedURLException;
import java.net.URL;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;

import com.gesoft.config.ConfigManager;


public class RpcClient{

	private String strRpcURL = "" ;
	private XmlRpcClientConfigImpl mConfig = null ;
	public static boolean bConnSucess = true ;
	public Object initParam = null ;
	private XmlRpcClient mClient = null ;
	String rpcUrl;
//	String strRpcUrl = "http://182.254.246.90:8101"; //me
	
	String strRpcUrl= "http://"+ConfigManager.getInstance().getConfigValue("IPhost")+":"+ConfigManager.getInstance().getConfigValue("clientPort");
	
	public RpcClient() {
		rpcUrl = strRpcUrl;
		initConnect(rpcUrl); 
	}
	
	public boolean initConnect(Object objParam) {
		
		initParam = objParam;
		strRpcURL = (String)objParam;		
		if(strRpcURL == null || strRpcURL.length() == 0)
			return false ;
		
		mConfig = new XmlRpcClientConfigImpl();
        try {
        	mConfig.setServerURL(new URL(strRpcURL));
        	mConfig.setEnabledForExtensions(true);  
        	mConfig.setConnectionTimeout(5*60*1000);
        	mConfig.setReplyTimeout(5*60*1000);
        	
        	mClient = new XmlRpcClient();  
        	mClient.setTransportFactory(new XmlRpcCommonsTransportFactory(mClient));	  
        	mClient.setConfig(mConfig);	
        	       	
		} catch (MalformedURLException e) {
			System.err.println("initConnect error :" + strRpcURL);
			//e.printStackTrace();
			bConnSucess = false ; 
			return false ;			
		}
		bConnSucess = true ;
		return true ;
	}	
	
	public Object getInitInfo() {
		return initParam;
	}
	
	public boolean isLive() {
		return bConnSucess;
	}
	
	
	public Object execute(String strMethod,Object[] args){
		
		if(strMethod == null || strMethod.length() == 0)
			return null ;
		
		if(mClient == null)
			return null ;
		
		Object objRet = null ;		
		try {	
			objRet = mClient.execute(strMethod, args);	
		} catch (XmlRpcException e) {	
			return null;
		}		
		return objRet;
	}
	
	
	//-----------------------------调用方法-------------------------------
	
	/**
	 * 获取采集机器人运行状态
	 * @return
	 */
	public String turnHtml(String ip,String Keyword,String intNum){
		//加密
		
		//参数 以数组封装
		String[] objParams = new String[]{ip,Keyword,intNum};
	
		//调用 发布 方法 
		Object obj = execute("RpcInterface.Keyword",objParams);
		
		//判断返回值
		if(obj == null)
			return null;
		
		//返回
		return obj.toString().trim();
	}
	public static void main(String []args)
	{
		RpcClient client = new RpcClient();
		String strTemp = client.turnHtml("127.0.0.1","","10");
		System.out.println(strTemp);
	}
	
}

