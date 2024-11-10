package com.example.loginproject.UI.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginproject.Models.GastosDia;
import com.example.loginproject.R;
import com.example.loginproject.UI.ViewHolder.DailyExpenseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class DailyExpensesRecyclerAdapter extends RecyclerView.Adapter<DailyExpenseViewHolder> {

    List<GastosDia> items = new ArrayList<>();
    public DailyExpensesRecyclerAdapter(List<GastosDia> items) {
        this.items = new ArrayList<>();
    }
    @NonNull
    @Override
    public DailyExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.detail_item_layout, parent, false);
        return new DailyExpenseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyExpenseViewHolder holder, int position) {
        holder.txvNombreArticulo.setText("Nombre: " + items.get(position).getNombreArticulo());
        holder.txvPrecio.setText( "Precio: $" + String.format("%.2f", items.get(position).getPrecio()));
        holder.txvCantidad.setText("Cantidad: " + items.get(position).getCantidad());
        holder.txvSubtotal.setText("Subtotal: $" + String.format("%.2f", items.get(position).getSubTotal()));
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    public void setDataList(List<GastosDia> dataList) {
        this.items = dataList;
        notifyDataSetChanged();
    }
    public double getActualSubTotalAll() {
        double finalData = 0;
        for (GastosDia item : this.items) {
            finalData += item.getSubTotal();
        }
        return finalData;
    }
}
