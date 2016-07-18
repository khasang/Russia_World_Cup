package com.khasang.russiaworldcup2018.rss;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.khasang.russiaworldcup2018.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by aleksandrlihovidov on 18.07.16.
 */
public class RSSActivity extends AppCompatActivity {
    private static final String TAG = "RSSActivity";

    private String finalUrl="http://www.fifa.com/worldcup/news/rss.xml";//http://www.fifa.com/rss/index.xml";

    private ArrayList<RSSItem> rssItems = new ArrayList<>();
    private RecyclerView recycler;
    private RSSItemListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rss);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        adapter = new RSSItemListAdapter();
        recycler.setAdapter(adapter);

        new HandleAsyncTask().execute(finalUrl);
    }

    private class HandleAsyncTask extends AsyncTask<String, Void, Void> {
        private InputStream stream;
        private HandleXML obj;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            rssItems.clear();
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                stream = conn.getInputStream();
                XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
                XmlPullParser myParser = xmlFactoryObject.newPullParser();
                myParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                myParser.setInput(stream, null);

                obj = new HandleXML(rssItems);
                obj.parseXMLDocumentWith(myParser);
            } catch (Exception e) {
                Toast.makeText(RSSActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }finally {
                try {
                    if (stream != null) {
                        stream.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyDataSetChanged();
        }
    }

    private class RSSItemListAdapter extends RecyclerView.Adapter<RSSItemHolder> {

        @Override
        public RSSItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(RSSActivity.this);
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            return new RSSItemHolder(view);
        }

        @Override
        public void onBindViewHolder(RSSItemHolder holder, int position) {
            RSSItem item = rssItems.get(position);
            holder.titleTextView.setText(item.getTitle());
        }

        @Override
        public int getItemCount() {
            return rssItems.size();
        }
    }

    private class RSSItemHolder extends RecyclerView.ViewHolder{
        TextView titleTextView;

        public RSSItemHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView;
        }
    }
}
