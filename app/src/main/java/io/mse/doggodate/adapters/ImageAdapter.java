package io.mse.doggodate.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import io.mse.doggodate.R;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private AppCompatActivity activity;
    // Keep all Images in array
    public Integer[] mThumbIds = {
            R.drawable.profile_image, R.drawable.dog2,
            R.drawable.dog3, R.drawable.dog4,
            R.drawable.dog3, R.drawable.dog4,
            R.drawable.dog3, R.drawable.dog4,
            R.drawable.profile_image, R.drawable.dog2,
            R.drawable.dog3, R.drawable.dog4,
            R.drawable.profile_image, R.drawable.dog2,
            R.drawable.dog3, R.drawable.dog4,
            R.drawable.profile_image, R.drawable.dog2,
            R.drawable.dog3, R.drawable.dog4,
            R.drawable.profile_image, R.drawable.dog2,
            R.drawable.dog3, R.drawable.dog4,

    };

    private ArrayList<Integer> photos;

    // Constructor
    public ImageAdapter(Context c, AppCompatActivity activity, ArrayList<Integer> photos){
        mContext = c;
        this.activity = activity;
        this.photos = photos;
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public Object getItem(int position) {
        return photos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource(photos.get(position));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        imageView.setLayoutParams(new GridView.LayoutParams( activity.getWindowManager().getDefaultDisplay().getWidth()/4,activity.getWindowManager().getDefaultDisplay().getWidth()/4 ));

        return imageView;
    }

}