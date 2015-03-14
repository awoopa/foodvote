package com.foodvote.foodvote;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.foodvote.model.User;

import java.util.List;

/**
 * Created by Mike Park on 14/03/2015.
 */
public class UserListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private List<User> userList;

    public UserListAdapter(List<User> userList) {
        this.userList = userList;
    }

    @Override
    // all the users + 2 headings
    public int getItemCount() {
        return userList.size() + 2;
    }

    @Override
    public int getItemViewType(int pos) {
        if(isHeader(pos))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    //headers should always be at pos 0 and 2
    private boolean isHeader(int pos) {
        return pos == 0 || pos == 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_header, parent, false);
            return new ULHeader(v);
        } else if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
            return new ULUser(v);
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure you're using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int pos) {
        if (holder instanceof ULHeader) {
            if (pos == 0)
                ((ULHeader) holder).title.setText("Host");
            else if (pos == 2)
                ((ULHeader) holder).title.setText("Users");
        } else if (holder instanceof ULUser) {
            if (pos == 1) {
                User u = userList.get(0);
                ((ULUser) holder).name.setText(u.getName());
            } else {
                User u = userList.get(pos - 2);
                ((ULUser) holder).name.setText(u.getName());
            }
        }
    }

    public static class ULHeader extends RecyclerView.ViewHolder {
        TextView title;

        public ULHeader(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.user_header);
        }
    }

    public static class ULUser extends RecyclerView.ViewHolder {
        TextView name;

        public ULUser(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.user_name);
        }
    }
}
