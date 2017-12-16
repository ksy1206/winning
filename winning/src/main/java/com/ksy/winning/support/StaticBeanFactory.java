package com.ksy.winning.support;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.stereotype.Component;

@Component
public class StaticBeanFactory implements BeanFactoryAware {
	private static BeanFactory beanFactory;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		StaticBeanFactory.beanFactory = beanFactory;
	}

	public static Object getBean(String name) {
		return beanFactory.getBean(name);
	}

	public static <T> T getBean(Class<T> type) {
		return beanFactory.getBean(type);
	}

	public static <T> T getBean(String name, Class<T> type) {
		return beanFactory.getBean(name, type);
	}
	
	public static <T> Map<String, T> getBeans(Class<T> type) {
		return ((ListableBeanFactory)beanFactory).getBeansOfType(type);
	}
}
