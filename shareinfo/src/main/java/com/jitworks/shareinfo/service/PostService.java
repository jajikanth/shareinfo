
package com.jitworks.shareinfo.service;

import java.util.List;

import com.jitworks.shareinfo.data.Post;
import com.jitworks.shareinfo.data.PostComment;


public interface PostService {

	public void createPost(Post post);

	public List<Post> getPosts(int userId);

	public void deletePost(Integer postId);

	public Post getPost(int postId);

	public void createPostComment(PostComment postComment);

}
