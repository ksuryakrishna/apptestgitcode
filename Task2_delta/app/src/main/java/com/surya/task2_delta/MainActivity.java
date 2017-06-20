package com.surya.task2_delta;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE=1;
    Button button_add,button_remove,button_cap;
    ListView listView;
    EditText editText_remove,editText_cap;
    public Bitmap imageBitmap;
    public String caption;
    ArrayList list = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.listView);
        button_add = (Button)findViewById(R.id.button_add);
        button_cap = (Button)findViewById(R.id.button_cap);
        button_remove = (Button)findViewById(R.id.button_remove);
        editText_cap = (EditText) findViewById(R.id.editText_cap);
        editText_remove = (EditText) findViewById(R.id.editText_remove);


        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        final NewAdapter adapter = new NewAdapter(this,imageBitmap,caption,list);
        listView.setAdapter(adapter);

        button_cap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                caption = editText_cap.getText().toString();
                editText_cap.setText("");

            }
        });

        button_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText_remove.getText().toString();
                editText_remove.setText("");
                int inp_pos = Integer.parseInt(input);
                int size = listView.getAdapter().getCount();
                if (size>inp_pos){
                    list.remove(inp_pos);
                    adapter.notifyDataSetChanged();
                }
                else
                    Toast.makeText(MainActivity.this,"No item present in position "+inp_pos,Toast.LENGTH_LONG).show();

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
        }
    }
}
class NewAdapter extends ArrayAdapter<String>{
    Context context;
    String captiontext;
    Bitmap image;
    ArrayList list;
    NewAdapter(Context c,Bitmap imageBitmap,String caption,ArrayList list){
        super(c,R.layout.single_row);
        this.context = c;
        this.captiontext=caption;
        this.image=imageBitmap;
        this.list=list;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.single_row,parent,false);
        ImageView myimage = (ImageView) row.findViewById(R.id.imageView2);
        TextView mytext = (TextView) row.findViewById(R.id.textView2);

        myimage.setImageBitmap(image);
        mytext.setText(captiontext);

        return row;
    }
}