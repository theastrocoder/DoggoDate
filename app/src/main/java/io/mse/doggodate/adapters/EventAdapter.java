package io.mse.doggodate.adapters;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.time.format.DateTimeFormatter;
import java.util.List;

import io.mse.doggodate.entity.DoggoEvent;
import io.mse.doggodate.R;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(DoggoEvent item);
    }


    private final OnItemClickListener listener;

    private List<DoggoEvent> eventList;

    public EventAdapter(List<DoggoEvent> eventList, OnItemClickListener listener) {
        this.eventList = eventList;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    @Override
    public void onBindViewHolder(EventViewHolder eventViewHolder, int i) {
        DoggoEvent event = eventList.get(i);
        eventViewHolder.zone.setText(event.getZone().getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");

        String formatDateTime = event.getDateTime().format(formatter);
        eventViewHolder.creator.setText(event.getCreator().getName() + " is going for a walk at " + formatDateTime);
        eventViewHolder.others.setText(event.getDoggosJoining().size() + " others are joining.");

        eventViewHolder.bind(eventList.get(i), listener);

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