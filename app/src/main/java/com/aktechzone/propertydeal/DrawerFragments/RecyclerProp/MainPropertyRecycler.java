package com.aktechzone.propertydeal.DrawerFragments.RecyclerProp;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aktechzone.propertydeal.MyUtils;
import com.aktechzone.propertydeal.Modelclass;
import com.aktechzone.propertydeal.MyProgressDialog.MyProgressDialog;
import com.aktechzone.propertydeal.R;
import com.aktechzone.propertydeal.ServiceUrls;
import com.aktechzone.propertydeal.SingletonVolley;
import com.aktechzone.propertydeal.activities.EditBuyerActivity;
import com.aktechzone.propertydeal.activities.EditSellerActivity;
import com.aktechzone.propertydeal.activities.ProfileBuyerPropertyActivity;
import com.aktechzone.propertydeal.activities.ProfileSellerPropertyActivity;
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

public class MainPropertyRecycler extends RecyclerView.Adapter<MainPropertyRecycler.ViewHoldeClass> {

    private Map<String, Boolean> checkBoxStates = new HashMap<>();
    ProgressDialog progressDialog;

    public interface OnItemCheckListener {
        void onItemCheck(Modelclass item);

        void onItemUncheck(Modelclass item);
    }

    private List<Modelclass> list;
    private Context context;
    private String layoutFlag = "single", typeFlag;

    @NonNull
    private OnItemCheckListener onItemCheckListener;

    public MainPropertyRecycler(List<Modelclass> list, Context context, String layoutFlag, String typeFlag, @NonNull OnItemCheckListener onItemCheckListener) {
        this.list = list;
        this.context = context;
        this.layoutFlag = layoutFlag;
        this.typeFlag = typeFlag;
        this.onItemCheckListener = onItemCheckListener;
        int i;
        for (i = 0; i < list.size(); i++) {
            checkBoxStates.put(i + "", false);
        }
    }

