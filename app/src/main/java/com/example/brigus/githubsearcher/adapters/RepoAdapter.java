package com.example.brigus.githubsearcher.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.brigus.githubsearcher.CommitsActivity;
import com.example.brigus.githubsearcher.R;
import com.example.brigus.githubsearcher.fragments.CommitsFragment;
import com.example.brigus.githubsearcher.model.UserRepo;

import java.util.List;


public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.RepoViewHolder> {

    private final Activity mActivity;
    private final List<UserRepo> mUserRepos;

    public RepoAdapter(Activity activity, List<UserRepo> userRepos) {
        this.mActivity = activity;
        this.mUserRepos = userRepos;
    }

    @Override
    public RepoAdapter.RepoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repo_item, parent, false);

        return new RepoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RepoAdapter.RepoViewHolder holder, int position) {

        try {
            UserRepo repo = mUserRepos.get(position);
            holder.repoName_textview.setText(repo.getName().equals(null) ? "N/A" : repo.getName());
            holder.repoDescription_textview.setText(repo.getDescription() == null ? "N/A" : repo.getDescription().toString());
            holder.repoLang_textview.setText(repo.getLanguage().equals(null) ? "N/A" : repo.getLanguage());
        } catch (Exception e) {
            Log.d("SHIT HIT THE FAN", "AT: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return mUserRepos.size();
    }


    //START: Nested class for the holder
    public class RepoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.repo_name) TextView repoName_textview;
        @BindView(R.id.repo_description) TextView repoDescription_textview;
        @BindView(R.id.repo_lang) TextView repoLang_textview;

        public RepoViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {

            UserRepo repo = mUserRepos.get(this.getAdapterPosition());

            Intent intent = new Intent(mActivity, CommitsActivity.class);
            intent.putExtra(CommitsFragment.REPO_OWNER_LOGIN_EXTRA, repo.getOwner().getLogin());
            intent.putExtra(CommitsFragment.REPO_DESCRIPTION_EXTRA, repoDescription_textview.getText());
            intent.putExtra(CommitsFragment.REPO_EXTRA, repo.getName());
            mActivity.startActivity(intent);
        }
    }
    //END: Nested class
}
