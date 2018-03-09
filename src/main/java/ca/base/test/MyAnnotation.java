package ca.base.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * @Target,和@Retention都是元注解，
 * @Target有三种取值，METHOD,TYPE,FILED，分别表示在方法上有效，在类上，在字段上
 * @Retention也是有三种取值，SOURCE,CLASS,RUNTIME,分别对应java源文件，class文件，内存中的字节码。默认是class文件阶段
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnnotation {
	  
	public boolean isId();
	public String name() default "";   //注解中的属性是以方法的形式存在的，但是赋值的时候也是.出来
 
}
