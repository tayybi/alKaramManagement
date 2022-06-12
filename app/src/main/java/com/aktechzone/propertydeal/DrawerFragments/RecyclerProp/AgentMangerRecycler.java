package com.aktechzone.propertydeal.DrawerFragments.RecyclerProp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aktechzone.propertydeal.Modelclass;
import com.aktechzone.propertydeal.MyProgressDialog.MyProgressDialog;
import com.aktechzone.propertydeal.MyUtils;
import com.aktechzone.propertydeal.R;
import com.aktechzone.propertydeal.ServiceUrls;
import com.aktechzone.propertydeal.SingletonVolley;
import com.aktechzone.propertydeal.activities.EditAgentMangerActivity;
import com.aktechzone.propertydeal.activities.ProfileMangerActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgentMangerRecycler extends RecyclerView.Adapter<AgentMangerRecycler.MangerViewHolder> {
    private Context context;
    private List<Modelclass> allRecord;
    String flageType;
    ProgressDialog progressDialog;

    public AgentMangerRecycler(Context context, List<Modelclass> list, String flageType) {
        allRecord = list;
        this.context = context;
        this.flageType = flageType;
    }

    @NonNull
    @Override
    public MangerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View mView = inflater.inflate(R.layout.custom_all_manger_display, parent, false);
        return new MangerViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MangerViewHolder holder, final int position) {

        holder.name.setText(allRecord.get(position).getName());
        holder.email.setText(allRecord.get(position).geteMail());
        holder.phoneNo.setText(allRecord.get(position).getPhone());
        holder.description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileMangerActivity.class);
                intent.putExtra("ID", allRecord.get(position).getId());
                intent.putExtra("NAME", allRecord.get(position).getName());
                intent.putExtra("EMAIL", allRecord.get(position).geteMail());
                intent.putExtra("PHONE", allRecord.get(position).getPhone());
                intent.putExtra("PASS", allRecord.get(position).getPassword());
                intent.putExtra("FLAGETYPE", flageType);
                context.startActivity(intent);
            }
        });

        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditAgentMangerActivity.class);
                intent.putExtra("ID", allRecord.get(position).getId());
                intent.putExtra("NAME", allRecord.get(position).getName());
                intent.putExtra("EMAIL", allRecord.get(position).geteMail());
                intent.putExtra("PHONE", allRecord.get(position).getPhone());
                intent.putExtra("PASS", allRecord.get(position).getPassword());
                intent.putExtra("FLAGETYPE", flageType);
                context.startActivity(intent);
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MyUtils.isNetworkAvailable(context)) {
                    DialogInterface.OnClickListener dialogInterface = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            progressDialog = MyProgressDialog.showDialog(context, "Loading . .");
                            deleteRecord(allRecord.get(position).getId(),position);
                        }
                    };
                    MyUtils.showAlertDialog(context, dialogInterface);
                } else {
                    MyUtils.showSnackBar(context);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return allRecord.size();
    }

    public class MangerViewHolder extends RecyclerView.ViewHolder {
        TextView name, phoneNo, email;
        ImageView ivEdit, ivDelete;
        LinearLayout description;

        public MangerViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            phoneNo = itemView.findViewById(R.id.phone);
            email = itemView.findViewById(R.id.email);
            description = itemView.findViewById(R.id.description);
            ivEdit = itemView.findViewById(R.id.ivEditPerson);
            ivDelete = itemView.findViewById(R.id.ivDeletePerson);
        }
    }

    private void deleteRecord(final String id, final int position) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServiceUrls.DELETE_MANGER_AGENT_LIST_REQUEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                progressDialog.cancel();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equals("true")) {
                        allRecord.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "item deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    progressDialog.cancel();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                progressDialog.cancel();
                Toast.makeText(context, "Connection Problem!" + error, Toast.LENGTH_SHORT).show();
                Log.wtf("error", "Volley Error " + error);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("id", id);
                return params;
            }
        };
        SingletonVolley.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
