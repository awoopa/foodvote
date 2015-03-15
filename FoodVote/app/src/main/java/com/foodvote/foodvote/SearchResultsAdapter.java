package com.foodvote.foodvote;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.foodvote.model.Place;
import com.foodvote.model.PlaceManager;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by Mike Park on 14/03/2015.
 */
public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchResultHolder> {

    public static class SearchResultHolder extends RecyclerView.ViewHolder {
        protected ImageView img;
        protected TextView title;
        protected TextView desc;

        public SearchResultHolder(View v) {
            super(v);
            img = (ImageView) v.findViewById(R.id.place_img);
            title = (TextView) v.findViewById(R.id.results_title);
            desc = (TextView) v.findViewById(R.id.results_content);
        }
    }

    private PlaceManager results;

    public SearchResultsAdapter () {
        this.results = PlaceManager.getInstance();
    }

    @Override
    public int getItemCount() {
        return results.getSize();
    }

    @Override
    public SearchResultHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.results_card, viewGroup, false);
        return new SearchResultHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchResultHolder holder, int i) {
        Place p = results.get(i);
        holder.title.setText(p.getName());
        String descText = makeDescText(p);
        holder.desc.setText(descText);
        Drawable d = getImage(p.getImageURL());
        holder.img.setImageDrawable(d);
    }

    private Drawable getImage(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src");
            return d;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String makeDescText(Place p) {
        String descText = "";
        for (String str : p.getDisplayAddress()) {
            descText = descText + str + ", ";
        }
        if (!p.getDisplayPhone().equals("")) {
            descText = descText + System.getProperty("line.separator") + p.getDisplayPhone();
        }
        if (p.getRating() != 0) {
            Double rating = p.getRating() / 2.0;
            String strRating = String.valueOf(rating);
            descText = descText + System.getProperty("line.separator") + strRating + " out of 5 stars";
        }
        return descText;
    }

    public void refresh() {
        int oldSize = getItemCount();
        this.results = PlaceManager.getInstance();
        int newSize = getItemCount();
        //Must notify of changes
        if (newSize >= oldSize) {                   //if new array size > old array size
            for (int i=0; i<oldSize; i++) {         //for every index that was in old array
                notifyItemChanged(i);
            }
            for (int i=oldSize; i<newSize; i++) {   //for every index that wasn't in old array
                notifyItemInserted(i);
            }
        } else if (newSize < oldSize) {             //if new array is smaller than old array
            for (int i=newSize; i<oldSize; i++) {   //for every index that is no longer in new array
                notifyItemRemoved(i);
            }
        }
    }

}
