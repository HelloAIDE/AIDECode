package code.aide.dn.com.aidecode.util;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import code.aide.dn.com.aidecode.R;

/**
 * Created by GIGAMOLE on 8/18/16.
 */
public class Utils {

    public static void setupImage(View imageView, final LibraryObject libraryObject) {
        ((ImageView)imageView.findViewById(R.id.select_item_image)).setBackgroundResource(libraryObject.getRes());
        ((TextView)imageView.findViewById(R.id.select_item_title)).setText(libraryObject.getTitle());
    }
    public static class LibraryObject {
        private int res;
        private String title;

        public LibraryObject(int res, String title) {
            this.res = res;
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getRes() {
            return res;
        }
        public void setRes(int res) {
            this.res = res;
        }
    }
}
