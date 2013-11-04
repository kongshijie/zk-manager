package com.dj.zk.manager.utils.json;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ser.StdSerializerProvider;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.codehaus.jackson.map.ser.std.NullSerializer;
import org.codehaus.jackson.type.TypeReference;

/**
 * 
 * @description:jsonUtils 工具类
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:27:16
 */
public class JsonUtils {

	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	private static final Logger log = Logger.getLogger(JsonUtils.class);

	static boolean isPretty = false;

	private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	static StdSerializerProvider sp = new StdSerializerProvider();
	static {
		sp.setNullValueSerializer(NullSerializer.instance);
	}

	public static boolean isPretty() {
		return isPretty;
	}

	public static void setPretty(boolean isPretty) {
		JsonUtils.isPretty = isPretty;
	}

	/**
	 * JSON串转换为Java泛型对象，可以是各种类型，此方法最为强大。用法看测试用例。
	 * 
	 * @param <T>
	 * @param jsonString
	 *            JSON字符串
	 * @param tr
	 *            TypeReference,例如: new TypeReference< List<FamousUser> >(){}
	 * @return List对象列表
	 */
	@SuppressWarnings("unchecked")
	public static <T> T json2GenericObject(String jsonString,TypeReference<T> tr, String dateFormat) {
		if (StringUtils.isNotEmpty(jsonString)) {
			try {
				ObjectMapper objectMapper = new ObjectMapper(null, sp, null);
				objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES); 

				if (StringUtils.isEmpty(dateFormat)) {
					objectMapper.setDateFormat(new SimpleDateFormat(
							DEFAULT_DATE_FORMAT));
				} else {
					objectMapper
							.setDateFormat(new SimpleDateFormat(dateFormat));
				}
				return (T) objectMapper.readValue(jsonString, tr);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage(), e);
			}
		}
		return null;
	}
	
	/**
	 * Json字符串转Java对象
	 * 
	 * @param jsonString
	 * @param c
	 * @return
	 */
	public static <T> T json2Object(String jsonString, Class<T> c,String dateFormat) {
		if (StringUtils.isNotEmpty(jsonString)) {
			try {
				ObjectMapper objectMapper = new ObjectMapper(null, sp, null);
				objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES); 
				if (StringUtils.isEmpty(dateFormat)) {
					objectMapper.setDateFormat(new SimpleDateFormat(
							DEFAULT_DATE_FORMAT));
				} else {
					objectMapper
							.setDateFormat(new SimpleDateFormat(dateFormat));
				}
				return (T)objectMapper.readValue(jsonString, c);
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e.getMessage(), e);
			}
		}
		return null;
	}
	

	/**
	 * Java对象转Json字符串
	 * 
	 * @param object
	 *            目标对象
	 * @param executeFields
	 *            排除字段
	 * @param includeFields
	 *            包含字段
	 * @param dateFormat
	 *            时间格式化
	 * @param isPretty
	 *            是否格式化打印 default false
	 * @return
	 */
	public static String toJson(Object object, String[] executeFields,
			String[] includeFields, String dateFormat) {
		String jsonString = "";
		try {
			BidBeanSerializerFactory bidBeanFactory = BidBeanSerializerFactory.instance;
			ObjectMapper mapper = new ObjectMapper(null, sp, null);
			if (StringUtils.isEmpty(dateFormat)) {
				mapper.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_FORMAT));
			} else {
				mapper.setDateFormat(new SimpleDateFormat(dateFormat));
			}
			if (includeFields != null) {
				String filterId = "includeFilter";
				mapper.setFilters(new SimpleFilterProvider().addFilter(
						filterId, SimpleBeanPropertyFilter
								.filterOutAllExcept(includeFields)));
				bidBeanFactory.setFilterId(filterId);
				mapper.setSerializerFactory(bidBeanFactory);

			} else if (includeFields == null && executeFields != null) {
				String filterId = "executeFilter";
				mapper.setFilters(new SimpleFilterProvider().addFilter(
						filterId, SimpleBeanPropertyFilter
								.serializeAllExcept(executeFields)));
				bidBeanFactory.setFilterId(filterId);
				mapper.setSerializerFactory(bidBeanFactory);
			}
			if (isPretty) {
				jsonString = mapper.writerWithDefaultPrettyPrinter()
						.writeValueAsString(object);
			} else {
				jsonString = mapper.writeValueAsString(object);
			}
		} catch (Exception e) {
			log.warn(e.getMessage(), e);
		} finally {
		}
		return jsonString;
	}

	

	public static void main(String[] args) {
//		App a = new App();
//		a.setAppIp("123123");
//		a.setAppName("中华人民共和国");
//		Person person = new Person();
//		person.setDifTime(5000);
//		Map<String, Object> dataMap = new HashMap<String, Object>();
//		dataMap.put(Person.class.getName(), person);
//		a.setDataMap(dataMap);
//		a.setCreateTime(new Date());
//		String aa = JsonUtils.toJson(a, new String[] { "id"},null, "yyyy-MM-dd");
//		System.out.println(aa);
//		App app = JsonUtils.json2Object(aa, App.class, "yyyy-MM-dd");
//        System.out.println(ToStringBuilder.reflectionToString(app));
 
	}

}
