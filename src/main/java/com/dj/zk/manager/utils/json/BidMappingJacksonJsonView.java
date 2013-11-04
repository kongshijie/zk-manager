package com.dj.zk.manager.utils.json;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerFactory;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.AbstractView;


/**
 * 
 * @description:jacksonJson 数据转换
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:27:09
 */
public class BidMappingJacksonJsonView extends AbstractView {

	/**
	 * Default content type. Overridable as bean property.
	 */
	public static final String DEFAULT_CONTENT_TYPE = "application/json";

	private ObjectMapper objectMapper = new ObjectMapper();

	private JsonEncoding encoding = JsonEncoding.UTF8;

	private boolean prefixJson = false;

	private Set<String> modelKeys;

	private boolean extractValueFromSingleKeyModel = false;

	private boolean disableCaching = true;

	/**
	 * 日期格式化 KEY
	 */
	public static String DATE_FORMAT = "DATE_FORMAT";

	/**
	 * 排除字段
	 */
	public static String EXECUTE_FIELDS = "EXECUTE_FIELDS";

	/**
	 * 包含字段
	 */
	public static String INCLUDE_FIELDS = "INCLUDE_FIELDS";

	/**
	 * 排除字段
	 */
	private String[] executeFields;

	/**
	 * 包含字段
	 */
	private String[] includeFields;

	/**
	 * Construct a new {@code JacksonJsonView}, setting the content type to
	 * {@code application/json}.
	 */
	public BidMappingJacksonJsonView() {
		setContentType(DEFAULT_CONTENT_TYPE);
		setExposePathVariables(false);
	}

	/**
	 * Sets the {@code ObjectMapper} for this view. If not set, a default
	 * {@link ObjectMapper#ObjectMapper() ObjectMapper} is used.
	 * <p>
	 * Setting a custom-configured {@code ObjectMapper} is one way to take
	 * further control of the JSON serialization process. For example, an
	 * extended {@link SerializerFactory} can be configured that provides custom
	 * serializers for specific types. The other option for refining the
	 * serialization process is to use Jackson's provided annotations on the
	 * types to be serialized, in which case a custom-configured ObjectMapper is
	 * unnecessary.
	 */
	public void setObjectMapper(ObjectMapper objectMapper) {
		Assert.notNull(objectMapper, "'objectMapper' must not be null");
		this.objectMapper = objectMapper;
	}

	/**
	 * Set the {@code JsonEncoding} for this converter. By default,
	 * {@linkplain JsonEncoding#UTF8 UTF-8} is used.
	 */
	public void setEncoding(JsonEncoding encoding) {
		Assert.notNull(encoding, "'encoding' must not be null");
		this.encoding = encoding;
	}

	/**
	 * Indicates whether the JSON output by this view should be prefixed with
	 * <tt>"{} && "</tt>. Default is false.
	 * <p>
	 * Prefixing the JSON string in this manner is used to help prevent JSON
	 * Hijacking. The prefix renders the string syntactically invalid as a
	 * script so that it cannot be hijacked. This prefix does not affect the
	 * evaluation of JSON, but if JSON validation is performed on the string,
	 * the prefix would need to be ignored.
	 */
	public void setPrefixJson(boolean prefixJson) {
		this.prefixJson = prefixJson;
	}

	/**
	 * Set the attribute in the model that should be rendered by this view. When
	 * set, all other model attributes will be ignored.
	 */
	public void setModelKey(String modelKey) {
		this.modelKeys = Collections.singleton(modelKey);
	}

	/**
	 * Set the attributes in the model that should be rendered by this view.
	 * When set, all other model attributes will be ignored.
	 */
	public void setModelKeys(Set<String> modelKeys) {
		this.modelKeys = modelKeys;
	}

	/**
	 * Return the attributes in the model that should be rendered by this view.
	 */
	public Set<String> getModelKeys() {
		return this.modelKeys;
	}

	/**
	 * Set the attributes in the model that should be rendered by this view.
	 * When set, all other model attributes will be ignored.
	 * 
	 * @deprecated use {@link #setModelKeys(Set)} instead
	 */
	@Deprecated
	public void setRenderedAttributes(Set<String> renderedAttributes) {
		this.modelKeys = renderedAttributes;
	}

	/**
	 * Return the attributes in the model that should be rendered by this view.
	 * 
	 * @deprecated use {@link #getModelKeys()} instead
	 */
	@Deprecated
	public Set<String> getRenderedAttributes() {
		return this.modelKeys;
	}

	/**
	 * Set whether to serialize models containing a single attribute as a map or
	 * whether to extract the single value from the model and serialize it
	 * directly.
	 * <p>
	 * The effect of setting this flag is similar to using
	 * {@code MappingJacksonHttpMessageConverter} with an {@code @ResponseBody}
	 * request-handling method.
	 * <p>
	 * Default is {@code false}.
	 */
	public void setExtractValueFromSingleKeyModel(boolean extractValueFromSingleKeyModel) {
		this.extractValueFromSingleKeyModel = extractValueFromSingleKeyModel;
	}

