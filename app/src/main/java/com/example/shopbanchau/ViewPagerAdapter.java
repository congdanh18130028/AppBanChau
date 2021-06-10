package com.example.shopbanchau;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.shopbanchau.utils.DataLocalManager;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                if(DataLocalManager.getToken().equals("")){
                    return new CartFragment();
                }else {
                    return new CartLoginFragment();
                }
            case 2:
                return new ChatFragment();
            case 3:
                if(DataLocalManager.getToken().equals("")){
                    return new UserFragment();
                }else {
                    return new UserLoginFragment();
                }
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
