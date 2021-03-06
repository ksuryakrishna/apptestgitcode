package com.surya.image_caption_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE=1;
    Button button_add,button_remove,button_cap;
    ListView listView;
    EditText editText_remove,editText_cap;
    public Bitmap[] imageBitmap;
    public String[] caption=null;
    public int i = 0,j=0;

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

        button_cap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               try{
                   caption[i] = editText_cap.getText().toString();
                   editText_cap.setText("");
                   i++;
               }
               catch (NullPointerException e){
                   e.printStackTrace();
               }

            }
        });
        final NewAdapter adapter = new NewAdapter(this,imageBitmap,caption);
        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);

        button_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editText_remove.getText().toString();
                editText_remove.setText("");
                int inp_pos = Integer.parseInt(input);
                int size = listView.getAdapter().getCount();
                if (size>inp_pos){
                    adapter.remove(input);
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
            /*Bundle extras = data.getExtras();
            imageBitmap[j] = (Bitmap) extras.get("data");
            j++;*/
            onCaptureImageResult(data);
        }
    }
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageBitmap[j]=thumbnail;j++;
    }
}

class NewAdapter extends ArrayAdapter<String> {
    public Context context;
    public String[] captiontext;
    public Bitmap[] image;

    NewAdapter(Context c, Bitmap[] imageBitmap, String[] caption) {
        super(c, R.layout.singlerow, R.id.textView2);
        this.context = c;
        this.captiontext = caption;
        this.image = imageBitmap;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        if (row==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.singlerow, parent,false);
        }
        ImageView myimage = (ImageView) row.findViewById(R.id.imageView2);
        TextView mytext = (TextView) row.findViewById(R.id.textView2);

        myimage.setImageBitmap(image[position]);
        mytext.setText(captiontext[position]);


        return row;
    }
}
