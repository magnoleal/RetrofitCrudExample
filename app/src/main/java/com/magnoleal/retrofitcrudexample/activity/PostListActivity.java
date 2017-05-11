package com.magnoleal.retrofitcrudexample.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.magnoleal.retrofitcrudexample.R;
import com.magnoleal.retrofitcrudexample.adapter.PostsAdapter;
import com.magnoleal.retrofitcrudexample.model.Post;
import com.magnoleal.retrofitcrudexample.service.PostService;
import com.magnoleal.retrofitcrudexample.util.Dialog;
import com.magnoleal.retrofitcrudexample.util.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostListActivity extends AppCompatActivity implements PostsAdapter.PostClickListener {

    private RecyclerView recyclerView;
    private PostsAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private PostService postService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(mLayoutManager);
        this.swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe);
        this.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadList();
            }
        });

        this.postService = new PostService();
        this.adapter = new PostsAdapter(new ArrayList<Post>(), this);

        this.recyclerView.setAdapter(this.adapter);
        this.recyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .color(ContextCompat.getColor(this, R.color.divider))
                        .build());

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostListActivity.this, PostFormActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter.getList().isEmpty())
            loadList();
    }

    private void loadList() {

        swipeRefresh.setRefreshing(true);

        Call<List<Post>> listCall = this.postService.all();

        listCall.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                try{
                    List<Post> mListRemote = postService.responseDefault(response);
                    adapter.setList(mListRemote);
                    adapter.notifyDataSetChanged();
                }catch (Exception ex){
                    Dialog.alert(PostListActivity.this, "Error", ex.getMessage());
                }finally {
                    swipeRefresh.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                swipeRefresh.setRefreshing(false);
                Dialog.alert(PostListActivity.this, "Error", t.getMessage());
            }
        });

    }

    @Override
    public void onClick(View view, int position, Post post) {
        Intent intent = new Intent(PostListActivity.this, PostFormActivity.class);
        intent.putExtra("post", post);
        startActivity(intent);
    }

    @Override
    public void onLongClick(View view, final int position, final Post post) {
        Dialog.confirm(this, "Question", "You want to delete this post?", new Callable() {
            @Override
            public Object call() throws Exception {

                final ProgressDialog pd = ProgressDialog.show(PostListActivity.this, "Wait", "Deleting data...");
                Call<Post> deleteCall = postService.delete(post.getId());
                deleteCall.enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call, Response<Post> response) {
                        pd.dismiss();
                        adapter.getList().remove(position);
                        adapter.notifyItemRemoved(position);
                        Dialog.alert(PostListActivity.this, "Success", "Post deleted!");
                    }

                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {
                        pd.dismiss();
                        Dialog.alert(PostListActivity.this, "Error", t.getMessage());
                    }
                });

                return null;
            }
        }, null);
    }
}
