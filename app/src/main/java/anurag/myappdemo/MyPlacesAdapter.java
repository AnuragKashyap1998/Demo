package anurag.myappdemo;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by anurag on 18-03-2018.
 */

public class MyPlacesAdapter extends ArrayAdapter<Places> {

    private final Activity context;
    private final ArrayList<Places> placelist;
    public MyPlacesAdapter(@NonNull Activity context, ArrayList<Places>placelist) {
        super(context, R.layout.place_layout,placelist);
        this.context=context;
        this.placelist=placelist;
    }
    public View getView(int position, View view , ViewGroup parent)
    {
        LayoutInflater inflater= context.getLayoutInflater();
        View mview= inflater.inflate(R.layout.place_layout,null,true);
        TextView name= (TextView)mview.findViewById(R.id.tv_name_place_layout);
        TextView place= (TextView)mview.findViewById(R.id.tv_place_place_layout);
        ImageView image =(ImageView)mview.findViewById(R.id.iv_img_place_layout);
        name.setText(placelist.get(position).getName());
        place.setText(placelist.get(position).getPlace());
        try {
            Picasso.with(getContext()).load(String.valueOf(new URL(placelist.get(position).getUrl1()))).resize(150,100).into(image);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return mview;
    }




}
