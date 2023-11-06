package ma.ensa.volley;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity2 extends AppCompatActivity {
    private DrawerLayout drawerLayout;

    private ImageView menu;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        drawerLayout=findViewById(R.id.drawer_layout);
        TextView home = findViewById(R.id.Home);
        TextView GE = findViewById(R.id.Getudiant);
        TextView GF = findViewById(R.id.Gfiliere);
        TextView GR = findViewById(R.id.Grole);
        TextView AR = findViewById(R.id.Arole);
        TextView AFFE = findViewById(R.id.Afetudiant);

        menu =findViewById(R.id.menu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recreate();
            }
        });

        GE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectACtivity(MainActivity2.this, addStudentActivity.class);
            }
        });
        GF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectACtivity(MainActivity2.this, MainActivity.class);
            }
        });
        GR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectACtivity(MainActivity2.this, addRoleActivity.class);
            }
        });
        AR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectACtivity(MainActivity2.this, AddStudentRoleActivity.class);
            }
        });
        AFFE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectACtivity(MainActivity2.this, ListeStudentFiliereActivity.class);
            }
        });

    }

   public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
   }
   public static void closeDrawer(DrawerLayout drawerLayout){
        if(drawerLayout.isDrawerOpen((GravityCompat.START))){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
   }

   public static void redirectACtivity(Activity activity,Class secondActivity){
        Intent intent=new Intent(activity,secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        activity.finish();

   }
   @Override
    protected void onPause(){
        super.onPause();
        closeDrawer(drawerLayout);
   }


    }


