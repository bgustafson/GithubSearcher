package com.example.brigus.githubsearcher.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.brigus.githubsearcher.R;
import com.example.brigus.githubsearcher.adapters.CommitAdapter;
import com.example.brigus.githubsearcher.model.Commit;
import com.example.brigus.githubsearcher.services.GithubEndpoints;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;


public class CommitsFragment extends Fragment {

    public static final String REPO_OWNER_LOGIN_EXTRA = "com.example.brigus.githubsearcher.repoownerlogin";
    public static final String REPO_EXTRA = "com.example.brigus.githubsearcher.repo";
    public static final String REPO_DESCRIPTION_EXTRA = "com.example.brigus.githubsearcher.repodescription";

    @BindView(R.id.project_name) TextView mProjectName_TextView;
    @BindView(R.id.project_description) TextView mProjectDescription_TextView;
    @BindView(R.id.commits_recycler) RecyclerView recyclerView;

    private Unbinder mUnbinder;


    public static CommitsFragment newInstance(Bundle bundle) {

        CommitsFragment fragment = new CommitsFragment();
        fragment.setArguments(bundle);

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_commits, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        String repoOwnerLogin = getArguments().getString(REPO_OWNER_LOGIN_EXTRA);
        String repo = getArguments().getString(REPO_EXTRA);
        String description = getArguments().getString(REPO_DESCRIPTION_EXTRA);

        mProjectName_TextView.setText(repo);
        mProjectDescription_TextView.setText(description);

        //open the details view here
        OkHttpClient client = new OkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(GithubEndpoints.BASE_URL)
                .build();

        GithubEndpoints service = retrofit.create(GithubEndpoints.class);
        final Call<List<Commit>> repoCommits = service.getRepoCommits(repoOwnerLogin, repo);

        repoCommits.enqueue(new Callback<List<Commit>>() {
            @Override
            public void onResponse(Call<List<Commit>> call, Response<List<Commit>> response) {

                Activity activity = getActivity();

                List<Commit> commits = response.body();
                DividerItemDecoration mDividerItemDecoration =
                        new DividerItemDecoration(activity, activity.getResources().getConfiguration().orientation);
                recyclerView.addItemDecoration(mDividerItemDecoration);
                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(new CommitAdapter(activity, commits));

            }

            @Override
            public void onFailure(Call<List<Commit>> call, Throwable t) {
                Toast.makeText(getActivity(), "Failed.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


    @Override
    public void onDestroyView() {
        mUnbinder.unbind();
        super.onDestroyView();
    }
}
