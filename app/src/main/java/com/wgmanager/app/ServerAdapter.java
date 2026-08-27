package com.wgmanager.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ServerAdapter extends RecyclerView.Adapter<ServerAdapter.ViewHolder> {
    private List<Server> servers;
    private OnServerClickListener listener;

    public interface OnServerClickListener {
        void onServerClick(Server server, int position);
    }

    public ServerAdapter(List<Server> servers, OnServerClickListener listener) {
        this.servers = servers;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_server, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Server s = servers.get(position);
        holder.tvName.setText(s.getName());
        holder.tvLocation.setText(s.getLocation());
        if (s.isConnected()) {
            holder.tvStatus.setText(R.string.connected);
            holder.tvStatus.setTextColor(holder.itemView.getContext().getColor(R.color.status_connected));
            holder.dot.setBackgroundResource(R.drawable.dot_status_connected);
        } else {
            holder.tvStatus.setText(R.string.disconnected);
            holder.tvStatus.setTextColor(holder.itemView.getContext().getColor(R.color.status_disconnected));
            holder.dot.setBackgroundResource(R.drawable.dot_status);
        }
        holder.itemView.setOnClickListener(v -> listener.onServerClick(s, position));
    }

    @Override
    public int getItemCount() { return servers.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvLocation, tvStatus;
        View dot;
        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvServerName);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            dot = itemView.findViewById(R.id.dotStatus);
        }
    }
}
