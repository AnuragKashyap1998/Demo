package anurag.myappdemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by anurag on 16-03-2018.
 */

public class AdapterMessage extends ArrayAdapter<Message> {
    private Context context;
    private ArrayList<Message> newMessagelist;
    public AdapterMessage(@NonNull Context context, ArrayList<Message> newMessagelist) {
        super(context, R.layout.messageview,newMessagelist);
        this.newMessagelist=newMessagelist;
        this.context=context;

    }
    static class ListViewHolder
    {
        TextView message;
        TextView username;
        String id;
        ListViewHolder(TextView username,TextView message)
        {
            this.username=username;
            this.message=message;
        }
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView==null)
        {
            convertView= LayoutInflater.from(context).inflate(R.layout.messageview,null);
            TextView username=(TextView)convertView.findViewById(R.id.user);
            TextView message=(TextView) convertView.findViewById(R.id.msg);
            ListViewHolder listViewHolder=new ListViewHolder(username,message);
            convertView.setTag(listViewHolder);
        }
        Message messages=newMessagelist.get(position);
        ListViewHolder listViewHolder=(ListViewHolder) convertView.getTag();
        listViewHolder.username.setText(messages.getUser());
        listViewHolder.message.setText(messages.getMsg());
        return convertView;
    }
}
