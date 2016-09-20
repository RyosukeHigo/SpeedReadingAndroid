package com.example.rubik_000.speedreadingandroid;

import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    TextView textView1;
    TextView textView2;
    TextView textView3;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1 = (TextView)findViewById(R.id.textView1);
        textView2 = (TextView)findViewById(R.id.textView2);
        textView3 = (TextView)findViewById(R.id.textView3);
        editText = (EditText)findViewById(R.id.editText);
        final Handler handler= new Handler();
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // マルチスレッドにしたい処理 ここから
                        final ArrayList<String> TextList = YahooAPI();
                        //final String result = getMessage(); // 何かの処理
                        for(final String s:TextList){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                    textView1.setText(s); // 画面に描画する処理
                            }
                        });
                        try {
                            Thread.sleep(500);
                        }catch (Exception ex){
                            System.out.println(ex);
                        }
                    }

                        // マルチスレッドにしたい処理 ここまで
                    }
                }).start();
            }


        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    public ArrayList<String> YahooAPI(){
        //Log.d("OK","OKOKOKOKO");
        ArrayList<String> result = new ArrayList<String>();
        final String BASE_URI = "http://jlp.yahooapis.jp/DAService/V1/parse";
        final String APP_ID = "dj0zaiZpPXY3U0syYkg4eXR5cSZzPWNvbnN1bWVyc2VjcmV0Jng9MDg-";
        String text = editText.getText().toString().replaceAll("\n","");
        try {
            text = URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            URL url = new URL(BASE_URI+"?appid="+APP_ID+"&sentence="+text);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            //String str = InputStreamToString(con.getInputStream());
//            XmlPullParser xmlPullParser = Xml.newPullParser();
//            xmlPullParser.setInput(con.getInputStream(), "UTF-8");
            //Log.d("TEST",str);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(con.getInputStream(),"UTF-8");
            int eventType = xmlPullParser.getEventType();
            boolean isChunk = false;
            boolean isSurface = false;
            String tmp = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_DOCUMENT) {
                    System.out.println("Start document");
                } else if(eventType == XmlPullParser.START_TAG) {
                    if(xmlPullParser.getName().equals("Chunk")){
                        isChunk = true;
                    }else if(xmlPullParser.getName().equals("Surface")){
                        isSurface = true;
                    }
                    System.out.println("Start tag "+xmlPullParser.getName());
                } else if(eventType == XmlPullParser.END_TAG) {
                    if(xmlPullParser.getName().equals("Chunk")){
                        isChunk = false;
                        result.add(tmp);
                        tmp = "";
                    }else if(xmlPullParser.getName().equals("Surface")){
                        isSurface = false;
                    }
                    System.out.println("End tag "+xmlPullParser.getName());
                } else if(eventType == XmlPullParser.TEXT) {
                    if(isChunk&&isSurface){
                        tmp = tmp + xmlPullParser.getText();
                    }
                    System.out.println("Text "+xmlPullParser.getText());
                }
                eventType = xmlPullParser.next();//increment
            }
        }catch (Exception ex) {
            System.out.println(ex);
        }

       return result;
    }
    static String InputStreamToString(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