	/**
	 * Disables caching of the generated JSON.
	 * <p>
	 * Default is {@code true}, which will prevent the client from caching the
	 * generated JSON.
	 */
	public void setDisableCaching(boolean disableCaching) {
		this.disableCaching = disableCaching;
	}

	@Override
	protected void prepareResponse(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType(getContentType());
		response.setCharacterEncoding(this.encoding.getJavaName());
		if (this.disableCaching) {
			response.addHeader("Pragma", "no-cache");
			response.addHeader("Cache-Control", "no-cache, no-store, max-age=0");
			response.addDateHeader("Expires", 1L);
		}
	}

	@Override
	protected synchronized void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Object value = filterModel(model);
		JsonGenerator generator = this.objectMapper.getJsonFactory().createJsonGenerator(response.getOutputStream(),
				this.encoding);
		if (this.prefixJson) {
			generator.writeRaw("{} && ");
		}
		
		
		this.objectMapper.writeValue(generator, value);
		 if(model.containsKey(INCLUDE_FIELDS) || model.containsKey(EXECUTE_FIELDS)) {
			 BidBeanSerializerFactory bidBeanFactory = BidBeanSerializerFactory.instance;
			 bidBeanFactory.setFilterId(null);
	    	 this.objectMapper.setSerializerFactory(bidBeanFactory);
	    }
		 
		 if(model.containsKey(DATE_FORMAT)) {
			 this.objectMapper.setDateFormat(null);
	    }
	}

	/**
	 * Filters out undesired attributes from the given model. The return value
	 * can be either another {@link Map} or a single value object.
	 * <p>
	 * The default implementation removes {@link BindingResult} instances and
	 * entries not included in the {@link #setRenderedAttributes
	 * renderedAttributes} property.
	 * 
	 * @param model
	 *            the model, as passed on to {@link #renderMergedOutputModel}
	 * @return the object to be rendered
	 */
	protected Object filterModel(Map<String, Object> model) {
		BidBeanSerializerFactory bidBeanFactory = BidBeanSerializerFactory.instance;
        if(model.containsKey(INCLUDE_FIELDS) || model.containsKey(EXECUTE_FIELDS)) {
    		this.objectMapper = new ObjectMapper();
        }
		Map<String, Object> result = new HashMap<String, Object>(model.size());
		Set<String> renderedAttributes = (!CollectionUtils.isEmpty(this.modelKeys) ? this.modelKeys : model.keySet());
		for (Map.Entry<String, Object> entry : model.entrySet()) {
			if (!(entry.getValue() instanceof BindingResult) && renderedAttributes.contains(entry.getKey())) {
				// 增加日期格式化 liudejian
				if (DATE_FORMAT.equals(entry.getKey())) {
					this.objectMapper.setDateFormat(new SimpleDateFormat(String.valueOf(entry.getValue())));
					continue;
				}
				// 增加排除字段 liudejian
				if (!model.containsKey(INCLUDE_FIELDS) && EXECUTE_FIELDS.equals(entry.getKey())) {
					if (entry.getValue() != null && entry.getValue() instanceof String[]) {
						executeFields = (String[]) entry.getValue();
						String filterId = "executeFilter";
						objectMapper.setFilters(new SimpleFilterProvider().addFilter(filterId,
								SimpleBeanPropertyFilter.serializeAllExcept(executeFields)));
						bidBeanFactory.setFilterId(filterId);
						objectMapper.setSerializerFactory(bidBeanFactory);
						continue;
					}
				}
				// 增加仅包含字段 liudejian
				if (INCLUDE_FIELDS.equals(entry.getKey())) {
					if (entry.getValue() != null && entry.getValue() instanceof String[]) {
 						includeFields = (String[]) entry.getValue();
						String filterId = "includeFilter";
						objectMapper.setFilters(new SimpleFilterProvider().addFilter(filterId,
								SimpleBeanPropertyFilter.filterOutAllExcept(includeFields)));
						bidBeanFactory.setFilterId(filterId);
						objectMapper.setSerializerFactory(bidBeanFactory);
						continue;
					}
				}
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return (this.extractValueFromSingleKeyModel && result.size() == 1 ? result.values().iterator().next() : result);
	}

	public String[] getExecuteFields() {
		return executeFields;
	}

	public void setExecuteFields(String[] executeFields) {
		this.executeFields = executeFields;
	}

	public String[] getIncludeFields() {
		return includeFields;
	}

	public void setIncludeFields(String[] includeFields) {
		this.includeFields = includeFields;
	}

}
