package com.example.brigus.githubsearcher.adapters;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.brigus.githubsearcher.R;
import com.example.brigus.githubsearcher.model.Commit;

import java.util.List;

public class CommitAdapter extends RecyclerView.Adapter<CommitAdapter.CommitViewHolder> {

    private final Activity mActivity;
    private final List<Commit> mCommits;

    public CommitAdapter(Activity activity, List<Commit> commits) {
        this.mActivity = activity;
        this.mCommits = commits;
    }

    @Override
    public CommitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.commit_item, parent, false);

        return new CommitAdapter.CommitViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(CommitViewHolder holder, int position) {
        try {
            Commit commit = mCommits.get(position);
            holder.commitAuthor_textview.setText(commit.getCommit().getAuthor().getName());
            holder.commitDate_textView.setText(commit.getCommit().getAuthor().getDate());
            holder.commitMessage_textview.setText(commit.getCommit().getMessage());

        } catch (Exception e) {
            Log.d("SHIT HIT THE FAN", "AT: " + position);
        }
    }


    @Override
    public int getItemCount() {
        return mCommits.size();
    }


    //START: Nested Class
    protected class CommitViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.commit_author) TextView commitAuthor_textview;
        @BindView(R.id.commit_date) TextView commitDate_textView;
        @BindView(R.id.commit_message) TextView commitMessage_textview;


        public CommitViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }



    }
    //END
}
