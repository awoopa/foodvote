package com.foodvote.foodvote;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.foodvote.model.Room;

import java.util.List;

/**
 * Created by Mike Park on 14/03/2015.
 */
public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.RoomListHolder> {

    public static class RoomListHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected TextView status;
        protected TextView result;

        public RoomListHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.voteName);
            status = (TextView) v.findViewById(R.id.voteStatus);
            result = (TextView) v.findViewById(R.id.voteResult);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("RecyclerView", "onClick：" + getPosition());
                }
            });
        }

    }

    private List<Room> roomList;

    public void add(Room room) {
        roomList.add(room);
        notifyItemInserted(roomList.size()-1);
    }

    public void remove(Room room) {
        int position = roomList.indexOf(room);
        roomList.remove(position);
        notifyItemRemoved(position);
    }

    public RoomListAdapter(List<Room> roomList) {
        this.roomList = roomList;
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    @Override
    //Create new item in list
    public RoomListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_list_item, parent, false);
        return new RoomListHolder(v);
    }

    @Override
    //Replace contents of list item
    public void onBindViewHolder(RoomListHolder holder, int position) {
        Room r = roomList.get(position);
        holder.name.setText(r.getName());
        if (r.getCompleted()) {
            holder.status.setText("Completed");
            holder.result.setText(r.getChosenPlace().getName());
        } else {
            holder.status.setText("Ongoing");
        }

    }
}
