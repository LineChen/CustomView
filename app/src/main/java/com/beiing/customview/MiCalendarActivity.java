package com.beiing.customview;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.beiing.customview.widgets.calendar2.CollapseCalendarView;
import com.beiing.customview.widgets.calendar2.manager.CalendarManager;

import org.joda.time.LocalDate;

import java.util.ArrayList;

public class MiCalendarActivity extends AppCompatActivity {
    private CollapseCalendarView mCalendarView;
    private CalendarManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_calendar);
        initCalendar();
        initListView();
    }

    private void initCalendar() {
        manager = new CalendarManager(LocalDate.now(),
                CalendarManager.State.MONTH, LocalDate.now().withYear(100),
                LocalDate.now().plusYears(100));
        mCalendarView = (CollapseCalendarView) findViewById(R.id.calendar);
        mCalendarView.init(manager);
        mCalendarView.setListener(new CollapseCalendarView.OnDateSelect() {

            @Override
            public void onDateSelected(LocalDate date) {
                String str = date.toString();
            }
        });
    }

    private void initListView() {
        ListView listview = (ListView) findViewById(R.id.listview);
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < 4; i++) {
            list.add("今日无安排" + i);
        }
        ListAdapter adapter = new ListAdapter(this, list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

            }
        });
    }

    class ListAdapter extends BaseAdapter {
        private ArrayList<String> list;
        private Context context;

        public ListAdapter(Context context, ArrayList<String> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == list.size() - 1) {
                return 1;
            } else {
                return 0;
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = null;
            ViewHolder vh = null;
            if (convertView == null) {
                if (getItemViewType(position) == 0) {
                    v = ininView();
                } else {
                    v = initLastView();
                }
            } else {
                v = convertView;
            }
            vh = (ViewHolder) v.getTag();
            if (getItemViewType(position) == 0) {
                bindView(vh, position);
            }
            return v;
        }

        private View initLastView() {
            ViewHolder vh = new ViewHolder();
            View v = LayoutInflater.from(context).inflate(
                    R.layout.item_calendar_notification_last, null);
            return v;
        }

        private void bindView(ViewHolder vh, int position) {
            String title = list.get(position);
            // vh.tvTitle.setText(title);
        }

        private View ininView() {
            ViewHolder vh = new ViewHolder();
            View v = LayoutInflater.from(context).inflate(
                    R.layout.item_calendar_notification, null);
            vh.tvTime = (TextView) v.findViewById(R.id.tvTime);
            vh.tvTitle = (TextView) v.findViewById(R.id.tvTitle);
            vh.ivStatus = (ImageView) v.findViewById(R.id.ivStatus);
            v.setTag(vh);
            return v;
        }

        class ViewHolder {
            private TextView tvTime;
            private TextView tvTitle;
            private ImageView ivStatus;
        }
    }
}
