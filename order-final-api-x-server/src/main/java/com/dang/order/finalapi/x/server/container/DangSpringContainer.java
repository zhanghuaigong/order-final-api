package com.dang.order.finalapi.x.server.container;

/**   
* @author zhangjing@dangdang.com
* @date 2014-3-28 下午1:55:02 
*/

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.container.Container;
import com.alibaba.dubbo.container.spring.SpringContainer;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @date 2014-3-28 下午1:55:02
 * 
 */
public class DangSpringContainer implements Container {

	private static final Logger logger = LoggerFactory.getLogger(SpringContainer.class);

	public static final String SPRING_CONFIG = "dubbo.spring.config";

	public static final String DEFAULT_SPRING_CONFIG = "classpath*:META-INF/spring/*.xml";

	static FileSystemXmlApplicationContext context;

	public static FileSystemXmlApplicationContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alibaba.dubbo.container.Container#start()
	 */
	@Override
	public void start() {
		context = getSpringContext();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.alibaba.dubbo.container.Container#stop()
	 */
	@Override
	public void stop() {
		try {
			if (context != null) {
				context.stop();
				context.close();
				context = null;
			}
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 获取spring会话
	 * 
	 * @return
	 */
	public static synchronized FileSystemXmlApplicationContext getSpringContext() {
		Path path = Paths.get("conf", "spring.xml");
		return new FileSystemXmlApplicationContext(path.toString());
	}

}
