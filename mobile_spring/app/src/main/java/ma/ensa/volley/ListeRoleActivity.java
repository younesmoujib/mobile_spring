package ma.ensa.volley;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ma.ensa.volley.adapter.RoleAdpater;
import ma.ensa.volley.adapter.StudentAdapter;
import ma.ensa.volley.beans.Role;
import ma.ensa.volley.beans.Student;

public class ListeRoleActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    private List<Role> roles;
    private List<String> roleList=new ArrayList<>();
    private RecyclerView recyclerView;
    private RoleAdpater roleAdpater;
    private ImageView menu;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_role);

        recyclerView=findViewById(R.id.recylerview3);
        getRole();
        menu =findViewById(R.id.menu1);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), addRoleActivity.class);
                startActivity(i);
            }
        });


    }

    public void getRole(){
        String rolesUrl = "http://10.0.2.2:8082/api/roles";

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,rolesUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            roles = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject roleObject = response.getJSONObject(i);
                                int id = roleObject.getInt("id");
                                String name = roleObject.getString("name");
                                Role role = new Role(id, name);
                                roles.add(role);
                                roleList.add(name);
                            }
                            roleAdpater = new RoleAdpater(ListeRoleActivity.this, roles);
                            recyclerView.setAdapter(roleAdpater);
                            recyclerView.setLayoutManager(new LinearLayoutManager(ListeRoleActivity.this));


                            roleAdpater.setOnItemClickListener((view, position) -> {

                                Role roleSelectd = roles.get(position);

                                AlertDialog.Builder builder = new AlertDialog.Builder(ListeRoleActivity.this);
                                builder.setTitle("Choose an action")
                                        .setMessage("Do you want to delete or modify the Role?")
                                        .setPositiveButton("Delete", (dialog, which) -> {
                                            deleteRole(roleSelectd);
                                            roles.remove(roleSelectd);
                                            recyclerView.setAdapter(roleAdpater);
                                            recyclerView.setLayoutManager(new LinearLayoutManager(ListeRoleActivity.this));

                                        })
                                        .setNegativeButton("Modify", (dialog, which) -> {
                                            showEditDialog(roleSelectd);

                                        })
                                        .setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss())
                                        .show();
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Erreur", error.toString());
                    }
                });

        requestQueue.add(request);
    }

    public void deleteRole(Role role){
        int roleId = role.getId();
        String url = "http://10.0.2.2:8082/api/roles/" + roleId;

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("DELETE", response.toString());


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Log.e("DELETE", "Error deleting Role: " + error.toString());
                    }
                });


        requestQueue.add(deleteRequest);
    }
    public void showEditDialog(Role role){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modification de le role : " + role.getName());

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText nomInput = new EditText(this);
        nomInput.setHint("Nom");
        nomInput.setText(role.getName());
        layout.addView(nomInput);



        builder.setView(layout);

        builder.setPositiveButton("Enregistrer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newNom = nomInput.getText().toString();


                Role updatedRole = new Role(
                        role.getId(),

                        newNom

                );

                updateRole(updatedRole);
            }
        });

        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void updateRole(Role role){
        String url = "http://10.0.2.2:8082/api/roles/" + role.getId();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", role.getId());
            jsonObject.put("name", role.getName());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Role updated successfully");
                        getRole();
                        recyclerView.setAdapter(roleAdpater);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ListeRoleActivity.this));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error updating Role: " + error.toString());

                    }
                });

        requestQueue.add(request);
    }

}