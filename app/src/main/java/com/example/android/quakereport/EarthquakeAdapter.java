package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;





public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {
    String placePrefix, placeSufix;
    final String LOCATION_SEPERATOR =" of";
    final String LOCATION_SEPERATOR2 = "Near the";


    public EarthquakeAdapter(@NonNull Context context, ArrayList<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        Earthquake currentAndroidEarthquake = getItem(position);

        TextView magnitudeView =  (TextView) listItemView.findViewById(R.id.mag);
        magnitudeView.setText(formatMagnitude(currentAndroidEarthquake.getMag()));
        /*
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();
        int magnitudeColor = getMagnitudeColor(currentAndroidEarthquake.getMag());
        magnitudeCircle.setColor(getMagnitudeColor(currentAndroidEarthquake.mMagnitude));

        */

        TextView place_prefix = (TextView) listItemView.findViewById(R.id.place_prefix);
        place_prefix.setText(placePrefix);
        TextView place_sufix= (TextView) listItemView.findViewById(R.id.place_sufix);
        place_sufix.setText(placeSufix);


        TextView date = (TextView) listItemView.findViewById(R.id.date);
        String dateToDisplay = formatDate(currentAndroidEarthquake.getDate());
        date.setText(dateToDisplay);

        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        timeView.setText(formatTime(currentAndroidEarthquake.getDate()));

        splitPlace(currentAndroidEarthquake.mPlace);



        return listItemView;
    }

    public  String formatTime(long timeinMilliseconds){
        Date time = new Date(timeinMilliseconds);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
        return simpleDateFormat.format(time);
    }


    public String formatDate(long dateInMilliseconds){
        Date date = new Date(dateInMilliseconds);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM DD, yyyy");
        return  dateFormat.format(date);
    }

    public void splitPlace(String orignalPlace){
        if (orignalPlace.contains(LOCATION_SEPERATOR)){
            String[] tmp = orignalPlace.split(LOCATION_SEPERATOR);
            placePrefix = tmp[1];
            placeSufix= tmp[0]+LOCATION_SEPERATOR;
        }else{
            placePrefix = orignalPlace;
            placeSufix = LOCATION_SEPERATOR2;
        }
    }

    public String formatMagnitude(double magnitude){
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        return decimalFormat.format(magnitude);
    }
/*
    public int getMagnitudeColor(double magnitude){
        int magnitude1Color;
        switch ((int) Math.floor(magnitude)){
            case 0:
            case 1:
                magnitude1Color =  R.color.magnitude1;
                break;
            case 2:
                magnitude1Color =  R.color.magnitude2;
                break;
            case 3:
                magnitude1Color =  R.color.magnitude3;
                break;
            case 4:
                magnitude1Color =  R.color.magnitude4;
                break;
            case 5:
                magnitude1Color =  R.color.magnitude5;
                break;
            case 6:
                magnitude1Color =  R.color.magnitude6;
                break;
            case 7:
                magnitude1Color =  R.color.magnitude7;
                break;
            case 8:
                magnitude1Color =  R.color.magnitude8;
                break;
            case 9:
                magnitude1Color =  R.color.magnitude9;
                break;
            default:
                magnitude1Color =  R.color.magnitude9;
        }
        return ContextCompat.getColor(getContext(), magnitude1Color);
    }*/
}
