package br.com.moviecreator.views.details;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;

import java.io.File;

import br.com.moviecreator.R;
import br.com.moviecreator.models.Movie;
import br.com.moviecreator.models.MoviesSearch;
import br.com.moviecreator.persistence.MovieDAO;
import br.com.moviecreator.proxies.MovieProxy;
import br.com.moviecreator.proxies.listeners.ProxyListener;
import br.com.moviecreator.views.app.App;
import br.com.moviecreator.views.details.fragments.DetailsFragments;
import br.com.moviecreator.views.fragments.AbstractFragment;
import br.com.moviecreator.views.home.HomeFragment;

public class DetailsActivity extends AppCompatActivity {

    public static final String DETAILS_EXTRA = "DETAILS_EXTRA";
    public static final String IS_LOCAL_FILE = "IS_LOCAL_FILE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_details);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NetworkImageView posterDetailsImageView = (NetworkImageView) findViewById(R.id.movie_image_details);

        if (savedInstanceState == null) {

            DetailsFragments fragment = new DetailsFragments();
            fragment.setArguments(getIntent().getExtras());

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
