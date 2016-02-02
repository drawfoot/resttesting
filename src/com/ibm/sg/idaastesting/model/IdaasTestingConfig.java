package com.ibm.sg.idaastesting.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public class IdaasTestingConfig {
	public static final String CFG_HOST = "host";	
	public static final String CFG_HOSTMODE = "hostmode";
	public static final String CFG_TENANT1 = "tenant1";
	public static final String CFG_TENANT2 = "tenant2";	
	public static final String CFG_APPID = "appid";

	private static IdaasTestingConfig instance = null;
	private Properties prop = new Properties();
	private final String CONFIG_FILE = new File(
			System.getProperty("user.dir"), "idaastesting.properties")
			.toString();
	
	public static IdaasTestingConfig getInstance() throws IOException {
		if(instance == null)
			return (instance = new IdaasTestingConfig());
		return instance;
	}

	private IdaasTestingConfig() throws IOException {
		if (new File(CONFIG_FILE).isFile()) {
			FileInputStream in = new FileInputStream(CONFIG_FILE);
			prop.load(in);
		}
	}

	public Properties getProp() {
		return prop;
	}

	public void setProp(Properties prop) {
		this.prop = prop;
	}
	
	public void save() throws IOException {
		OutputStream output = new FileOutputStream(CONFIG_FILE);
		prop.store(output, null);		
	}

}
