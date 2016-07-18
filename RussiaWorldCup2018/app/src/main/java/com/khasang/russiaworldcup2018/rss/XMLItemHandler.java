package com.khasang.russiaworldcup2018.rss;

import org.xmlpull.v1.XmlPullParser;

/**
 * Created by aleksandrlihovidov on 18.07.16.
 */
public class XMLItemHandler {
    public RSSItem parseXMLItemWith(XmlPullParser parser) {
        int event;
        String text = null;
        boolean isParse = true;
        RSSItem item = new RSSItem();
        try {
            String name;
            while (isParse) {
                parser.next();
                event = parser.getEventType();
                name = parser.getName();

                switch (event) {
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (name.equals("title")) {
                            item.setTitle(text);
                        } else if (name.equals("link")) {
                            item.setLink(text);
                        } else if (name.equals("description")) {
                            item.setDescription(text);
                        } else if (name.equals("enclosure")) {
                            item.setImageUrl(parser.getAttributeValue(1));
                        } else if (name.equals("item")) {
                            isParse = false;
                        }
                        break;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }
}
