package it.pgp.continuousselectionadapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AdapterLite extends ArrayAdapter<AdapterItem> {

    private final LayoutInflater inflater;
    private final List<AdapterItem> objects;
    private final int itemLayoutRes;

    public AdapterLite(Context context,
                       int itemLayoutRes,
                       List<AdapterItem> objects) {
        super(context, itemLayoutRes,objects);
        inflater = LayoutInflater.from(context);
        this.objects = objects;
        this.itemLayoutRes = itemLayoutRes;
    }

    private static int getResByString(String s) {
        switch (s) {
            case "WindowsMobile":
                return R.drawable.windowsmobile_logo;
            case "iOS":
                return R.drawable.ios_logo;
            case "Blackberry":
                return R.drawable.blackberry_logo;
            default:
                return R.drawable.android_logo;
        }
    }

    public static class AdapterItemViewHolder {
        TextView name;
        ImageView imageView;

        AdapterItemViewHolder(TextView name, ImageView imageView) {
            this.name = name;
            this.imageView = imageView;
        }
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull final ViewGroup parent) {
        AdapterItem item = getItem(position);
        TextView name;
        ImageView imageView;

        if(convertView == null) {
            convertView = inflater.inflate(itemLayoutRes, null);

            name = convertView.findViewById(R.id.label);
            imageView = convertView.findViewById(R.id.logo);

            convertView.setTag(new AdapterItemViewHolder(name,imageView));
        }
        else {
            AdapterItemViewHolder viewHolder = (AdapterItemViewHolder) convertView.getTag();
            name = viewHolder.name;
            imageView = viewHolder.imageView;
        }

        convertView.setBackgroundColor(item.isChecked()? 0x9934B5E4: Color.TRANSPARENT);

        name.setText(item.filename);

        imageView.setImageResource(getResByString(item.filename));

        return convertView;
    }

    public void toggleSelectOne(int position) {
        objects.get(position).toggle();
        notifyDataSetChanged();
    }
}
