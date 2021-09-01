package com.example.shopbanchau;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ListBillsAdapter extends FragmentStatePagerAdapter {
    public ListBillsAdapter( FragmentManager fm, int behavior) {
        super(fm, behavior);
    }


    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                return new BillsWaitConfirmFragment();
            case 1:
                return new BillConfirmFragment();
            case 2:
                return new BillReceivedFragment();
            default:
                return new BillsWaitConfirmFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = "Chờ xác nhận";
                break;
            case 1:
                title = "Chờ lấy hàng";
                break;
            case 2:
                title = "Đã giao";
                break;
        }
        return title;
    }
}
