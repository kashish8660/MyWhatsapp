package com.example.mywhatsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter { //just need to extend to 'FragmentPagerAdapter', and android studio will suggest constructor, getItem() and getCount().
    public FragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0 : return new ChatFragment();
//            break;
            case 1 : return new StatusFragment();
//            break;
            case 2 : return new CallFragment();
//            break;
            default : return new ChatFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) { //this function was not suggested by Andorid Studio
        switch (position){
            case 0 : return "Chat";
//            break;
            case 1 : return "Status";
//            break;
            case 2 : return "Call";
//            break;
            default : return null;
        }
    }
}
