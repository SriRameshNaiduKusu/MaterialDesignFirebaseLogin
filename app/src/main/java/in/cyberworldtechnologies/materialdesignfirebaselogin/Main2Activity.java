package in.cyberworldtechnologies.materialdesignfirebaselogin;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private TextView textView;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main2 );
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );


        textView = findViewById( R.id.welname );

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference( firebaseAuth.getUid() );


        databaseReference.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue( UserProfile.class );
                textView.setText( "Welcome " + userProfile.getUserName() );

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText( Main2Activity.this, databaseError.getCode(), Toast.LENGTH_SHORT ).show();
            }
        } );



        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.addDrawerListener( toggle );
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener( this );
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        if (drawer.isDrawerOpen( GravityCompat.START )) {
            drawer.closeDrawer( GravityCompat.START );
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.main, menu );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected( item );
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (item.getItemId()) {

            case R.id.nav_profile:
                startActivity( new Intent( Main2Activity.this, ProfileActivity.class ) );
                break;

            case R.id.nav_logout:
                Logout();

                break;


            case R.id.nav_share:
                Intent i = new Intent( Intent.ACTION_SEND );

                i.addFlags( Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK );

                try {


                    i.setType( "text/plain" );
                    i.putExtra( Intent.EXTRA_SUBJECT, "Email Login" );
                    String sAux = "Hey! Look at our App. Download it Free Now!\n\n";
                    sAux = sAux + "http://play.google.com/store/apps/details?id=" + getPackageName();
                    i.putExtra( Intent.EXTRA_TEXT, sAux );
                    startActivity( Intent.createChooser( i, "choose one" ) );
                } catch (Exception e) {
                    //e.toString();
                }
                break;

            case R.id.nav_rateus:

                Uri uri = Uri.parse( "market://details?id=" + getPackageName() );
                Intent goToMarket = new Intent( Intent.ACTION_VIEW, uri );
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags( Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK );
                try {
                    startActivity( goToMarket );
                } catch (ActivityNotFoundException e) {
                    startActivity( new Intent( Intent.ACTION_VIEW,
                            Uri.parse( "http://play.google.com/store/apps/details?id=" + getPackageName() ) ) );
                }

                break;

        }

            DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
            drawer.closeDrawer( GravityCompat.START );
            return true;
        }







    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(Main2Activity.this, MainActivity.class));
    }
}
