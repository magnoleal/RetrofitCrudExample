package com.magnoleal.retrofitcrudexample.service;

import com.magnoleal.retrofitcrudexample.dao.PostDao;
import com.magnoleal.retrofitcrudexample.model.Post;

import java.util.List;

import retrofit2.Call;

/**
 * Created by cti on 19/05/16.
 */
public class PostService extends AbstractService {

    private PostDao dao;

    public PostService() {
        super.init();
        this.dao = this.retrofit.create(PostDao.class);
    }

    public Call<List<Post>> all(){
        return this.dao.all();
    }

    public Call<Post> find(int id){
        return this.dao.find(id);
    }

    public Call<Post> create(Post post){
        return this.dao.create(post);
    }

    public Call<Post> create(String title, String body){
        return this.dao.create(title, body);
    }

    public Call<Post> update(Post post){
        return this.dao.update(post.getId(), post);
    }

    public Call<Post> update(int id, String title, String body){
        return this.dao.update(id, title, body);
    }

    public Call<Post> delete(int id){
        return this.dao.delete(id);
    }

}
