package com.aashishgodambe.android.showstores.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aashishgodambe.android.showstores.Model.Item;
import com.aashishgodambe.android.showstores.R;

import java.util.List;

/**
 * Created by Aashish on 1/19/2016.
 */
public class AdapterItem extends RecyclerView.Adapter<AdapterItem.ViewHolderItem> {

    private List<Item> itemList;
    private Context mContext;
    private final String LOG_TAG = AdapterItem.class.getSimpleName();

    // Constructor to initialize data fed to adapter
    public AdapterItem(List<Item> itemList,Context context) {
        this.itemList = itemList;
        mContext = context;
        Log.v(LOG_TAG, "Constructor created list items = " + itemList.size());
    }

    //
    @Override
    public ViewHolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.v(LOG_TAG, "onCreateViewHolder called. ");

        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);

        return new ViewHolderItem(row);
    }

    // Data is attached to individual item
    @Override
    public void onBindViewHolder(ViewHolderItem holder, int position) {
        Log.v(LOG_TAG, "onBindViewHolder called with pos = " + position);

        Item currentItem = itemList.get(position);
        holder.itemStyle.setText("Style : "+currentItem.getStyle().toUpperCase());
        holder.itemLat.setText("Latitude : "+currentItem.getLatitude());
        holder.itemLong.setText("Longitude : "+currentItem.getLongitude());
        holder.itemDetail.setText(" " + currentItem.getDetails());

        // If the style is paperbox, set red as the item color.If style is starbucks, set it as green.
        if(currentItem.getStyle().equals("paperbox")){
            holder.itemlayout.setBackgroundColor(mContext.getResources().getColor(R.color.colorGreen));
        }else if(currentItem.getStyle().equals("starbucks")){
            holder.itemlayout.setBackgroundColor(mContext.getResources().getColor(R.color.colorRed));
        }

        Log.v("AdapterItem", position + "");

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // Configure the views.
    static class ViewHolderItem extends RecyclerView.ViewHolder{

        private TextView itemLong;
        private TextView itemLat;
        private TextView itemDetail;
        private TextView itemStyle;
        private LinearLayout itemlayout;

        public ViewHolderItem(View itemView) {
            super(itemView);

            itemDetail = (TextView) itemView.findViewById(R.id.textview_details);
            itemStyle = (TextView) itemView.findViewById(R.id.textview_style);
            itemLat = (TextView) itemView.findViewById(R.id.textview_latitude);
            itemLong = (TextView) itemView.findViewById(R.id.textview_longitude);
            itemlayout = (LinearLayout) itemView.findViewById(R.id.list_layout);

        }
    }
}
