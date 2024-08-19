package com.z8ten.bdonor;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DonorAdapter extends RecyclerView.Adapter<DonorAdapter.DonorViewHolder> {
    private List<Donor> donors;

    public DonorAdapter(List<Donor> donors) {
        this.donors = donors;
    }

    @Override
    public DonorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donor_item, parent, false);
        return new DonorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DonorViewHolder holder, int position) {
        Donor donor = donors.get(position);
        holder.nameTextView.setText(donor.getName());
        holder.bloodTypeTextView.setText(donor.getBloodType());
        holder.phoneTextView.setText(donor.getPhone());
        holder.cityTextView.setText(donor.getCity());


        holder.callmebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dielIntent=new Intent(Intent.ACTION_DIAL);
                dielIntent.setData(Uri.parse("tel: "+donor.getPhone()));
                view.getContext().startActivity(dielIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return donors.size();
    }

    public void updateDonors(List<Donor> newDonors) {
        donors.clear();
        donors.addAll(newDonors);
        notifyDataSetChanged();
    }

    static class DonorViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView bloodTypeTextView;
        TextView phoneTextView;
        Button callmebtn;
        TextView cityTextView;

        DonorViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            bloodTypeTextView = itemView.findViewById(R.id.bloodTypeTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            callmebtn=itemView.findViewById(R.id.callmebtn);
            cityTextView=itemView.findViewById(R.id.cityTextView);
        }
    }
}
