package edu.sjsu.cmpe275.aop.aspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;  // if needed
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

// Aspect order: 1 - Validation, 2 - Authorization, 3 - Retry
@Order(1)
@Aspect
public class ValidationAspect {

	@Pointcut("execution(public * edu.sjsu.cmpe275.aop.BlogService.*(..))")
	public void argsPointcut() {
	}

	@Pointcut("execution(public void edu.sjsu.cmpe275.aop.BlogService.shareBlog(..))")
	public void sharePointcut() {
	}

	@Pointcut("execution(public void edu.sjsu.cmpe275.aop.BlogService.commentOnBlog(..))")
	public void commentPointcut() {
	}

	@Before("argsPointcut()")
	public void validateArgsLength(JoinPoint joinPoint) throws IllegalArgumentException {

		Object[] args = joinPoint.getArgs();
		String userId = (String) args[0];
		String blogUserId = (String) args[1];
		if (!((userId.length() >= 3 && userId.length() <= 16) && (blogUserId.length() >= 3 && blogUserId.length() <= 16)))
			throw new IllegalArgumentException("Length of required arguments is out of bounds");
	}

	@Before("sharePointcut()")
	public void validateShareArgs(JoinPoint joinPoint) throws IllegalArgumentException {

		Object[] args = joinPoint.getArgs();
		String targetUserId = (String) args[2];
		if (!((targetUserId.length() >= 3) && (targetUserId.length() <= 16)))
			throw new IllegalArgumentException("Length of target id is incorrect");
	}

	@Before("commentPointcut()")
	public void validateCommentArgs(JoinPoint joinPoint) throws IllegalArgumentException {

		Object[] args = joinPoint.getArgs();
		if (args[2] != null) {
			String msg = (String) args[2];
			if (msg.length() > 100 || msg.equals("")) {
				throw new IllegalArgumentException("length of msg is out of bounds");
			}

		} else
			throw new IllegalArgumentException("message cannot be null");
	}
}