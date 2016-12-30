package com.example.pk.test2012;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.pk.test2012.uttil.Utiil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by pk on 17.10.2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    ArrayList<EarthQuake> earthQuakesdata;
    Context context;
    private static final String LOCATION_SEPERATOR = " of ";
    RecyclerEvent.LongClickListener longClickListener;

    public RecyclerViewAdapter(ArrayList<EarthQuake> earthQuakesdata, Context context, RecyclerEvent.LongClickListener longClickListener) {
        this.context = context;
        this.earthQuakesdata = earthQuakesdata;
        this.longClickListener = longClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView location_offsetTv;
        TextView primary_locationTv;
        TextView magnitude;
        TextView date;
        TextView time;

        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            this.location_offsetTv = (TextView) itemView.findViewById(R.id.location_offset);
            this.primary_locationTv = (TextView) itemView.findViewById(R.id.primary_location);
            this.magnitude = (TextView) itemView.findViewById(R.id.magnitude);
            this.date = (TextView) itemView.findViewById(R.id.date);
            this.time = (TextView) itemView.findViewById(R.id.time);


            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(final View v) {
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(earthQuakesdata.get(getAdapterPosition()).getUrl()));
//            context.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            longClickListener.onLongClick(getAdapterPosition());
            return true;
        }

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final TextView location_offsetTv = holder.location_offsetTv;
        final TextView primary_locationTv = holder.primary_locationTv;
        final TextView magnitude = holder.magnitude;
        final TextView date = holder.date;
        final TextView time = holder.time;
        String locationOfset;
        String primary_location;

        String originalLocation = earthQuakesdata.get(position).getLocation();
        if (originalLocation.contains(LOCATION_SEPERATOR)) {
            String[] parts = originalLocation.split(LOCATION_SEPERATOR);
            locationOfset = parts[0] + LOCATION_SEPERATOR;
            primary_location = parts[1];
        } else {
            locationOfset = "near the";
            primary_location = originalLocation;
        }

        location_offsetTv.setText(locationOfset);
        primary_locationTv.setText(primary_location);
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        magnitude.setText(decimalFormat.format(earthQuakesdata.get(position).getMagnitude()));

        Date dateObj = new Date(earthQuakesdata.get(position).getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        date.setText(dateFormat.format(dateObj));

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        time.setText(timeFormat.format(dateObj));

        GradientDrawable magnitudeCircle = (GradientDrawable) magnitude.getBackground();
        int magnitudeColor = Utiil.calculateCircleColor(context, earthQuakesdata.get(position).getMagnitude());
        magnitudeCircle.setColor(magnitudeColor);
        magnitude.setBackground(magnitudeCircle);

    }

    @Override
    public int getItemCount() {
        return earthQuakesdata.size();
    }
}

