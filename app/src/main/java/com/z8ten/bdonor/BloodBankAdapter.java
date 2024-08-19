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

public class BloodBankAdapter extends RecyclerView.Adapter<BloodBankAdapter.BloodBankViewHolder> {
    private List<BloodBank> bloodBanks;

    public BloodBankAdapter(List<BloodBank> bloodBanks) {
        this.bloodBanks = bloodBanks;
    }

    @Override
    public BloodBankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blood_bank_item, parent, false);
        return new BloodBankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BloodBankViewHolder holder, int position) {
        BloodBank bloodBank = bloodBanks.get(position);
        holder.nameTextView.setText(bloodBank.getName());
        holder.cityTextView.setText(bloodBank.getCity());
        holder.phoneTextView.setText(bloodBank.getPhone());

        holder.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + bloodBank.getPhone()));
                view.getContext().startActivity(dialIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bloodBanks.size();
    }

    public void updateBloodBanks(List<BloodBank> newBloodBanks) {
        bloodBanks.clear();
        bloodBanks.addAll(newBloodBanks);
        notifyDataSetChanged();
    }

    static class BloodBankViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView cityTextView;
        TextView phoneTextView;
        Button callButton;

        BloodBankViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            cityTextView = itemView.findViewById(R.id.cityTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            callButton = itemView.findViewById(R.id.callButton);
        }
    }
}
