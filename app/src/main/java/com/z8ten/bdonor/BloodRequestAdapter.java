package com.z8ten.bdonor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BloodRequestAdapter extends RecyclerView.Adapter<BloodRequestAdapter.ViewHolder> {
    private List<BloodRequest> bloodRequests;
    private Context context;

    public BloodRequestAdapter(Context context, List<BloodRequest> bloodRequests) {
        this.bloodRequests = bloodRequests;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.blood_request_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BloodRequest bloodRequest = bloodRequests.get(position);
        holder.donorNameTextView.setText(bloodRequest.getDonorName());
        holder.bloodTypeTextView.setText(bloodRequest.getBloodType());
        holder.hospitalTextView.setText(bloodRequest.getHospital());
        holder.contactNumberTextView.setText(bloodRequest.getContactNumber());

        // Handle the call icon click
        holder.callIcon.setOnClickListener(v -> {
            String phoneNumber = bloodRequest.getContactNumber(); // Assuming this is a valid phone number
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            context.startActivity(callIntent);
        });
    }

    @Override
    public int getItemCount() {
        return bloodRequests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView donorNameTextView;
        TextView bloodTypeTextView;
        TextView hospitalTextView;
        TextView contactNumberTextView;
        ImageView callIcon; // Call icon ImageView

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            donorNameTextView = itemView.findViewById(R.id.donorNameTextView);
            bloodTypeTextView = itemView.findViewById(R.id.bloodTypeTextView);
            hospitalTextView = itemView.findViewById(R.id.hospitalTextView);
            contactNumberTextView = itemView.findViewById(R.id.contactNumberTextView);
            callIcon = itemView.findViewById(R.id.callIcon); // Initialize the call icon
        }
    }
}
