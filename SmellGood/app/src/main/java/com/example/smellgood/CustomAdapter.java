package com.example.smellgood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
    private Context context;
    private final String[] values;

    public CustomAdapter(Context context, String[] values) {
        this.context = context;
        this.values = values;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {
            gridView = new View(context);

            gridView = inflater.inflate(R.layout.scoreboardperson, null);

            TextView textView = (TextView) gridView.findViewById(R.id.grid_item_label);
            textView.setText(values[position]);

            ImageView imageView = (ImageView) gridView.findViewById(R.id.grid_item_image);

            String mobile = values[position];

            if (position < 1) {
                imageView.setImageResource(R.drawable.goldmedal);
            }if (position < 3 && position > 0) {
                imageView.setImageResource(R.drawable.silvermedal);
            }if (position < 10 && position > 3) {
                imageView.setImageResource(R.drawable.bronzemedal);
            }

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
}
