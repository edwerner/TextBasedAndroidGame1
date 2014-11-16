package com.movie.locations.adapter;

import android.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.movie.locations.domain.FilmLocation;

// here's our beautiful adapter
public class ArrayAdapterItem extends ArrayAdapter<FilmLocation> {

    Context mContext;
    int layoutResourceId;
    FilmLocation data[] = null;

    public ArrayAdapterItem(Context mContext, int layoutResourceId, FilmLocation[] data) {

        super(mContext, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        /*
         * The convertView argument is essentially a "ScrapView" as described is Lucas post 
         * http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
         * It will have a non-null value when ListView is asking you recycle the row layout. 
         * So, when convertView is not null, you should simply update its contents instead of inflating a new row layout.
         */
        if(convertView==null){
            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        // object item based on the position
        FilmLocation objectItem = data[position];

        // get the TextView and then set the text (item name) and tag (item ID) values
        TextView textViewItem1 = (TextView) convertView.findViewById(R.id.text1);
        TextView textViewItem2 = (TextView) convertView.findViewById(R.id.text2);
        textViewItem1.setText(objectItem.getTitle());
        textViewItem2.setTag(objectItem.getDirector());

        return convertView;

    }

}