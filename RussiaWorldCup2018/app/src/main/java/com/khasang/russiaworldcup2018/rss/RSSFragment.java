package com.khasang.russiaworldcup2018.rss;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.khasang.russiaworldcup2018.R;
import com.khasang.russiaworldcup2018.ThumbnailDownloader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by aleksandrlihovidov on 20.07.16.
 */
public class RSSFragment extends Fragment {
    private static final String TAG = "RSSFragment";

    private String finalUrl="http://www.fifa.com/worldcup/news/rss.xml";//http://www.fifa.com/rss/index.xml";

    private ArrayList<RSSItem> rssItems = new ArrayList<>();
    private RecyclerView recycler;
    private RSSItemListAdapter adapter;

    private ThumbnailDownloader<RSSItemHolder> thumbnailDownloader;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        new HandleAsyncTask().execute(finalUrl);

        Handler responseHandler = new Handler();
        thumbnailDownloader = new ThumbnailDownloader<>(responseHandler);
        thumbnailDownloader.setThumbnailDownloaderListener(
                new ThumbnailDownloader.ThumbnailDownloaderListener<RSSItemHolder>() {
            @Override
            public void onThumbnailDownloaded(RSSItemHolder rssItemHolder, Bitmap thumbnail) {
                Drawable drawable = new BitmapDrawable(getResources(), thumbnail);
                rssItemHolder.rssImageView.setImageDrawable(drawable);
            }
        });
        thumbnailDownloader.start();
        thumbnailDownloader.getLooper();
        Log.i(TAG, "onCreate: background thread started");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rss, container, false);

        recycler = (RecyclerView) view.findViewById(R.id.recycler);
        recycler.setLayoutManager(new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false));
        adapter = new RSSItemListAdapter();
        recycler.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        thumbnailDownloader.clearQueue();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        thumbnailDownloader.quit();
        Log.i(TAG, "onDestroy: background thread destroyed");
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
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.rss_list_item, parent, false);
            return new RSSItemHolder(view);
        }

        @Override
        public void onBindViewHolder(RSSItemHolder holder, int position) {
            RSSItem item = rssItems.get(position);
            holder.titleTextView.setText(item.getTitle());
            if (item.getImageUrl() == null || item.getImageUrl().isEmpty()) {
                holder.rssImageView.setImageResource(R.mipmap.ic_launcher);
            } else {
                try {
                    URL url = new URL(item.getImageUrl());
                    thumbnailDownloader.queueThumbnail(holder, item.getImageUrl());
                } catch (MalformedURLException e) {
                    holder.rssImageView.setImageResource(R.mipmap.ic_launcher);
                }
            }
        }

        @Override
        public int getItemCount() {
            return rssItems.size();
        }
    }

    private class RSSItemHolder extends RecyclerView.ViewHolder{
        ImageView rssImageView;
        TextView titleTextView;

        public RSSItemHolder(View itemView) {
            super(itemView);
            rssImageView = (ImageView) itemView.findViewById(R.id.image_view_rss_item);
            titleTextView = (TextView) itemView.findViewById(R.id.text_view_rss_item);
        }
    }
}
