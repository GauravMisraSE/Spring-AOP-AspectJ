package edu.sjsu.cmpe275.aop.aspect;
import edu.sjsu.cmpe275.aop.Blog;
import edu.sjsu.cmpe275.aop.exceptions.AccessDeniedExeption;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import java.util.*;

// Aspect order: 1 - Validation, 2 - Authorization, 3 - Retry

@Order(2)
@Aspect
public class AuthorizationAspect {

	@Pointcut("execution(public void edu.sjsu.cmpe275.aop.BlogService.shareBlog(..))")
	public void sharePointcut() {}

	@Pointcut("execution(public void edu.sjsu.cmpe275.aop.BlogService.unshareBlog(..))")
	public void unsharePointcut() {}

	@Pointcut("execution(public * edu.sjsu.cmpe275.aop.BlogService.readBlog(..))")
	public void readPointcut(){}

	@Pointcut("execution(public void edu.sjsu.cmpe275.aop.BlogService.commentOnBlog(..))")
	public void commentPointcut(){}

	// usermap contains userblog sharing relationships.
	static Map<String, HashSet<String>> usermap = new HashMap<String, HashSet<String>>();

	public boolean checkEntry(String name) {

		if (usermap.containsKey(name)) return true;
		else return false;
	}

	public void makeEntry(String name) {

		usermap.put(name, new HashSet<String>());
		return;
	}

	public void addToTargetUser(String blogUserId, String targetUserId) {

		if (usermap.containsKey(targetUserId)) {
			usermap.get(targetUserId).add(blogUserId);
			return;
		}
		//add target user if not found
		else {
			HashSet<String> initial = new HashSet<String>();
			initial.add(blogUserId);
			usermap.put(targetUserId, initial);
			return;
		}
	}

	@Before("sharePointcut()")
	public void validateShare(JoinPoint joinPoint) throws AccessDeniedExeption {

		Object[] args = joinPoint.getArgs();
		String userId = (String) args[0];
		String blogUserId = (String) args[1];
		String targetUserId = (String) args[2];
		// convert all user names to lower case
		userId = userId.toLowerCase();
		blogUserId = blogUserId.toLowerCase();
		targetUserId = targetUserId.toLowerCase();

		if (checkEntry(userId)) {
			//user is already found in the map, retrieve blogs which are shared with him
			HashSet<String> availableBlogs = usermap.get(userId);
			if (availableBlogs.contains(blogUserId) || userId.equalsIgnoreCase(blogUserId)) {
				//check if target user is present in the map or not, handle accordingly
				addToTargetUser(blogUserId, targetUserId);
//				try {
//					joinPoint.proceed();
//				} catch (Throwable throwable) {
//
//				}
			} else {
				throw new AccessDeniedExeption(userId + " DOES NOT HAVE ACCESS TO SHARE " + blogUserId);
			}
		} else {
			if (userId.equalsIgnoreCase(blogUserId)) {

				if (!checkEntry(userId)) {
					makeEntry(userId);
				}

				if (!checkEntry(targetUserId)) {
					makeEntry(targetUserId);
				}

				usermap.get(targetUserId).add(userId);
//				try {
//					joinPoint.proceed();
//				} catch (Throwable throwable) {
//
//				}
			} else {
				//a new user is trying to share someone else's blog to third party
				throw new AccessDeniedExeption("A NEW USER CANNOT SHARE THIRD PARTY'S BLOG");
			}
		}

		System.out.println(usermap);
	}

	@Before("unsharePointcut()")
	public void validateUnshare(JoinPoint joinPoint) throws AccessDeniedExeption {

		Object[] args = joinPoint.getArgs();
		String userId = (String) args[0];
		String targetUserId = (String) args[1];
		// convert all user names to lower case
		userId = userId.toLowerCase();
		targetUserId = targetUserId.toLowerCase();

		if (checkEntry(userId) && checkEntry(targetUserId)) {

			//check for same userId
			if (userId.equalsIgnoreCase(targetUserId))
				return;
				//remove from the set if found
			if (usermap.get(targetUserId).contains(userId)) {
				usermap.get(targetUserId).remove(userId);
//				try {
//					joinPoint.proceed();
//				} catch (Throwable throwable) {
//				}
			}
			else throw new AccessDeniedExeption(userId +"'s blog is not shared with " + targetUserId);
		}

		else
			throw new AccessDeniedExeption("Invalid inputs, not found in the relationship map!");

		System.out.println(usermap);
	}

	@Before("readPointcut()")
	public Blog validateRead(JoinPoint joinPoint) throws AccessDeniedExeption {

		Blog blog;
		Object[] args = joinPoint.getArgs();
		String userId = (String) args[0];
		String blogUserId = (String) args[1];
		// convert all user names to lower case
		userId = userId.toLowerCase();
		blogUserId = blogUserId.toLowerCase();
		// if user tries to read his own blog
		if (userId.equalsIgnoreCase(blogUserId)) {
			//if a completely new user
			if (!checkEntry(userId)) {
				makeEntry(userId);
				System.out.println(usermap);
			}

//			try {
//				blog = (Blog) joinPoint.proceed();
//				return blog;
//			}
//			catch (Throwable throwable) {
//				System.out.println(throwable);
//			}
		}
		// check if user is already present
		else if (checkEntry(userId)) {

			HashSet<String> availableBlogs = usermap.get(userId);
			if (availableBlogs.contains(blogUserId)) {

//				try {
//					blog = (Blog) joinPoint.proceed();
//					return blog;
//				}
//				catch (Throwable throwable) {
//					//if (throwable.getClass().getName().equals("edu.sjsu.cmpe275.aop.exceptions.NetworkException"){
//						//throw new NetworkException("Unable to proceed even after 3 retries");
//					//}
//				}
			}
			else {
				throw new AccessDeniedExeption(userId + " does not have access to " + blogUserId);
			}
		}
		else {
			throw new AccessDeniedExeption(userId + " not present in blog space.");
		}
	return null;
	}

	@Before("commentPointcut()")
	public void validateComment(JoinPoint joinPoint) throws AccessDeniedExeption{

		Object[] args = joinPoint.getArgs();
		String userId = (String) args[0];
		String blogUserId = (String) args[1];

		// convert all user names to lower case
		userId = userId.toLowerCase();
		blogUserId = blogUserId.toLowerCase();

		// if user tries to comment on his own blog
		if (userId.equalsIgnoreCase(blogUserId)) {
			//if a completely new user
			if (!checkEntry(userId)) {
				makeEntry(userId);
				System.out.println(usermap);
			}
//			try {
//				joinPoint.proceed();
//			}
//			catch (Throwable throwable) {
//			}
		}
		// check if user is already present
		else if (checkEntry(userId)) {

			HashSet<String> availableBlogs = usermap.get(userId);
			if (availableBlogs.contains(blogUserId)) {

//				try {
//						joinPoint.proceed();
//				}
//				catch (Throwable throwable) {
//				}
			}
			else
				throw new AccessDeniedExeption(userId + " does not have access to comment on " + blogUserId);
		}
		else {
			throw new AccessDeniedExeption(userId + " not present in blog space.");
		}
	}
}






