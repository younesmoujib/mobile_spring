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

import ma.ensa.volley.adapter.FiliereAdapter;
import ma.ensa.volley.adapter.RoleAdpater;
import ma.ensa.volley.beans.Filiere;
import ma.ensa.volley.beans.Role;

public class ListeFiliereActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    private List<Filiere> filieres;
    private ArrayList<String> filiereList = new ArrayList<>();
    private RecyclerView recyclerView;
    private FiliereAdapter filiereAdapter;
    private ImageView menu;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_filiere);

        recyclerView=findViewById(R.id.recylerview4);
        getFilieres();
        menu =findViewById(R.id.menu1);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
    }

    private void getFilieres() {
        String filieresUrl = "http://10.0.2.2:8082/api/filieres";

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, filieresUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            filieres = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject filiereObject = response.getJSONObject(i);
                                int id = filiereObject.getInt("id");
                                String code = filiereObject.getString("code");
                                String Libelle =filiereObject.getString("libelle");
                                Filiere filiere = new Filiere(id, code,Libelle);
                                filieres.add(filiere);
                                filiereList.add(code);
                            }
                            filiereAdapter = new FiliereAdapter(ListeFiliereActivity.this,filieres);
                            recyclerView.setAdapter(filiereAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(ListeFiliereActivity.this));


                            filiereAdapter.setOnItemClickListener((view, position) -> {

                                Filiere filiereSelectd = filieres.get(position);
                                // Handle the item click here
                                // For example, show a dialog for delete or modify
                                AlertDialog.Builder builder = new AlertDialog.Builder(ListeFiliereActivity.this);
                                builder.setTitle("Choose an action")
                                        .setMessage("Do you want to delete or modify the Filiere?")
                                        .setPositiveButton("Delete", (dialog, which) -> {
                                            deleteFiliere(filiereSelectd);
                                            filieres.remove(filiereSelectd);
                                            recyclerView.setAdapter(filiereAdapter);
                                            recyclerView.setLayoutManager(new LinearLayoutManager(ListeFiliereActivity.this));

                                        })
                                        .setNegativeButton("Modify", (dialog, which) -> {
                                            showEditDialog(filiereSelectd);

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

    public void deleteFiliere(Filiere filiere){
        int filiereId = filiere.getId();
        String url = "http://10.0.2.2:8082/api/filieres/" + filiereId;

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
                        Log.e("DELETE", "Error deleting Filiere: " + error.toString());
                    }
                });


        requestQueue.add(deleteRequest);
    }

    public void showEditDialog(Filiere filiere){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modification de la filiere : " + filiere.getCode());

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText codeInput = new EditText(this);
        codeInput.setHint("Code");
        codeInput.setText(filiere.getCode());
        layout.addView(codeInput);

        final EditText libelleInput = new EditText(this);
        libelleInput.setHint("Libelle");
        libelleInput.setText(filiere.getLibelle());
        layout.addView(libelleInput);



        builder.setView(layout);

        builder.setPositiveButton("Enregistrer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newcode = codeInput.getText().toString();
                String newlibelle = libelleInput.getText().toString();


                Filiere updatedFiliere = new Filiere(
                        filiere.getId(),newcode,newlibelle

                );

                updateFiliere(updatedFiliere);
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
    public void updateFiliere(Filiere filiere){
        String url = "http://10.0.2.2:8082/api/filieres/" + filiere.getId();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", filiere.getId());
            jsonObject.put("code", filiere.getCode());
            jsonObject.put("libelle", filiere.getLibelle());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Filiere updated successfully");
                        getFilieres();
                        recyclerView.setAdapter(filiereAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ListeFiliereActivity.this));
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