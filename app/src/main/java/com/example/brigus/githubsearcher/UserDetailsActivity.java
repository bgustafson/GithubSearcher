package com.example.brigus.githubsearcher;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.brigus.githubsearcher.fragments.UserDetailsFragment;

public class UserDetailsActivity extends SingleFragmentHostActivity {

    @Override
    protected Fragment createFragment() {
        return UserDetailsFragment.newInstance();
    }
}
