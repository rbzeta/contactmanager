package app.rbzeta.contactmanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import app.rbzeta.contactmanager.R;
import app.rbzeta.contactmanager.custom.CircleTransform;
import app.rbzeta.contactmanager.model.Contact;

/**
 * Created by Robyn on 01/11/2016.
 */

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.MyViewHolder> {
    private List<Contact> mContactList;
    private Context mContext;

    public ContactListAdapter(Context context,List<Contact> contactList){
        mContactList = contactList;
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_contact_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Contact contact = mContactList.get(position);
        String url = contact.getProfilePic();
        Glide.with(mContext).load(url)
                .crossFade()
                .thumbnail(0.2f)
                .centerCrop()
                .placeholder(R.drawable.ic_account_circle_black_24dp)
                .error(R.drawable.ic_account_circle_black_24dp)
                .bitmapTransform(new CircleTransform(mContext))
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.imgProfile);

        holder.textContactName.setText(contact.getFirstName()+" "+contact.getLastName());
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProfile;
        private TextView textContactName;

        public MyViewHolder(View view) {
            super(view);
            imgProfile = (ImageView)view.findViewById(R.id.img_contact_list);
            textContactName = (TextView)view.findViewById(R.id.text_contact_name);
        }
    }
}
