package br.com.moviecreator.views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.io.Serializable;

import br.com.moviecreator.R;
import br.com.moviecreator.utils.AppNavigationListner;
import br.com.moviecreator.views.archived.ArchivedMoviesFragment;
import br.com.moviecreator.views.create.CreateMovieFragment;
import br.com.moviecreator.views.fragments.AbstractFragment;
import br.com.moviecreator.views.home.HomeFragment;

/**
 * Created by Mateus Emanuel Araújo on 23/10/15.
 * teusemanuel@gmail.com
 */
public class AppActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AppNavigationListner, Serializable {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {

            Bundle bundle = new Bundle();
            bundle.putSerializable("NAVIGATION_LISTNER", this);

            HomeFragment fragment = new HomeFragment();
            fragment.setArguments(bundle);

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();

            getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    AbstractFragment f = (AbstractFragment) getSupportFragmentManager().findFragmentById(R.id.container);
                    if (f != null) {
                        f.setTitle();
                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_top_movies) {
            updateCurrentFragment(new HomeFragment());
        } else if (id == R.id.nav_new_movie) {
            updateCurrentFragment(new CreateMovieFragment());
        } else if (id == R.id.nav_gallery) {
            updateCurrentFragment(new ArchivedMoviesFragment());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void updateCurrentFragment(@NonNull AbstractFragment currentFragment) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("NAVIGATION_LISTNER", AppActivity.this);
        currentFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, currentFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(currentFragment.getTag())
                .commit();
    }
}
