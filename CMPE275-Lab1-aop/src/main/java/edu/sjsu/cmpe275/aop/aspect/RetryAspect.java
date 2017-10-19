package edu.sjsu.cmpe275.aop.aspect;
import edu.sjsu.cmpe275.aop.Blog;
import edu.sjsu.cmpe275.aop.exceptions.NetworkException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

// Aspect order: 1 - Validation, 2 - Authorization, 3 - Retry
@Order(3)
@Aspect
public class RetryAspect {

	// catches all the methods except the read method
	@Pointcut("execution(public void edu.sjsu.cmpe275.aop.BlogService.*(..))")
	public void retryPointcut(){}

	// for read method as it has to return a blog Object
	@Pointcut("execution(public * edu.sjsu.cmpe275.aop.BlogService.readBlog(..))")
	public void retryReadPointcut(){}

	@Around("retryPointcut()")
	public void retry(ProceedingJoinPoint joinPoint) throws NetworkException{

		// counting the no. of retries
		int counter = 0;

		while (counter < 3){

			try {
				joinPoint.proceed();
			}
			catch (Throwable throwable){
				// check if it is a network exception
				if (throwable.getClass().getName().equals("edu.sjsu.cmpe275.aop.exceptions.NetworkException")) {
					counter = counter + 1;
					continue;
					}
				else
					// any other exception is not given a retry
					break;
			}
			break;
		}
		//System.out.println("counter = " + counter);
		if (counter == 3)
			throw new NetworkException("Unable to proceed even after 3 retries");
	}

	@Around("retryReadPointcut()")
	public Blog retryRead(ProceedingJoinPoint joinPoint) throws NetworkException{

		int counter = 0;
		Blog blog = null;

		while (counter < 3){

			try {
				blog = (Blog)joinPoint.proceed();
			}
			catch (Throwable throwable){
				if (throwable.getClass().getName().equals("edu.sjsu.cmpe275.aop.exceptions.NetworkException")) {
					counter = counter + 1;
					continue;
				}
				else
					break;
			}
			break;
		}
		//System.out.println("counter = " + counter);
		if (counter == 3)
			throw new NetworkException("Unable to proceed even after 3 retries");
		return blog;
	}
}