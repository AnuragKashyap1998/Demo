package anurag.myappdemo;

/**
 * Created by anurag on 18-03-2018.
 */

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;

//  import com.bumptech.glide.Glide;

public class ImageAdapter extends PagerAdapter {
    Context mContext;
    private String[] sliderImageurl=new String[3];
    ImageAdapter(Context context,String[] url)
    {
        this.mContext=context;
        this.sliderImageurl=url;
    }

    @Override
    public int getCount() {
        return sliderImageurl.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==(ImageView)object;
    }

    public Object instantiateItem(ViewGroup container,int position)
    {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //  imageView.setImageResource(sliderImageurl[position]);
        // Glide.with(mContext).load(sliderImageurl[position]).into(imageView);
        try {
            Picasso.with(mContext).load(String.valueOf(new URL(sliderImageurl[position]))).resize(350,250).into(imageView);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ((ViewPager)container).addView(imageView,0);


        return imageView;
    }

    public void destroyItem(ViewGroup container,int position, Object object)
    {
        ((ViewPager)container).removeView((ImageView)object);
    }


}
