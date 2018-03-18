package anurag.myappdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    FirebaseAuth mAuth;
    private static String TAG="MESS";
    public static  final int Default_Msg_Limit=1000;
    public static final int RC_SIGN_IN = 1;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private  TabLayout tabLayout;
    private ViewPager mViewPager;
    static CoordinatorLayout coordinatorLayout;
    FirebaseUser user;
    public FirebaseDatabase f;
    public DatabaseReference d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        coordinatorLayout=(CoordinatorLayout) findViewById(R.id.main_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mAuth = FirebaseAuth.getInstance();
        
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.i(TAG,"second point");
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(MainActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();

                } else {

                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder().setIsSmartLockEnabled(false)
                                    .setProviders(
                                            AuthUI.EMAIL_PROVIDER,
                                            AuthUI.GOOGLE_PROVIDER
                                    )
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
        //f=FirebaseDatabase.getInstance();
        //it finds the node chat if it is note present then database makes it automatically
       // d=f.getReference("Chat");

    }

    public FirebaseUser getFirebaseUser() {
        return user;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mAuthListener!=null){
            mAuth.removeAuthStateListener(mAuthListener);

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(MainActivity.this, "Signed in", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(MainActivity.this, "Not Signed in", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        if(requestCode==1)
        {
            if(resultCode==RESULT_OK)
            {
                user=getFirebaseUser();
                f = FirebaseDatabase.getInstance();
                String lang=data.getStringExtra("lang");
                d=f.getReference("language");
                if(user!=null) {
                    d.child(user.getUid()).child("lang").setValue(lang.substring(0,2));
                }
                SharedPreferences sharedPreferences=getSharedPreferences("MyappDemo",MODE_PRIVATE);
                String namelang=sharedPreferences.getString("language",null);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                if(lang!=null) {
                    editor.putString("language", lang.substring(0, 2));
                }
                else
                {
                    editor.putString("language","en");
                }
                    editor.commit();
            }
        }
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent i=new Intent(MainActivity.this,SelectLanguage.class);
            startActivityForResult(i,1);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public FirebaseDatabase f;
        public DatabaseReference chat;
        ValueEventListener a, b;
        public static final int Default_Msg_Limit = 1000;
        public static final int RC_SIGN_IN = 1;
        FirebaseAuth.AuthStateListener mauthstatelistener;
        FirebaseUser user;
        DatabaseReference users;
        DatabaseReference group;
        ListView mylist;
        AdapterList adapterList;
        static ArrayList<Details> details;

        private static final String ARG_SECTION_NUMBER = "section_number";
        private FirebaseAuth mfirebaseAuth;

        Places_DatabaseHelper db;
        Button create_btn,search_btn;
        ListView placeslist;
        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            mfirebaseAuth = FirebaseAuth.getInstance();
            f = FirebaseDatabase.getInstance();
            chat = f.getReference("Chat");
            users = f.getReference("users");
            group = f.getReference("Group Chat");
            user = ((MainActivity) getActivity()).getFirebaseUser();
            //for finding the current name of user

            Bundle b = getArguments();
            details = new ArrayList<>();
            adapterList = new AdapterList(getActivity(), details);

            int selectionNumber = b.getInt(ARG_SECTION_NUMBER);
            //Important section
            if (selectionNumber == 1) {
                View rootView = inflater.inflate(R.layout.fragment_places_list, container, false);
                create_btn=rootView.findViewById(R.id.btn_create_places);
                search_btn=rootView.findViewById(R.id.btn_search_places);
                placeslist=rootView.findViewById(R.id.lv_place_places);

                create_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getActivity(),CreatePlaceActivity.class);
                        startActivity(i);
                    }
                });

                db = new Places_DatabaseHelper(getActivity());

                final ArrayList<Places> placesdetail= db.getAllPlaces();
                final MyPlacesAdapter adapter= new MyPlacesAdapter(getActivity(),placesdetail);
                placeslist.setAdapter(adapter);

                placeslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                       Intent intent = new Intent(getActivity(),PlaceDetailActivity.class);
                        intent.putExtra("PLACESDETAILS", (Serializable)adapterView.getItemAtPosition(i));
                         intent.putExtra("PLACESDETAILS", (Serializable) placesdetail.get(i));

                        startActivity(intent);
                    }
                });
                return rootView;
            }

            if (user != null) {
                Log.e(TAG, "onAuthStateChanged:signed_in" + user.getUid());

            } else { //user is not logged in

                Log.e(TAG, "onAuthStateChanged:signed_out");

            }
            if (selectionNumber == 2) {
                adapterList.notifyDataSetChanged();
                // Toast.makeText(getContext(), mfirebaseAuth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
                View rootView = inflater.inflate(R.layout.fragment_main, container, false);
                mylist = (ListView) rootView.findViewById(R.id.mylist);
                mylist.setAdapter(adapterList);
                mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent i1 = new Intent(getActivity(), Inbox.class);
                        i1.putExtra("UserNames", details.get(i).getName());
                        i1.putExtra("FireAuth", mfirebaseAuth.getCurrentUser().getDisplayName());
                        startActivity(i1);
                    }
                });
                return rootView;

            } else {
                View rootView = inflater.inflate(R.layout.fragment_main, container, false);
                return rootView;
            }
        }
        @Override
        public void onPause() {
            super.onPause();
            if(a!=null){
                mfirebaseAuth.removeAuthStateListener(mauthstatelistener);
                detach();
                details.clear();
            }
        }
        @Override
        public void onResume() {
            super.onResume();

        }
        private  void detach() {
            if (a != null) {
                users.removeEventListener(a);
            }
            a=null;
        }
        @Override
        public void onStart() {
            super.onStart();
            if(user!=null) {
                users.child(user.getUid()).child("name").setValue(user.getDisplayName());

            }
            a=users.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    details.clear();
                    for(DataSnapshot mysnapshot: dataSnapshot.getChildren()){
                        Details detail=mysnapshot.getValue(Details.class);

                        details.add(detail);

                        Log.i(TAG,detail.getName());
                    }
                    adapterList.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}
