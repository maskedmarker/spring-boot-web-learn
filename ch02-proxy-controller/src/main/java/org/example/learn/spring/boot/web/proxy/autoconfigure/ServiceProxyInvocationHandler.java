package org.example.learn.spring.boot.web.proxy.autoconfigure;

import org.springframework.aop.support.AopUtils;
import org.springframework.cglib.proxy.InvocationHandler;

import java.lang.reflect.Method;

public class ServiceProxyInvocationHandler implements InvocationHandler {

	private Object target;

	public ServiceProxyInvocationHandler(Object target) {
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (method.getName().equals("toString")) {
			return AopUtils.getTargetClass(proxy).toString();
		}
		if (method.getName().equals("equals")) {
			return method.invoke(AopUtils.getTargetClass(proxy), args);
		}
		if (method.getName().equals("hashCode")) {
			return AopUtils.getTargetClass(proxy).hashCode();
		}
		return method.invoke(target,args);
	}
}
