package com.example.brigus.githubsearcher.services;


import com.example.brigus.githubsearcher.model.Commit;
import com.example.brigus.githubsearcher.model.UserDetails;
import com.example.brigus.githubsearcher.model.UserRepo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface GithubEndpoints {

    String BASE_URL = "https://api.github.com/";

    @GET("users/{user}")
    Call<UserDetails> getUserDetails(@Path("user") String user);


    @GET("users/{user}/repos")
    Call<List<UserRepo>> getUserRepos(@Path("user") String user);
    

    @GET("repos/{user}/{repo_name}/commits")
    Call<List<Commit>> getRepoCommits(@Path("user") String user, @Path("repo_name") String repo_name);

}
