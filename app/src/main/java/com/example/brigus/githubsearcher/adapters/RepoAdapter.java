package com.example.brigus.githubsearcher.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.brigus.githubsearcher.R;
import com.example.brigus.githubsearcher.model.Commit;
import com.example.brigus.githubsearcher.model.UserRepo;
import com.example.brigus.githubsearcher.services.GithubEndpoints;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

            //open the details view here
            OkHttpClient client = new OkHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(GithubEndpoints.BASE_URL)
                    .build();

            GithubEndpoints service = retrofit.create(GithubEndpoints.class);
            final Call<List<Commit>> repoCommits = service.getRepoCommits(repo.getOwner().getLogin(), repo.getName());

            repoCommits.enqueue(new Callback<List<Commit>>() {
                @Override
                public void onResponse(Call<List<Commit>> call, Response<List<Commit>> response) {

                    List<Commit> commits = response.body();
                    //TODO:Do something when the commits list comes back.
                    Toast.makeText(mActivity, "Commit sha: " + commits.get(0).getSha(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<List<Commit>> call, Throwable t) {
                    Toast.makeText(mActivity, "Failed.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    //END: Nested class
}
