package com.khasang.russiaworldcup2018.rss;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

/**
 * Created by aleksandrlihovidov on 18.07.16.
 */
public class HandleXML {
    private ArrayList<RSSItem> mItems;

    public HandleXML(ArrayList<RSSItem> items) {
        mItems = items;
    }

    public void parseXMLDocumentWith(XmlPullParser myParser) {
        int event;
        try {
            event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name=myParser.getName();

                switch (event){
                    case XmlPullParser.START_TAG:
                        if (name.equals("item")) {
                            mItems.add(new XMLItemHandler().parseXMLItemWith(myParser));
                        }
                        break;
                }
                event = myParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
