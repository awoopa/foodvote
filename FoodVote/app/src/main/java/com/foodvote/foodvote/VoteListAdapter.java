package com.foodvote.foodvote;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.footvote.foodvote.R;

/**
 * Created by Mike Park on 14/03/2015.
 */
public class VoteListAdapter extends RecyclerView.Adapter<VoteListHolder> {

    public static class VoteListHolder extends RecyclerView.ViewHolder {
        protected TextView Name;
        protected TextView Status;
        protected TextView Result;

        public VoteListHolder(View v) {
            super(v);
            Name = (TextView) v.findViewById(R.id.voteName);
            Status = (TextView) v.findViewById(R.id.voteStatus);
            Result = (TextView) v.findViewById(R.id.voteResult);
        }
    }



}
