package com.aktechzone.propertydeal.DrawerFragments.RecyclerProp;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aktechzone.propertydeal.Modelclass;
import com.aktechzone.propertydeal.MyProgressDialog.MyProgressDialog;
import com.aktechzone.propertydeal.R;
import com.aktechzone.propertydeal.ServiceUrls;
import com.aktechzone.propertydeal.SingletonVolley;
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

public class NewRequestRecycler extends RecyclerView.Adapter<NewRequestRecycler.RequestViewHolderClass> {

    private List<Modelclass> list;
    private Context context;
    private String flag;
    private ProgressDialog progressDialog;

    public NewRequestRecycler(List<Modelclass> list, Context context, String flage) {
        this.list = list;
        this.context = context;
        this.flag = flage;
    }

    @Override
    public NewRequestRecycler.RequestViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_new_request_view, parent, false);
        return new NewRequestRecycler.RequestViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolderClass holder, final int position) {

        holder.tvReqname.setText(list.get(position).getName());
        holder.tvReqEmail.setText(list.get(position).geteMail());
        holder.tvReqPhone.setText(list.get(position).getPhone());


        holder.imgDelReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag.equals("manager")) {
                    progressDialog = MyProgressDialog.showDialog(context, "Loading . .");
                    deleteRequest(list.get(position).getId(), position, ServiceUrls.DELETE_MANGER_AGENT_LIST_REQUEST);
                } else if (flag.equals("agent")) {
                    progressDialog = MyProgressDialog.showDialog(context, "Loading . .");
                    deleteRequest(list.get(position).getId(), position, ServiceUrls.DELETE_MANGER_AGENT_LIST_REQUEST);
                }
            }
        });
        holder.imgReqApproved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag.equals("manager")) {
                    progressDialog = MyProgressDialog.showDialog(context, "Loading . . ");
                    approveRequest(list.get(position).getId(), position, ServiceUrls.APPROVED_MANAGER_REQUEST, "2");
                } else if (flag.equals("agent")) {
                    progressDialog = MyProgressDialog.showDialog(context, "Loading . .");
                    approveRequest(list.get(position).getId(), position, ServiceUrls.APPROVED_AGENT_REQUEST, "3");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RequestViewHolderClass extends RecyclerView.ViewHolder {
        TextView tvReqname, tvReqEmail, tvReqPhone;
        ImageView imgReqApproved, imgDelReq;

        public RequestViewHolderClass(View itemView) {
            super(itemView);
            tvReqEmail = itemView.findViewById(R.id.tvReqemail);
            tvReqname = itemView.findViewById(R.id.tvReqName);
            tvReqPhone = itemView.findViewById(R.id.tvReqphone);
            imgReqApproved = itemView.findViewById(R.id.ivApprovedRequest);
            imgDelReq = itemView.findViewById(R.id.ivDeleteRequest);
        }
    }

    private void deleteRequest(final String id, final int position, String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                progressDialog.cancel();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equals("true")) {
                        list.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
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

    private void approveRequest(final String id, final int position, String url, final String statues) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                progressDialog.cancel();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    if (status.equals("true")) {
                        list.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Approved", Toast.LENGTH_SHORT).show();
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
                params.put("status", statues);
                return params;
            }
        };
        SingletonVolley.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);
    }
}
