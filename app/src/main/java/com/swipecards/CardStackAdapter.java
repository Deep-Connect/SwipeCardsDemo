package com.swipecards;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.swipecards.R;

import java.util.ArrayList;
import java.util.Random;

public class CardStackAdapter extends BaseAdapter {

    private ArrayList<String> mItems;
    Context mContext;
    LayoutInflater mInflater;
    Random random;
    int max=6;
    int min=0;
    private int[] colorArray={R.color.darkpink,R.color.blue,R.color.green,R.color.yellow,R.color.purple,R.color.orange,R.color.cyan,R.color.wheat};

    public CardStackAdapter(Context context, ArrayList<String> cards) {
        mItems = cards;
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d("cardAdapter", "size is " + mItems.size());
        random = new Random();

    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.swipecard, parent, false);
            holder = new ViewHolder();
            holder.mTvName = (TextView) convertView.findViewById(R.id.helloText);

            holder.cardView = (CardView) convertView.findViewById(R.id.cv);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int colorPos=random.nextInt(max - min + 1) + min;
        convertView.setTag(holder);
        holder.mTvName.setText(mItems.get(position));
        holder.cardView.setCardBackgroundColor(ContextCompat.getColor(mContext, colorArray[colorPos]));
        convertView.setTranslationY(position*12);

        return convertView;
    }

    class ViewHolder {
        TextView mTvName;
        CardView cardView;
    }

}
