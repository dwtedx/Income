package com.dwtedx.income.home;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.dwtedx.income.R;
import com.dwtedx.income.account.AccountFragment;
import com.dwtedx.income.addrecord.AddRecordActivity;
import com.dwtedx.income.base.BaseActivity;
import com.dwtedx.income.connect.ProgressDialog;
import com.dwtedx.income.connect.SaDataProccessHandler;
import com.dwtedx.income.connect.SaException;
import com.dwtedx.income.entity.ApplicationData;
import com.dwtedx.income.entity.DiVersion;
import com.dwtedx.income.profile.ProfileActivity;
import com.dwtedx.income.provider.HomeTipSharedPreferences;
import com.dwtedx.income.report.HomeReportPayingPieFragment;
import com.dwtedx.income.report.ShareActivity;
import com.dwtedx.income.service.UserService;
import com.dwtedx.income.updateapp.UpdateService;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private NavigationView navigationView;

    private IncomeLineFragment mMoneyLineFragment;
    private HomeReportPayingPieFragment mReportFragment;
    private AccountFragment mAccountFragment;
    private ImageView mHeaderView;
    private TextView mUsernameView;
    private Button mHomeTipButton;
    private RelativeLayout mHomeTipLayout;

    private int mMenuItemId;

    private int mMenuItemSelectId;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.homelogo);
        setSupportActionBar(toolbar);

        //是否有提示
        HomeTipSharedPreferences.init(this);
        if(HomeTipSharedPreferences.getIsTip()){
            mHomeTipLayout = (RelativeLayout) findViewById(R.id.home_tip_layout);
            mHomeTipLayout.setVisibility(View.VISIBLE);
            mHomeTipButton = (Button) findViewById(R.id.home_tip_button);
            mHomeTipButton.setOnClickListener(this);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (mMenuItemId == R.id.nav_money) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
                    transaction.replace(R.id.container, mMoneyLineFragment);
                    transaction.commitAllowingStateLoss();
                } else if (mMenuItemId == R.id.nav_report) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
                    transaction.replace(R.id.container, mReportFragment);
                    transaction.commitAllowingStateLoss();
                } else if (mMenuItemId == R.id.nav_wallet) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
                    transaction.replace(R.id.container, mAccountFragment);
                    transaction.commitAllowingStateLoss();
                }
                cancelProgressDialog();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        mHeaderView = (ImageView) headerView.findViewById(R.id.imageView);
        mUsernameView = (TextView) headerView.findViewById(R.id.usernameView);
        headerView.findViewById(R.id.nav_header_view).setOnClickListener(this);

        //Fragment
        mMoneyLineFragment = new IncomeLineFragment();
        mReportFragment = new HomeReportPayingPieFragment();
        mAccountFragment = new AccountFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, mMoneyLineFragment).commit();
            mMenuItemSelectId = R.id.nav_money;
        }

        getVersions();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isLogin()) {
            Glide.with(this).load(ApplicationData.mDiUserInfo.getHead()).into(mHeaderView);
            mUsernameView.setText(ApplicationData.mDiUserInfo.getName());
        }else {
            Glide.with(this).load(R.mipmap.userhead).into(mHeaderView);
            mUsernameView.setText(getString(R.string.profile_login));
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
        mMenuItemId = item.getItemId();
        if (mMenuItemId == R.id.nav_profile) {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (mMenuItemId == R.id.nav_share) {
            Intent intent = new Intent(HomeActivity.this, ShareActivity.class);
            startActivity(intent);
            //openShare();
        } else if (mMenuItemId == R.id.nav_send) {
            //Snackbar.make(findViewById(R.id.app_title), "玩命开发中", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            intent.putExtra("isShowData", true);
            startActivity(intent);
        }else {
            mMenuItemSelectId = item.getItemId();
            showProgressDialog();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home_list) {
            startActivity(new Intent(HomeActivity.this, IncomeListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_tip_button:
                mHomeTipLayout.setVisibility(View.GONE);
                HomeTipSharedPreferences.setIsTip(ApplicationData.mAppVersionCode);
                break;

            case R.id.fab:
                startActivity(new Intent(HomeActivity.this, AddRecordActivity.class));
                break;

            case R.id.nav_header_view:
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                break;

        }
    }

    public void showProgressDialog() {
        mProgressDialog = getProgressDialog();
        mProgressDialog.show();
    }

    public void cancelProgressDialog() {
        if(null != mProgressDialog) {
            mProgressDialog.cancel();
        }
    }

    private void getVersions() {
        SaDataProccessHandler<Void, Void, DiVersion> dataVerHandler = new SaDataProccessHandler<Void, Void, DiVersion>(HomeActivity.this) {
            @Override
            public void onSuccess(final DiVersion data) {
                if (data.isUpdate()) {
                    new MaterialDialog.Builder(HomeActivity.this)
                            .title(getString(R.string.tip_update) + data.getVersion())
                            .content(data.getContent())
                            .positiveText(R.string.ok)
                            .negativeText(R.string.cancel)
                            .onAny(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    //NEGATIVE   POSITIVE
                                    if (which.name().equals("POSITIVE")) {
                                        //调用拨号面板：
                                        Intent intent = new Intent(HomeActivity.this, UpdateService.class);
                                        intent.putExtra("Key_App_Name", "Income");
                                        intent.putExtra("Key_Down_Url", data.getApkurl());
                                        HomeActivity.this.startService(intent);
                                    }
                                }
                            })
                            .show();
                }
            }

            @Override
            public void onPreExecute() {

            }

            @Override
            public void handlerError(SaException e) {

            }
        };
        UserService.getInstance().versionUpdate(dataVerHandler);
    }

}
