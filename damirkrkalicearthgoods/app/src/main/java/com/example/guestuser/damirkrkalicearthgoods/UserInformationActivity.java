package com.example.guestuser.damirkrkalicearthgoods;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.guestuser.damirkrkalicearthgoods.fragments.UserInformationFragment;
import com.example.guestuser.damirkrkalicearthgoods.helper.MyFragmentHelper;


public class UserInformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_information_activity_layout);

        MyFragmentHelper.fragmentReplace(this,
                R.id.fragmentProfilContainer,
                UserInformationFragment.newInstance(),
                UserInformationFragment.Tag,
                false);

    }

}
