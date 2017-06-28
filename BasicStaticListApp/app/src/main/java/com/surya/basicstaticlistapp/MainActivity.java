package com.surya.basicstaticlistapp;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String[] titles;
    int[] images = {R.drawable.image1,R.drawable.image2,R.drawable.image3,R.drawable.image4,R.drawable.image5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources res = getResources();
        titles = res.getStringArray(R.array.titles);

        listView = (ListView) findViewById(R.id.listview);
        Newadapter adapter = new Newadapter(this,R.layout.single_row,titles,images);
        listView.setAdapter(adapter);

    }
}

class Newadapter extends ArrayAdapter<String>{

    Context c;
    int[] images;
    String[] titles;
    public Newadapter(@NonNull Context context, @LayoutRes int resource, String[] titles, int images[]) {
        super(context, resource, R.id.textView,titles);
        this.c=context;
        this.images=images;
        this.titles = titles;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.single_row,parent,false);
        TextView myText = (TextView) row.findViewById(R.id.textView);
        ImageView myImage = (ImageView) row.findViewById(R.id.imageView);
        myImage.setImageResource(images[position]);
        myText.setText(titles[position]);
        return row;
    }
}