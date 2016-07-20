package com.example.hanshu.netpicture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Button find;
    EditText editText;
    ImageView imageView;

    int CHAGE=1;
    int ERROR=2;
    private static final String TAG = "MainActivity";
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==CHAGE){
                Bitmap bitmap= (Bitmap) msg.obj;
                imageView.setImageBitmap(bitmap);
            }else if(msg.what==ERROR){
                Toast.makeText(MainActivity.this,"获取图片失败",Toast.LENGTH_SHORT).show();
            }
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        find= (Button) findViewById(R.id.bt);
        editText= (EditText) findViewById(R.id.et);
        imageView= (ImageView) findViewById(R.id.iv);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            String path=editText.getText().toString().trim();
                            URL url=new URL(path);
                            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("GET");
                            connection.setConnectTimeout(5000);
                            connection.setRequestProperty("User-Agent",
                                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; " +
                                            "SV1; .NET4.0C; .NET4.0E; .NET CLR 2.0.50727; " +
                                            ".NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; Shuame)");
                            Log.i(TAG, "run: 11111111111111111111111111111111111111111");
                            int code=connection.getResponseCode();
                            if(code==200){
                                InputStream in=connection.getInputStream();
                                Bitmap bitmap=BitmapFactory.decodeStream(in);
                                Message message=new Message();
                                message.what=CHAGE;
                                message.obj=bitmap;
                                handler.sendMessage(message);

                            }else{
                                Message message=new Message();
                                message.what=ERROR;
                                handler.sendMessage(message);


                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Message message=new Message();
                            message.what=ERROR;
                            handler.sendMessage(message);

                        }
                    };
                }.start();

            }
        });
    }
}
