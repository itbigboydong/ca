package ca.base.test;

@MyAnnotation(isId=true,name="Moke")
public class AnnotationTest {
    
	public static void main(String[] args) {
		//判断AnnotationTest类上是否有@MyAnnotation这个注解
		if(AnnotationTest.class.isAnnotationPresent(MyAnnotation.class)){
			MyAnnotation myanno= AnnotationTest.class.getAnnotation(MyAnnotation.class);
			System.out.println(myanno.name());
		}
	}
}
