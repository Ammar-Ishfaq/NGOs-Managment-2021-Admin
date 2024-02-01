package com.ammar.fypadmin.Home;

import android.content.Intent;
import android.os.Bundle;

import com.ammar.fypadmin.Home.Fragments.AllUsers;
import com.ammar.fypadmin.R;
import com.ammar.fypadmin.tools.fragmentUtil;
import com.ammar.fypadmin.tools.mainPermissionUtils;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    AllUsers allUsers = new AllUsers();
    private com.ammar.fypadmin.tools.mainPermissionUtils mainPermissionUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fragmentUtil.getInstance(this).loadFragment(allUsers);
        mainPermissionUtils = new mainPermissionUtils(this);
        mainPermissionUtils.CheckAllPermissionNeeded();//maintain all the permissions for that app

    }

    @Override
    public void onBackPressed() {
        if (fragmentUtil.getInstance(this).getIndexFrag() instanceof AllUsers)
            super.onBackPressed();
        else fragmentUtil.getInstance(this).loadFragment(allUsers);

    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.top_app_bar, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mainPermissionUtils.CheckAllPermissionNeeded();//check that all the permissions are granted r  not

//        }
    }
}