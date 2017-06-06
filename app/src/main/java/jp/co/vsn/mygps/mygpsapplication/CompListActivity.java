package jp.co.vsn.mygps.mygpsapplication;

import java.util.List;

import jp.co.vsn.mygps.mygpsapplication.db.AddressDao;
import jp.co.vsn.mygps.mygpsapplication.db.AddressEntity;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CompListActivity extends Activity {
    private AddressDao mAddressDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_list);
        ListView listView = (ListView) findViewById(R.id.list_view);
        mAddressDao = new AddressDao(getApplicationContext());
        List<AddressEntity> addressList = mAddressDao.selectAll();
        CustomAdapter adapter = new CustomAdapter(getApplicationContext(), addressList);
        listView.setAdapter(adapter);
    }

    private class CustomAdapter extends ArrayAdapter<AddressEntity> {

        private LayoutInflater mInflater;

        public CustomAdapter(Context context, List<AddressEntity> list) {
            super(context, 0, list);
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AddressEntity item = getItem(position);
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.listitem, null);
                holder.stateText = (TextView) convertView.findViewById(R.id.state_text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.stateText.setText(item.getState());

            if (item.isComp()) {
                holder.stateText.setBackgroundColor(Color.BLUE);
            } else {
                holder.stateText.setBackgroundColor(Color.WHITE);
            }
            return convertView;
        }
    }

    private class ViewHolder {
        TextView stateText;
    }
}
