package org.example.learn.spring.boot.web.proxy.autoconfigure;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(ExportServiceRegistrar.class)
public @interface EnableExportService {

	String[] value() default {};

	String[] basePackages() default {};

	Class<?>[] basePackageClasses() default {};

	Class<?>[] clients() default {};

	Class<?> superClass() default Void.class;
}
