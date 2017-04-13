package com.example.brigus.githubsearcher.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.example.brigus.githubsearcher.R;
import com.example.brigus.githubsearcher.adapters.RepoAdapter;
import com.example.brigus.githubsearcher.model.UserDetails;
import com.example.brigus.githubsearcher.model.UserRepo;
import com.example.brigus.githubsearcher.services.GithubEndpoints;
import com.squareup.picasso.Picasso;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;


public class UserDetailsFragment extends Fragment {

    public static final String USER_EXTRA = "com.example.brigus.githubsearcher.userdetails";
    private Unbinder mUnbinder;

    @BindView(R.id.user_avatar) ImageView userAvatar;
    @BindView(R.id.username_detail) TextView username_textview;
    @BindView(R.id.login_detail) TextView login_textview;
    @BindView(R.id.repos_recycler) RecyclerView recyclerView;


    public static UserDetailsFragment newInstance() {
        return new UserDetailsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_details, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        final UserDetails details = getActivity().getIntent().getExtras().getParcelable(USER_EXTRA);
        Picasso.with(getContext()).load(details.getAvatarUrl()).into(userAvatar);
        username_textview.setText(details.getName());
        login_textview.setText(details.getLogin());

        OkHttpClient client = new OkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(GithubEndpoints.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GithubEndpoints service = retrofit.create(GithubEndpoints.class);
        final Call<List<UserRepo>> userRepos = service.getUserRepos(details.getLogin());

        userRepos.enqueue(new Callback<List<UserRepo>>() {
            @Override
            public void onResponse(Call<List<UserRepo>> call, Response<List<UserRepo>> response) {
                final List<UserRepo> repos = response.body();

                //LinearLayoutManager layoutManager
                //        = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        //GridLayoutManager(getActivity(), 2));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(new RepoAdapter(getActivity(), repos));
            }

            @Override
            public void onFailure(Call<List<UserRepo>> call, Throwable t) {
                Toast.makeText(getActivity(), "Failed.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
