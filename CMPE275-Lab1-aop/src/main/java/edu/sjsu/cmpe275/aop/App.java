package edu.sjsu.cmpe275.aop;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
	public static void main(String[] args) {

    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("context.xml");
        BlogService blogService = (BlogService) ctx.getBean("blogService");

        try {

        	blogService.shareBlog("Alice", "Alice", "Alax");
	        blogService.shareBlog("Bob", "Bob", "Alice");
	        blogService.shareBlog("Alice", "Bob", "Bob");
	        blogService.shareBlog("Bob", "Bob", "Ron");
	        blogService.shareBlog("Alice", "Bob", "Alax");
	        blogService.shareBlog("ron", "ron", "alice");
	        blogService.readBlog("Alice", "Ron");
//	        blogService.readBlog("Jim","alice");
//	        ***************************
	        blogService.commentOnBlog("Alice","bob", null);
//	        blogService.unshareBlog("ron", "alice");
//	        blogService.unshareBlog("Alice", "Ron");
//	        blogService.unshareBlog("Alice", "Bob");

	        //blogService.shareBlog("Alice", "Bob", "Ron");


//	        blogService.readBlog("Bob", "Bob");
            //blogService.commentOnBlog("Bob", "Alice", "Nice work!");
            //blogService.unshareBlog("Alice", "Bob");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ctx.close();
    }
}