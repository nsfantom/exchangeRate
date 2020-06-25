package tm.fantom.exchangerate.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import javax.inject.Inject;

import tm.fantom.exchangerate.R;
import tm.fantom.exchangerate.RateApp;
import tm.fantom.exchangerate.databinding.ActivityMainBinding;
import tm.fantom.exchangerate.repo.SharedStorage;
import tm.fantom.exchangerate.ui.base.BaseActivity;
import tm.fantom.exchangerate.ui.base.BaseFragment;
import tm.fantom.exchangerate.ui.dashboard.DashboardFragment;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding layout;
    private BaseFragment currentFragment;
    @Inject
    SharedStorage sharedStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setUiOptions();
        super.onCreate(savedInstanceState);
        layout = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(layout.getRoot());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            layout.getRoot().setSystemUiVisibility(layout.getRoot().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        RateApp.get(this).getAppComponent().inject(this);

        if (savedInstanceState == null) {
            setUiOptions();
            navigateTo(Fragments.DASHBOARD);
        }
        initThemeMode(sharedStorage.isDarkMode());
    }

    public void setThemeMode(boolean darkMode) {
        if (sharedStorage.isDarkMode() != darkMode) {
            sharedStorage.saveDarkMode(darkMode);
//            initThemeMode(darkMode);
            recreate();
        }
    }

    private void initThemeMode(boolean darkMode) {
        setTheme(darkMode ? R.style.AppThemeDark : R.style.AppThemeLight);
    }

    private void setUiOptions() {
        View decorView = getWindow().getDecorView();
        int uiOptions = getUIOption();
        decorView.setSystemUiVisibility(uiOptions);
    }

    private int getUIOption() {
        if (Build.VERSION.SDK_INT == 22) {
            return View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        } else {
            return View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        }
    }

    public void navigateTo(@Fragments int fragments) {
        BaseFragment fragment = null;
        hideProgress();

        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            layout.mainRoot.setBackgroundResource(R.color.colorBackgroundLight);
        }
        switch (fragments) {
            case Fragments.DASHBOARD:
                fragment = DashboardFragment.newInstance();
                break;
            case Fragments.DETAILS:
//                fragment = MovieDetailFragment.newInstance();
                break;

        }
        if (fragment != null) {
            if (currentFragment != null && currentFragment.getFragmentTag().equals(fragment.getFragmentTag())) {
                return;
            }

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            if (fragment.needBackStack()) {
                fragment.setRetainInstance(true);
                fragmentTransaction.addToBackStack(fragment.getFragmentTag());
            }
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                fragmentTransaction.add(layout.container.getId(), fragment);
            } else {
                fragmentTransaction.replace(layout.container.getId(), fragment);
            }
            fragmentTransaction.commitAllowingStateLoss();
            currentFragment = fragment;
        }
    }

    public void setCurrentFragment(BaseFragment currentFragment) {
        this.currentFragment = currentFragment;
    }

    @Override
    public void onBackPressed() {
        if (currentFragment != null) {
            if (currentFragment instanceof DashboardFragment) {
                finish();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }
}
