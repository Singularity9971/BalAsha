package thuglife.balasha;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by avirudhtheraja on 12/14/15.
 */
public class NavDrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;

    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
        this.context = context;
        this.navDrawerItems = navDrawerItems;
    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_list_item, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);

        if(navDrawerItems.get(position).getIcon() != 0)
            imgIcon.setImageResource(navDrawerItems.get(position).getIcon());

        String check = navDrawerItems.get(position).getTitle();

        if(check.equals("Bal Asha Child Development Centre") || check.equals("Home")) {
            if(imgIcon.getScaleType() != ImageView.ScaleType.FIT_CENTER)
                imgIcon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }

        txtTitle.setText(navDrawerItems.get(position).getTitle());

        return convertView;
    }

}

