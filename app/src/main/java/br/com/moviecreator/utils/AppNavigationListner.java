package br.com.moviecreator.utils;

import android.support.v4.app.Fragment;

import java.io.Serializable;

import br.com.moviecreator.views.fragments.AbstractFragment;

/**
 * Created by Mateus Emanuel Araújo on 24/10/15.
 * teusemanuel@gmail.com
 */
public interface AppNavigationListner extends Serializable {
    void updateCurrentFragment(AbstractFragment currentFragment);
}
