package anurag.myappdemo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Inbox extends AppCompatActivity {

    String text;
    private static final int Default_Msg_Limit=1000;
    FirebaseAuth mfirebaseauth;
    FirebaseDatabase f;
    String frndname;
    String name2;
    ListView mylistView;
    DatabaseReference d;
    DatabaseReference langRef;
    ArrayList<Message> myMessageList;
    DatabaseReference df;
    FloatingActionButton msgbtn;
    EditText mytext;
    String msg;
    AdapterMessage adapterMessage;
    ValueEventListener a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        Intent i=getIntent();
        frndname=i.getStringExtra("UserNames");
        name2=i.getStringExtra("FireAuth");
        f=FirebaseDatabase.getInstance();
        langRef=f.getReference("language");
        d=f.getReference(frndname+name2);
        df=f.getReference(name2+frndname);
        mylistView=(ListView)findViewById(R.id.privatemessage);
        myMessageList=new ArrayList<>();
        adapterMessage=new AdapterMessage(this,myMessageList);
        mylistView.setAdapter(adapterMessage);
        msgbtn=(FloatingActionButton) findViewById(R.id.msgbtn);
        mytext=(EditText)findViewById(R.id.mytext);
        mytext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    msgbtn.setEnabled(true);
                } else {
                    msgbtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mytext.setFilters(new InputFilter[]{new InputFilter.LengthFilter(Default_Msg_Limit)});


        msgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMessageList.clear();
                fetchCourses(mytext.getText().toString());
                String messages = mytext.getText().toString().trim();

                String id = d.push().getKey();

                Message message = new Message(id,messages,name2,"Anonymus");
                d.child(id).setValue(message);
                mytext.setText("");
            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



    }
    private void fetchCourses(String useText)
    {
        Log.i("MESS","fetch");
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://translate.yandex.net/").addConverterFactory(GsonConverterFactory.create()).build();
        ApiInterface apiInterface=retrofit.create(ApiInterface.class);

        SharedPreferences sharedPreferences=getSharedPreferences("MyappDemo",MODE_PRIVATE);
        String namelang=sharedPreferences.getString("language",null);
        Call<Data> call;
        if(namelang==null) {
            call=apiInterface.getda(useText);
        }
        else if(namelang.equals("en"))
        {
            call=apiInterface.getda(useText);
        }
        else
        {
            call=apiInterface.getde(useText);
        }
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                Log.i("MESS","in");
                Data data=response.body();
                text=data.gettext();
                String id2=df.push().getKey();
                String id = d.push().getKey();
                Message message = new Message(id,text,name2,"Anonymus");
                df.child(id2).setValue(message);
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(a==null){
            ValueEventListener a= d.addValueEventListener(new ValueEventListener() {
                @Override

                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot mysnapshot : dataSnapshot.getChildren()) {

                        Message message = mysnapshot.getValue(Message.class);

                        myMessageList.add(message);
                    }
                    adapterMessage.notifyDataSetChanged();
                    scrollMyListViewToBottom();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
    private void scrollMyListViewToBottom() {
        mylistView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                mylistView.setSelection(adapterMessage.getCount() - 1);
            }
        });
    }
}
