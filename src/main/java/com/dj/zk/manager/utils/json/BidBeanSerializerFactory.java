package com.dj.zk.manager.utils.json;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.introspect.BasicBeanDescription;
import org.codehaus.jackson.map.ser.BeanSerializerFactory;

/**
 * 
 * @description:重写jackson 工厂类，增加过滤与仅包含
 * @version  Ver 1.0
 * @author   <a href="mailto:zuiwoxing@gmail.com">dejian.liu</a>
 * @Date	 2013-11-3 下午6:26:59
 */
public class BidBeanSerializerFactory extends BeanSerializerFactory {

    public final static BidBeanSerializerFactory instance = new BidBeanSerializerFactory(null);
    
    private Object filterId;
    
	protected BidBeanSerializerFactory(Config config) {
		super(config);
	}
	
    @Override
    protected synchronized Object findFilterId(SerializationConfig config,
    		BasicBeanDescription beanDesc) {
    	return getFilterId();
    }

	public Object getFilterId() {
		return filterId;
	}

	public void setFilterId(Object filterId) {
		this.filterId = filterId;
	}
}
