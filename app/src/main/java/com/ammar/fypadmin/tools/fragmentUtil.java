package com.ammar.fypadmin.tools;

import android.content.Context;

import com.ammar.fypadmin.R;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

public class fragmentUtil {
    private static final fragmentUtil ourInstance = new fragmentUtil();
    private static Context context;

    public static Fragment getIndexFrag() {
        return indexFrag;
    }

    private static Fragment indexFrag = null;

    public static fragmentUtil getInstance(Context context) {
        fragmentUtil.context = context;
        return ourInstance;
    }

    private fragmentUtil() {
    }

    /**
     * Simply Load the fragment on the index position
     *
     * @return
     */
    public boolean loadFragment(Fragment frag) {
        if (frag != null) {
            indexFrag = frag;
            FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.frame, frag); // replace a Fragment with Frame Layout
            transaction.commit(); // commit the changes
            return true;
        }
        return false;
    }
}
