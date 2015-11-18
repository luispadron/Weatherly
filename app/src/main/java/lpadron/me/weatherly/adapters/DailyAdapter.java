package lpadron.me.weatherly.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import lpadron.me.weatherly.R;
import lpadron.me.weatherly.weather.Daily;


public class DailyAdapter extends BaseAdapter {

    private Context context;
    private Daily[] dailyList;

    public DailyAdapter(Context context, Daily[] dailyList) {
        this.context = context;
        this.dailyList = dailyList;
    }

    @Override
    public int getCount() {
        return dailyList.length;
    }

    @Override
    public Object getItem(int position) {
        return dailyList[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            //create the data
            convertView = LayoutInflater.from(context).inflate(R.layout.daily_list_item, null);
            holder = new ViewHolder();
            holder.iconImageView = (ImageView) convertView.findViewById(R.id.dailyIconImageView);
            holder.tempLabel = (TextView) convertView.findViewById(R.id.currentlyTempLabel);
            holder.dayLabel = (TextView) convertView.findViewById(R.id.dayLabel);

            convertView.setTag(holder);

        }else {
            //views already loading and user is scrolling
            holder = (ViewHolder) convertView.getTag();
        }

        Daily day = dailyList[position];

        holder.iconImageView.setImageResource(day.getIconId());
        holder.tempLabel.setText(day.getTempHigh() + "");
        if (position == 0) {
            holder.dayLabel.setText("Today");
        } else {
            holder.dayLabel.setText(day.getDayOfTheWeek());
        }
        return convertView;
    }

    private static class ViewHolder {
        ImageView iconImageView;
        TextView tempLabel;
        TextView dayLabel;
    }
}
