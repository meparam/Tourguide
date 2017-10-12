package com.thinktanki.atmfinder;

import android.*;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.thinktanki.atmfinder.util.AndroidUtil;
import com.thinktanki.atmfinder.util.TrackGPS;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class SplashScreenNew extends AppCompatActivity implements Animation.AnimationListener{

    ImageView tg_logoImageView,tg_logoName;
    String USERLOGINSTATUS="UserLoginStatus";
    String USERNAME="username";
    String FIRSTNAME="firstname";
    String LASTNAME="lastname";
    String PASSWORD="password";
    String MOBILE="mobile";
    String EMAILID="emailid";
    String PROFILEIMAGE="image";
    Animation animationBounce;
    String STATUS="status";
    boolean status=false;
    LinearLayout linearLayout;
    String Status;

    private AndroidUtil androidUtil;
    private TrackGPS trackGPS;
    final private int PERMISSION_REQUEST = 12;
    private String TAG = SplashScreen.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    final private int WAIT_IN_MILLISECOND = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        linearLayout = (LinearLayout) findViewById(R.id.activity_splash_screen_relativelayout);
        tg_logoImageView = (ImageView) findViewById(R.id.tg_logo);
        tg_logoName = (ImageView) findViewById(R.id.tg_name);
        animationBounce = AnimationUtils.loadAnimation(this,R.anim.bounce);
        animationBounce.setAnimationListener(this);
        linearLayout.startAnimation(animationBounce);

        androidUtil=new AndroidUtil(this);
        androidUtil.changeStatusBarColor();
//Checking for permission
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startApp();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST);
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {
        sharedPreferences = getSharedPreferences(USERLOGINSTATUS , MODE_PRIVATE);
        //SharedPreferences.Editor editor = sharedPreferences.edit();
        Status = sharedPreferences.getString(STATUS,"");

//        if(Status.equals("LoggedIn"))
//        {
//           status=true;
//        }
//        else {
//            status=false;
//        }

    }

    @Override
    public void onAnimationEnd(Animation animation) {

        SharedPreferences     sharedPreferences = getSharedPreferences(USERLOGINSTATUS, Context.MODE_PRIVATE);
        status=sharedPreferences.getBoolean("islogin",false);

        if(status)
        {
            String Username = sharedPreferences.getString(USERNAME,"");
            //Intent intent = new Intent(getBaseContext(), UserProfileActivity.class);
            //Intent intent = new Intent(getBaseContext(), UsersPageActivity.class);
            //Intent intent = new Intent(getBaseContext(),UserMainScreen_Map.class);
            Intent intent = new Intent(getBaseContext(),MainActivity.class);
            intent.putExtra("username",Username);
            startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(getBaseContext(), SignInScreen.class);
            startActivity(intent);
        }
        finish();

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startApp();

                } else {
                    finish();
                    Toast.makeText(SplashScreenNew.this, "Please provide the GPS permission to this app", Toast.LENGTH_LONG).show();

                }
                return;
            }
        }
    }

    /*Start the Application*/
    private void startApp() {
        if (!androidUtil.checkNetworkStatus()) {
            new AlertDialog.Builder(this)
                    .setMessage(getResources().getString(R.string.internet_dialog_msg))
                    .setTitle(getResources().getString(R.string.internet_dialog_title))
                    .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show();

        } else {

            trackGPS = new TrackGPS(this);
            if (trackGPS.canGetLocation()) {
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("LATITUDE", String.valueOf(trackGPS.getLatitude()));
                editor.putString("LONGITUDE", String.valueOf(trackGPS.getLongitude()));
                editor.putString("RADIUS", "1000");
                editor.commit();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashScreenNew.this, MainActivity.class);
                        startActivity(intent);
                        SplashScreenNew.this.finish();
                    }
                }, WAIT_IN_MILLISECOND);
            } else {
                trackGPS.showSettingsAlert();
            }
        }
    }

}
