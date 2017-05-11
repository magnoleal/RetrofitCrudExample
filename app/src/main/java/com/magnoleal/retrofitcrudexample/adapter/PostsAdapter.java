package com.magnoleal.retrofitcrudexample.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.magnoleal.retrofitcrudexample.R;
import com.magnoleal.retrofitcrudexample.model.Post;

import java.util.List;

/**
 * Created by cti on 28/04/16.
 */
public class PostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Post> mList;
    private PostsAdapter.PostClickListener listener;

    public interface PostClickListener{
        void onClick(View view, int position, Post Post);
        void onLongClick(View view, int position, Post Post);
    }

    public PostsAdapter(List<Post> mList, PostsAdapter.PostClickListener listener) {
        this.mList = mList;
        this.listener = listener;
    }

    public List<Post> getList(){
        return mList;
    }

    public void setList(List<Post> Posts){
        this.mList = Posts;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PostsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false));
    }
    
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder hd, final int position) {

        final Post item = mList.get(position);

        final PostsAdapter.ViewHolder holder = (PostsAdapter.ViewHolder) hd;

        holder.tvTitle.setText(item.getTitle());
        holder.tvBody.setText(item.getBody());

        if(listener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(v, position, item);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onLongClick(v, position, item);
                    return false;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public long getItemId(int position) {
        return mList == null ? -1 : mList.get(position).getId();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull private final TextView tvTitle;
        @NonNull private final TextView tvBody;

        public ViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvBody = (TextView) view.findViewById(R.id.tvBody);
        }
    }

}