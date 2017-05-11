package com.example.brigus.githubsearcher;

import android.support.v4.app.Fragment;
import com.example.brigus.githubsearcher.fragments.CommitsFragment;


public class CommitsActivity extends SingleFragmentHostActivity {

    @Override
    protected Fragment createFragment() {
        return CommitsFragment.newInstance(getIntent().getExtras());
    }
}
