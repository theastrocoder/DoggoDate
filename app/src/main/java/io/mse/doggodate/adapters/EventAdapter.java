package io.mse.doggodate.adapters;



import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.List;

import io.mse.doggodate.entity.DoggoEvent;
import io.mse.doggodate.R;
import io.mse.doggodate.entity.DoggoZone;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(DoggoEvent item);
    }


    private final OnItemClickListener listener;

    private List<DoggoZone> zoneList;

    public  EventAdapter(List<DoggoZone> zoneList, OnItemClickListener listener) {
        this.zoneList = zoneList;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return zoneList.size();
    }

    @Override
    public void onBindViewHolder(EventViewHolder eventViewHolder, int i) {
        DoggoZone zone = zoneList.get(i);
        Log.i("ADAPTER","Zone " + zone.getName() + " list " +zone.getEventList());
        eventViewHolder.zone.setText(zone.getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");

        DoggoEvent event =  zone.getEventList().get(0);
        String formatDateTime = event.getTime().format(formatter);
        eventViewHolder.creator.setText(event.getCreator().getName() + " is going for a walk at " + formatDateTime);
        eventViewHolder.others.setText(zone.getEventList().size() + " others are joining.");

        eventViewHolder.bind(event, listener);

    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.view_newsfeed, viewGroup, false);

        return new EventViewHolder(itemView);
    }


    public static class EventViewHolder extends RecyclerView.ViewHolder {
        protected TextView zone;
        protected TextView creator;
        protected TextView others;

        public EventViewHolder(View v) {
            super(v);
            zone = (TextView) v.findViewById(R.id.dogzone);
            creator = (TextView) v.findViewById(R.id.creator);
            others = (TextView) v.findViewById(R.id.others);
        }

        public void bind(final DoggoEvent item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });

        }
    }
}