    @Override
    public ViewHoldeClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_property_display, parent, false);
        return new ViewHoldeClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHoldeClass holder, final int position) {
        final Modelclass currentItem = list.get(position);
        if (typeFlag.equals("buyer")) {
            holder.name.setText(list.get(position).getName());
            holder.price.setText(list.get(position).getBudjet());
            holder.dateof.setText(list.get(position).getDate());
        } else {
            holder.name.setText(list.get(position).getName());
            holder.price.setText(list.get(position).getBudjet());
            holder.dateof.setText(list.get(position).getDate());
        }
        if (layoutFlag.equals("single")) {
            Log.wtf("single", "true");
            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.linearLayoutCb.setVisibility(View.GONE);
        } else {
            holder.linearLayout.setVisibility(View.GONE);
            holder.linearLayoutCb.setVisibility(View.VISIBLE);
        }
        if (layoutFlag.equals("single")) {
            holder.description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (typeFlag.equals("buyer")) {
                        Intent intent = new Intent(context, ProfileBuyerPropertyActivity.class);
                        intent.putExtra("id", list.get(position).getId());
                        intent.putExtra("name", list.get(position).getName());
                        intent.putExtra("contact", list.get(position).getContact());
                        intent.putExtra("date", list.get(position).getDate());
                        intent.putExtra("ref", list.get(position).getRefrence());
                        intent.putExtra("area", list.get(position).getArea());
                        intent.putExtra("budjet", list.get(position).getBudjet());
                        intent.putExtra("note", list.get(position).getNote());
                        intent.putExtra("catName", list.get(position).getCatName());
                        intent.putExtra("createdBy", list.get(position).getCreatedBy());
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, ProfileSellerPropertyActivity.class);
                        intent.putExtra("id", list.get(position).getId());
                        intent.putExtra("city", list.get(position).getCity());
                        intent.putExtra("name", list.get(position).getName());
                        intent.putExtra("phase", list.get(position).getPhase());
                        intent.putExtra("block", list.get(position).getBlock());
                        intent.putExtra("plot", list.get(position).getPlot());
                        intent.putExtra("demand", list.get(position).getDemand());
                        intent.putExtra("refrence", list.get(position).getRefrence());
                        intent.putExtra("contact", list.get(position).getContact());
                        intent.putExtra("date", list.get(position).getDate());
                        intent.putExtra("catName", list.get(position).getCatName());
                        intent.putExtra("createdBy", list.get(position).getCreatedBy());
                        intent.putExtra("note", list.get(position).getNote());
                        context.startActivity(intent);
                    }
                }
            });
            holder.imgEditProperty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (typeFlag.equals("buyer")) {
                        Intent intent = new Intent(context, EditBuyerActivity.class);
                        intent.putExtra("id", list.get(position).getId());
                        intent.putExtra("name", list.get(position).getName());
                        intent.putExtra("contact", list.get(position).getContact());
                        intent.putExtra("date", list.get(position).getDate());
                        intent.putExtra("ref", list.get(position).getRefrence());
                        intent.putExtra("area", list.get(position).getArea());
                        intent.putExtra("budjet", list.get(position).getBudjet());
                        intent.putExtra("note", list.get(position).getNote());
                        context.startActivity(intent);
                    } else {
                        Intent intent = new Intent(context, EditSellerActivity.class);
                        intent.putExtra("id", list.get(position).getId());
                        intent.putExtra("city", list.get(position).getCity());
                        intent.putExtra("name", list.get(position).getName());
                        intent.putExtra("phase", list.get(position).getPhase());
                        intent.putExtra("block", list.get(position).getBlock());
                        intent.putExtra("plot", list.get(position).getPlot());
                        intent.putExtra("demand", list.get(position).getDemand());
                        intent.putExtra("refrence", list.get(position).getRefrence());
                        intent.putExtra("contact", list.get(position).getContact());
                        intent.putExtra("date", list.get(position).getDate());
                        context.startActivity(intent);
                    }
                }
            });
            holder.imgDelProperty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MyUtils.isNetworkAvailable(context)) {
                        DialogInterface.OnClickListener dialogInterface = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (typeFlag.equals("buyer")) {
                                    progressDialog = MyProgressDialog.showDialog(context, "Loading . .");
                                    deleteSellerData(list.get(position).getId(), position, ServiceUrls.DELETE_BUYER_PROPERTY);
                                } else {
                                    progressDialog = MyProgressDialog.showDialog(context, "Loading . .");
                                    deleteSellerData(list.get(position).getId(), position, ServiceUrls.DELETE_SELLER_PROPERTY);
                                }
                            }
                        };
                        MyUtils.showAlertDialog(context, dialogInterface);
                    } else {
                        MyUtils.showSnackBar(context);
                    }
                }
            });
        } else {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.checkBox.setChecked(!holder.checkBox.isChecked());
                    if (holder.checkBox.isChecked()) {
                        currentItem.setId(list.get(position).getId());
//                        Log.wtf("checkedBuyer","true");
                        onItemCheckListener.onItemCheck(currentItem);
                        onCheckChanged(position, true);
                    } else {
                        currentItem.setId(list.get(position).getId());
//                        Log.wtf("checkedBuyer","false");
                        onItemCheckListener.onItemUncheck(currentItem);
                        onCheckChanged(position, false);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHoldeClass extends RecyclerView.ViewHolder {
        TextView name, price, dateof;
        LinearLayout linearLayout, linearLayoutCb, description;
        ImageView imgEditProperty, imgDelProperty;
        CheckBox checkBox;

        public ViewHoldeClass(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            dateof = itemView.findViewById(R.id.date);
            linearLayout = itemView.findViewById(R.id.linearLayoutParent);
            linearLayoutCb = itemView.findViewById(R.id.linearLayoutCheckBox);
            description = itemView.findViewById(R.id.description);
            imgEditProperty = itemView.findViewById(R.id.imgEditProperty);
            imgDelProperty = itemView.findViewById(R.id.imgDelProperty);
            checkBox = itemView.findViewById(R.id.checkBoxMainProp);
        }
    }

    private void deleteSellerData(final String id, final int position, String url) {
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
                        Toast.makeText(context, "item deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
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

    @TargetApi(Build.VERSION_CODES.N)
    private void onCheckChanged(int position, boolean checked) {
        checkBoxStates.put(position + "", checked);
    }
}
