package com.dj.zk.manager.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 
 * @description:configure 工具类
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:29:10
 */
public class ZooConfigUtils {
	
	private static Logger logger = Logger.getLogger(ZooConfigUtils.class);
    private static volatile Properties properties;
  
    static {
    	reload();
    }
    
    public static Properties getProperties() {
    	return properties;
    }
    
    public static String get(String key) {
    	return properties.getProperty(key);
    }
    
    public static String get(String key,String defaultValue) {
    	return properties.getProperty(key,defaultValue);
    }
    
    
    public static void reload() {
		String path = ZooConfigUtils.class.getClassLoader().getResource(".").getFile();
    	Properties proot = initClassPath(path);
    	path = ZooConfigUtils.class.getResource("/").getFile();
    	Properties childProperties = initClassPath(path);
    	proot.putAll(childProperties);
    	properties = proot;
    }
    
  

	/**
	 * 加载系统必须的properties
	 * @throws
	 **/
	private static Properties initClassPath(String basePath) {
		Properties allProp = new Properties();
		try {
			//【1】获取系统配置文件信息
			Properties systemProperties = new Properties();
			systemProperties.putAll(System.getProperties());
			//【2】获取jar 包  路径 中配置文件信息
			List<Properties> listJarProp = new ArrayList<Properties>();
	
 			File jarPath = new File(basePath);
			jarPath = jarPath.getParentFile();
			List<String> listFileNames = getAllJarProperties(jarPath);
			for (String string : listFileNames) {
				InputStream in = ZooConfigUtils.class.getClassLoader().getResourceAsStream(string);
				if (in != null) {
 					Properties p = new Properties();
					p.load(in);
					listJarProp.add(p);
				}
			}
			
			//【3】加载 classpath配置文件
			List<Properties> listClassProp = new ArrayList<Properties>();
			List<String> listClassNames = getAllClassPathProperties(jarPath);
			for (String string : listClassNames) {
				InputStream in = new FileInputStream(new File(string));
 				if (in != null) {
 					Properties p = new Properties();
					p.load(in);
					listClassProp.add(p);
				}
			}
 
			// 将已经加载的Properties合并
			List<Properties> propAll = new ArrayList<Properties>();
			propAll.add(systemProperties);
			propAll.addAll(listJarProp);
			propAll.addAll(listClassProp);

			for (Properties properties : propAll) {
				allProp.putAll(properties);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}  
		return allProp;
	}
	
	  
		/**
		 * String regex = "*.+\\.properties";
		 * @description
		 * @param PropertiesHolder设定文件
		 * @return List<Properties> DOM对象
		 * @throws
		 */
		private static List<String> getSpecifiedFileFromJar(File file,String regex) {
			List<String> lists = new ArrayList<String>();
			try {
				if (StringUtils.isEmpty(regex)) {
					return lists;
				}
				/**
				 * 如果不以jar结尾
				 */
				if(!file.getName().endsWith("jar")) {
					lists.add(file.getAbsolutePath());
					return lists;
				}
				
				Pattern p = Pattern.compile(regex);
				JarFile jarFile = new JarFile(file.getAbsolutePath());
				Enumeration<JarEntry> entries = jarFile.entries();
				while (entries.hasMoreElements()) {
					JarEntry entry = entries.nextElement();
					String name = entry.getName();
					Matcher m = p.matcher(name);
					if (m.matches()) {
						lists.add(name);
					}
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
			return lists;
		}
		
		/**
		 * 
		 * @description 取得所有 jar包中的properties
		 * @param    PropertiesHolder设定文件
		 * @return  List<String> DOM对象
		 * @throws
		 */
		private static List<String> getAllJarProperties(File parentFile) {
			List<String> lists = new ArrayList<String>();
			List<File> listJars = (List<File>) FileUtils.listFiles(parentFile,
					new String[] { "jar"}, true);
			for (File file : listJars) {
	 			lists.addAll(getSpecifiedFileFromJar(file, ".+\\.properties"));
			}
			return lists;
		}
		
		/**
		 * 
		 * @description 取得所有 jar包中的properties
		 * @param    PropertiesHolder设定文件
		 * @return  List<String> DOM对象
		 * @throws
		 */
		private static List<String> getAllClassPathProperties(File parentFile) {
			List<String> lists = new ArrayList<String>();
			List<File> listProps = (List<File>) FileUtils.listFiles(parentFile,
					new String[] { "properties"}, true);
			for (File file : listProps) {
	 			lists.addAll(getSpecifiedFileFromJar(file, ".+\\.properties"));
			}
			return lists;
		}
		
 
}
