package com.magnoleal.retrofitcrudexample.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.magnoleal.retrofitcrudexample.R;
import com.magnoleal.retrofitcrudexample.model.Post;
import com.magnoleal.retrofitcrudexample.service.PostService;
import com.magnoleal.retrofitcrudexample.util.Dialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostFormActivity extends AppCompatActivity {

    private EditText etTitle;
    private EditText etBody;
    private PostService postService;
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_form);

        etTitle = (EditText) findViewById(R.id.etTitle);
        etBody = (EditText) findViewById(R.id.etBody);
        postService = new PostService();

        post = getIntent().getParcelableExtra("post");
        if(post != null) {
            populateForm();
            setTitle("Edit Post");
        }else{
            post = new Post();
            setTitle("New Post");
        }

    }

    public void clickBtSend(View v){
        validate();
    }

    private void populateForm(){
        etTitle.setText(post.getTitle());
        etBody.setText(post.getBody());
    }

    public void validate(){
        boolean isValid = true;

        if(etTitle.getText().toString().trim().isEmpty()){
            isValid = false;
            etTitle.setError("Field required");
        }
        if(etBody.getText().toString().trim().isEmpty()){
            isValid = false;
            etBody.setError("Field required");
        }

        if(isValid){
            submit();
        }

    }

    private void submit(){

        post.setTitle(etTitle.getText().toString().trim());
        post.setBody(etBody.getText().toString().trim());

        final ProgressDialog pd = ProgressDialog.show(this, "Wait", "Sending data...");
        Call<Post> postCall;

        if(post.getId() > 0) {
            postCall = postService.update(post);
        }
        else{
            postCall = postService.create(post);
        }

        postCall.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                pd.dismiss();
                Dialog.closeActivityAlert(PostFormActivity.this, "Success", "Post saved!");
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                pd.dismiss();
                Dialog.alert(PostFormActivity.this, "Error", t.getMessage());
            }
        });
    }
}
