package org.nfcu.sre.clf.core;

import java.io.*;
import java.util.Properties;

import org.nfcu.sre.clf.slf4j.SRELogger;
import org.nfcu.sre.clf.slf4j.SRELoggerFactory;

public class SRELoggingConfig{

	private static final SRELogger LOG = SRELoggerFactory.getLogger(SRELoggingConfig.class);

	private static SRELoggingConfig sreLoggingConfig =null;
	
	private SRELoggingConfig() {
	}
	
	public static SRELoggingConfig Singleton() {
		if(sreLoggingConfig == null) {
			sreLoggingConfig = new SRELoggingConfig();
		}
		return sreLoggingConfig;
	}
	
    public Properties loadJVMProps() {
    	try {
    		ClassLoader loader = SRELoggingConfig.class.getClassLoader();
    		return loadProperties(loader, "sre-logging.properties");
    	}catch(Exception e) {
    		LOG.warn("Unable to read the sre-logging.properties file", e.getMessage());
    	}
        return null;
    }
		
	protected Properties loadProperties(ClassLoader classLoader, String propertiesFileName) throws IOException {
		Properties properties = new Properties(); 
		InputStream propertyStream = null; 
		
		try {
			propertyStream = classLoader.getResourceAsStream(propertiesFileName);
			
			if(propertyStream == null) { 
				File propertyFile = new File(propertiesFileName);
				if (propertyFile.exists()) { 
					propertyStream = new FileInputStream(propertyFile); 
				}
			}
			if (propertyStream == null) { 
				throw new FileNotFoundException (
						"Unable to find property file in classpath, or file system: '"+ propertiesFileName + "'");
			}
			properties.load(propertyStream); 
			return properties; 
		} finally {
			if (propertyStream != null) { 
				propertyStream.close();
				}
			}
		}
	}

