package com.karan.bunchtest.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.karan.bunchtest.R;
import com.karan.bunchtest.model.EmployeeModel;

import java.util.ArrayList;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {
    private ArrayList<EmployeeModel> empList;

    public EmployeeAdapter(ArrayList<EmployeeModel> empList) {
        this.empList = empList;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_data_card, parent, false);
        return new EmployeeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        holder.empID.setText(""+empList.get(position).getEmpId());
        holder.empName.setText(empList.get(position).getEmpName());
    }

    @Override
    public int getItemCount() {
        return empList.size();
    }

    public class EmployeeViewHolder extends RecyclerView.ViewHolder{
        private TextView empID, empName;
        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);

            empID = itemView.findViewById(R.id.emp_id);
            empName = itemView.findViewById(R.id.emp_name);
        }
    }
}
