package com.example.sk_m4_gg.projectsmart;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Badgeable;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText Fusernames, Fpasswords;
    Button loginbutton;
    TextView showtext;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    NavigationView navigationView;
    ActionBarDrawerToggle dtoggle;
  // Intent  i= new Intent(this, UserProfile.class);
    public static final String USER_NAME = "USERNAME";
    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Drawer.Result  drawer_r=null;
        setContentView(R.layout.activity_main);
        Fusernames =(EditText)findViewById(R.id.editText);
        Fpasswords  =(EditText)findViewById(R.id.editText2);
        loginbutton=(Button)findViewById(R.id.button);
        showtext  =(TextView)findViewById(R.id.textView2);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        dtoggle=setupActionBarDrawerToggle();
        navigationView= (NavigationView) findViewById(R.id.nvView);

        setupDrawerContent(navigationView);
        mDrawer.setDrawerListener(dtoggle);

        final Drawer.Result finalDrawer_r1 = drawer_r;
       drawer_r= new Drawer().withActivity(this).withToolbar(toolbar).withActionBarDrawerToggle(true).withHeader(R.layout.header).addDrawerItems(
                new PrimaryDrawerItem().withName(R.string.Home).withIcon(FontAwesome.Icon.faw_home)).withOnDrawerListener(
                new Drawer.OnDrawerListener(){
                    @Override
                    public void onDrawerOpened(View views){
                        InputMethodManager  manager   =  (InputMethodManager)MainActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        manager.hideSoftInputFromInputMethod(MainActivity.this.getCurrentFocus().getWindowToken(),0);

                    }

                    @Override
                    public void onDrawerClosed(View view) {

                    }

                    @Override
                    public void onDrawerSlide(View view, float v) {

                    }
                }).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem drawerItems) {
                if(drawerItems  instanceof Nameable){
                    Toast.makeText(MainActivity.this,MainActivity.this.getString(((Nameable)drawerItems).getNameRes()),Toast.LENGTH_SHORT).show();
                }
                if(drawerItems instanceof Badgeable){
                    Badgeable  badgeable =  (Badgeable)drawerItems;
                    if(badgeable.getBadge()!=null){
                        try{
                            int b  =  Integer.valueOf(badgeable.getBadge());
                            if(b>0){
                                finalDrawer_r1.updateBadge(String.valueOf(b - 1), i);
                            }
                        }catch (Exception  e ){
                            Log.e("Exceprion  e","Exception ");
                        }
                    }
                }

    }
        }).build();

        loginbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                login();
            }

            private void login() {
                username  =Fusernames.getText().toString();
               password  = Fpasswords.getText().toString();
             invokelogin(username, password);
            }
        });
    }

    private void invokelogin(final String username, final String password) {

        class LoginAsyc  extends AsyncTask<String ,Void ,String>{
            Dialog dialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                dialog = ProgressDialog.show(MainActivity.this,"Please wait...","Loading...");
            }

            @Override
            protected void onPostExecute(String s) {
                String r   =s.trim();
                dialog.dismiss();
                if(r.equalsIgnoreCase("success")){
                    Intent  n  =  new Intent(MainActivity.this,Teacherinfo.class);
                    n.putExtra(USER_NAME,username);
                    finish();
                    startActivity(n);
                    Toast.makeText(getApplicationContext(),"Амжилттай нэвтэрлээ",Toast.LENGTH_SHORT).show();
                }
  else  {
                    Toast.makeText(getApplicationContext(),"Таны нэр  эсвэл  нууц үг алдаатай байна",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {

            //    Toast.makeText(getApplicationContext(),"irlee",Toast.LENGTH_SHORT).show();
                 String  username  = params[0];
                String  password = params[1];
                InputStream  st=null;
                List<NameValuePair> lname =  new ArrayList<NameValuePair>();
                lname.add(new BasicNameValuePair("username",username));
                lname.add(new BasicNameValuePair("password",password));
                String  res  ="";
                try{
                    HttpClient client = new  DefaultHttpClient();
                    HttpPost post  =  new HttpPost("http://127.0.0.1/login/login.php");
                    post.setEntity(new UrlEncodedFormEntity(lname));
                    HttpResponse response = client.execute(post);
                    HttpEntity entity = response.getEntity();
                    st   =  entity.getContent();
                }catch (Exception e){

                    Log.e(" not Connect","not  connect");
                }
                   try{
                       BufferedReader  reader   =  new BufferedReader(new InputStreamReader(st,"UTF-8"),8);
                       StringBuilder sb =  new StringBuilder();
                       String line  ="";
                       while((line =reader.readLine())!=null){
                           sb.append(line + "\n");
                       }
                       st.close();
                       res = sb.toString();
                   }catch (Exception e1) {
                       Log.e("Exception","Exception");
                   }

                return  res;

            }

    }

        LoginAsyc asyc =  new LoginAsyc();
        asyc.execute(username, password);
    }

    public ActionBarDrawerToggle setupActionBarDrawerToggle(){
        return  new ActionBarDrawerToggle(this,mDrawer,toolbar,R.string.open, R.string.close);
    }
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });

    }
    private void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class framentclass = null;
        switch (menuItem.getItemId()){
                case R.string.Home:
                    new Home();
                    break;
            case R.id.nav_second_fragment:
                break;
            case R.id.nav_third_fragment:
                break;
            default:
        }
         try{
            fragment =(Fragment)framentclass.newInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        dtoggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dtoggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (dtoggle.onOptionsItemSelected(item))
            return true;


        return super.onOptionsItemSelected(item);
    }
}