package edu.sjsu.cmpe275.aop;

import edu.sjsu.cmpe275.aop.exceptions.AccessDeniedExeption;
import edu.sjsu.cmpe275.aop.exceptions.NetworkException;

public class BlogServiceImpl implements BlogService{

	public Blog readBlog(String userId, String blogUserId) throws AccessDeniedExeption, NetworkException {
		//throw new NetworkException("NE thrown");
		//System.out.println(userId + " successfully read the blog of " + blogUserId);
		return null;
	}

	public void shareBlog(String userId, String blogUserId, String targetUserId)
			throws AccessDeniedExeption, NetworkException
	{
		//System.out.println(userId + " successfully shared " + blogUserId + " with " + targetUserId);
		//throw new NetworkException("exception thrown");
	}

	public void unshareBlog(String userId, String targetUserId) throws AccessDeniedExeption, NetworkException {
		throw new NetworkException("exception thrown");
		// System.out.println(userId + " successfully unshared with " + targetUserId);
	}

	public void commentOnBlog(String userId, String blogUserId, String message)
			throws AccessDeniedExeption, IllegalArgumentException, NetworkException {
		throw new NetworkException("exception thrown");
		//System.out.println(userId + " successfully commented on blog of " + blogUserId);
	}

}
