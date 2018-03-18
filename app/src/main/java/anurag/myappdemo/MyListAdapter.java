package anurag.myappdemo;

/**
 * Created by anurag on 18-03-2018.
 */

import android.app.Activity;
import android.media.Rating;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Er.Deepak_kumar on 16-03-2018.
 */

public class MyListAdapter extends ArrayAdapter<Guide> {

    private final Activity context;
    private final ArrayList<Guide> guidedetail;
    public MyListAdapter(@NonNull Activity context, ArrayList<Guide>guidedetail) {
        super(context, R.layout.guide_layout,guidedetail);
        this.context=context;
        this.guidedetail=guidedetail;
    }
    public View getView(int position, View view , ViewGroup parent)
    {
        LayoutInflater inflater= context.getLayoutInflater();
        View mview= inflater.inflate(R.layout.guide_layout,null,true);
        TextView name= (TextView)mview.findViewById(R.id.tv_name_guide_layout);
        TextView price= (TextView)mview.findViewById(R.id.tv_price_guide_layout);
        RatingBar rating =(RatingBar) mview.findViewById(R.id.rat_guide_layout);

        name.setText(guidedetail.get(position).getName());
        price.setText(guidedetail.get(position).getPackage_price());
        rating.setRating(guidedetail.get(position).getRating());
        return mview;
    }




}
