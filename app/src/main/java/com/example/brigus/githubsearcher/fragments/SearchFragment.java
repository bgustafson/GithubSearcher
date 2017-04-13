package com.example.brigus.githubsearcher.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.example.brigus.githubsearcher.R;
import com.example.brigus.githubsearcher.UserDetailsActivity;
import com.example.brigus.githubsearcher.model.UserDetails;
import com.example.brigus.githubsearcher.services.GithubEndpoints;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SearchFragment extends Fragment {

    //@BindView(R.id.username_edittext) EditText mUserName_EditText;
    @BindView(R.id.autocomplete_username_textview) AutoCompleteTextView mAutoCompleteUserName_TextView;
    @BindView(R.id.find_user_button) Button mFindUser_Button;

    private Unbinder mUnbinder;


    public static SearchFragment newInstance() {
        return new SearchFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mUnbinder = ButterKnife.bind(this, view);


        String[] autoCompleteUsers = getResources().getStringArray(R.array.users_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<> (getActivity(), android.R.layout.simple_list_item_1, autoCompleteUsers);
        mAutoCompleteUserName_TextView.setAdapter(adapter);

        return view;
    }

    @OnClick(R.id.find_user_button)
    public void findUser() {

        String username;
        //username = mUserName_EditText.getText().toString().equals(null) ? "" : mUserName_EditText.getText().toString();
        //if(username.equals("")){
            username = mAutoCompleteUserName_TextView.getText().toString().equals(null) ? "" : mAutoCompleteUserName_TextView.getText().toString();
        //}

        OkHttpClient client = new OkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(GithubEndpoints.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GithubEndpoints service = retrofit.create(GithubEndpoints.class);
        final Call<UserDetails> userDetails = service.getUserDetails(username);

        userDetails.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                final UserDetails details = response.body();

                Intent intent = new Intent(getActivity(), UserDetailsActivity.class);
                intent.putExtra(UserDetailsFragment.USER_EXTRA, details);
                getActivity().startActivity(intent);
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                Toast.makeText(getActivity(), "Failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}
