package lpadron.me.weatherly.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import lpadron.me.weatherly.R;
import lpadron.me.weatherly.weather.Hourly;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder>{

    private Hourly[] hours;


    public HourlyAdapter(Hourly[] hours) {
        this.hours = hours;
    }

    public class HourlyViewHolder extends RecyclerView.ViewHolder {

        public TextView timeLabel;
        public TextView summaryLabel;
        public TextView tempLabel;
        public ImageView iconImageView;

        public HourlyViewHolder(View itemView) {
            super(itemView);

            /* Instantiate the views */
            timeLabel = (TextView) itemView.findViewById(R.id.hourlyTimeLabel);
            summaryLabel = (TextView) itemView.findViewById(R.id.hourlySummaryLabel);
            tempLabel = (TextView) itemView.findViewById(R.id.hourlyTempLabel);
            iconImageView = (ImageView) itemView.findViewById(R.id.hourlyIconImageView);
        }

        public void bindHour(Hourly hour) {
            timeLabel.setText(hour.getHour());
            summaryLabel.setText(hour.getSummary());
            tempLabel.setText(hour.getTemp() + "");
            iconImageView.setImageResource(hour.getIconId());
        }
    }

    @Override
    public HourlyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hourly_list_item, parent, false);

        HourlyViewHolder viewHolder = new HourlyViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HourlyViewHolder holder, int position) {
        holder.bindHour(hours[position]);
    }

    @Override
    public int getItemCount() {
        return hours.length;
    }
}
