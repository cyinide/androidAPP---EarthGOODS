package com.example.guestuser.damirkrkalicearthgoods.helper;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;

public class MyFragmentHelper {

    private static FragmentManager fm;
    private static FragmentTransaction ft;


    public interface RunnableCallback<T> extends Serializable{
        void run(T t);
    }

    public static void fragmentReplace(AppCompatActivity activity, int targetLayoutId, Fragment fragment, String fragmentTag, boolean addToBackStack) {
        fm = activity.getSupportFragmentManager();
        ft = fm.beginTransaction();

        ft.replace(targetLayoutId, fragment, fragmentTag);

        if (addToBackStack) {
            ft.addToBackStack(null);
        }

        ft.commit();
    }

    public static void dodajDialog(AppCompatActivity activity, String tag, DialogFragment dialogFragment) {
        fm = activity.getSupportFragmentManager();
        dialogFragment.show(fm, tag);
    }

    public static void  refreshFragment (AppCompatActivity activity, String tag)
    {
        Fragment frg = null;
        frg = activity.getSupportFragmentManager().findFragmentByTag(tag);
        final FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
    }
}
