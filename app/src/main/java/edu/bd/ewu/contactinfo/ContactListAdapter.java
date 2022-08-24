package edu.bd.ewu.contactinfo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactListAdapter extends ArrayAdapter<ContactInfo> {

    ArrayList<ContactInfo> contactInfos;
    public ContactListAdapter(Context context, ArrayList<ContactInfo> contactInfos) {
        super(context, R.layout.contact_list_design, contactInfos);
        this.contactInfos = contactInfos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_list_design, parent, false);
        }

        TextView user_name = convertView.findViewById(R.id.user_name);
        TextView user_phone = convertView.findViewById(R.id.user_number);
        CircleImageView image = convertView.findViewById(R.id.user_img);
        ImageView call_btn = convertView.findViewById(R.id.call_btn);

        user_name.setText(contactInfos.get(position).getName());
        user_phone.setText(contactInfos.get(position).getPhone());

        if(!contactInfos.get(position).getImage().equalsIgnoreCase("") ){
            byte[] b = Base64.decode(contactInfos.get(position).getImage(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            image.setImageBitmap(bitmap);
        }

        System.out.println(contactInfos.get(position).getName());
        return convertView;
    }
}
