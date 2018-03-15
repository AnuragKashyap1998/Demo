package anurag.myappdemo;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by anurag on 15-03-2018.
 */

public class AdapterList extends ArrayAdapter<Details> {
    private Activity context;
    private ArrayList<Details> newlist;
    public AdapterList(Activity context, ArrayList<Details> newlist) {
        super(context,R.layout.username,newlist);
        this.context=context;
        this.newlist=newlist;
    }

    static class ListViewHolder
    {
        TextView username;
        ListViewHolder(TextView username)
        {
            this.username=username;
        }
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(R.layout.username,null);
            TextView username=(TextView)convertView.findViewById(R.id.name);
            ListViewHolder listViewHolder=new ListViewHolder(username);
            convertView.setTag(listViewHolder);
        }
        Details details=newlist.get(position);
        ListViewHolder listViewHolder=(ListViewHolder) convertView.getTag();
        listViewHolder.username.setText(details.getName());
        return convertView;

    }
}
