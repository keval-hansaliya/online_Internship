package com.internship;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class DashboardActivity extends AppCompatActivity {

    MeowBottomNavigation mbottomNavigation;

    int HOME_MENU = 1;
    int CART_MENU = 2;
    int WISHLIST_MENU = 3;
    int PROFILE_MENU = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mbottomNavigation = findViewById(R.id.dashboard_bottom);

        mbottomNavigation.add(new MeowBottomNavigation.Model(HOME_MENU, R.drawable.image_home));
        mbottomNavigation.add(new MeowBottomNavigation.Model(CART_MENU, R.drawable.image_cart));
        mbottomNavigation.add(new MeowBottomNavigation.Model(WISHLIST_MENU, R.drawable.image_favourite));
        mbottomNavigation.add(new MeowBottomNavigation.Model(PROFILE_MENU, R.drawable.image_username));

        mbottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                // your codes
                if (item.getId() == HOME_MENU) {
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.dashboard_relative, new HomeFragment()).commit();

                    mbottomNavigation.show(HOME_MENU, true);
                }
                else if (item.getId() == CART_MENU) {
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.dashboard_relative, new WishlistFragment()).commit();

                    mbottomNavigation.show(CART_MENU, true);
                }
                else if (item.getId() == WISHLIST_MENU) {
                    mbottomNavigation.show(WISHLIST_MENU, true);
                }
                else if (item.getId() == PROFILE_MENU) {
                    FragmentManager manager = getSupportFragmentManager();
                    manager.beginTransaction().replace(R.id.dashboard_relative, new ProfileFragment()).commit();

                    mbottomNavigation.show(PROFILE_MENU, true);
                } else {}
            }
        });

        mbottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                // your codes
            }
        });

        mbottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                // your codes
            }
        });

        mbottomNavigation.show(HOME_MENU, true);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.dashboard_relative, new HomeFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }
}