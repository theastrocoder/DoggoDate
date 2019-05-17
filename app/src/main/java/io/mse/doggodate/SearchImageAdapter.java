package io.mse.doggodate;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;

import java.util.ArrayList;

import io.mse.doggodate.Entity.Doggo;

public class SearchImageAdapter extends BaseAdapter {
    // Keep all Images in array
    public ArrayList<Doggo> doggos = new ArrayList<>();

    private MainActivity activity;

    private Context mContext;

    // Constructor
    public SearchImageAdapter(Context c, MainActivity activity) {
        mContext = c;
        this.activity = activity;
        doggos.addAll(activity.getDefaultSearch());
    }

    @Override
    public int getCount() {
        return doggos.size();
    }

    @Override
    public Object getItem(int position) {
        return doggos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


//        ImageView imageView = new ImageView(mContext);
//        imageView.setImageResource(mThumbIds[position]);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
        ViewHolderItem viewHolder;


        if (convertView == null) {

            // inflate the layout
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.row_grid_search_indicator, parent, false);

            // well set up the ViewHolder
            viewHolder = new ViewHolderItem();

            viewHolder.imageViewItem = (ImageView) convertView.findViewById(R.id.imageView_indicator);
            viewHolder.textViewItem = (TextView) convertView.findViewById(R.id.textView_indicator);

            // store the holder with the view.
            convertView.setTag(viewHolder);


        } else {
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        // object item based on the position

        // get the TextView from the ViewHolder and then set the text (item name) and tag (item ID) values
        viewHolder.textViewItem.setText(doggos.get(position).getName() +" "  +((activity.getActiveDog().getFollowings().contains(doggos.get(position)))? "": "+"));
        viewHolder.textViewItem.setTag(position);
        viewHolder.textViewItem.setBackgroundColor(((Activity)activity).getResources().getColor((activity.getActiveDog().getFollowings().contains(doggos.get(position)))?R.color.colorPrimaryLight:R.color.colorPrimaryLighter));

       viewHolder.imageViewItem.setImageResource(doggos.get(position).getProfilePic());


        return convertView;

    }

    // our ViewHolder.
// caches our TextView
    static class ViewHolderItem {
        TextView textViewItem;
        ImageView imageViewItem;
    }


}