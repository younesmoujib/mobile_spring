package ma.ensa.volley;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ma.ensa.volley.beans.Filiere;

public class addStudentActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText username,password,fullname,email,phone;
    private Spinner filiere;
    private Button addS;
    RequestQueue requestQueue;
    private List<Filiere> filieres;
    private ArrayList<String> filiereList = new ArrayList<>();
    private ArrayAdapter<String> spinnerAdapter;
     private ImageView menu;

    String insertUrl = "http://10.0.2.2:8082/api/students";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        fullname=findViewById(R.id.fname);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.phone);
        filiere=findViewById(R.id.spfiliere);
        addS=findViewById(R.id.btnS);
        addS.setOnClickListener(this);

        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filiereList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filiere.setAdapter(spinnerAdapter);
        getFilieresFromAPI();

        menu =findViewById(R.id.menu1);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(i);
            }
        });



    }




    @Override
    public void onClick(View v) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", username.getText().toString() );
            jsonBody.put("password", password.getText().toString() );
            jsonBody.put("name", fullname.getText().toString() );
            jsonBody.put("email", email.getText().toString() );
            jsonBody.put("phone", phone.getText().toString() );
            JSONObject filiereObject = new JSONObject();
            int selectedFiliereId = filieres.get(filiere.getSelectedItemPosition()).getId();
            filiereObject.put("id",selectedFiliereId );


            jsonBody.put("filiere", filiereObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                insertUrl, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("resultat", response+"");
                Intent i = new Intent(getApplicationContext(), ListStudentActivity.class);
                startActivity(i);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Erreur", error.toString());
            }
        });
        requestQueue.add(request);

    }
    private void getFilieresFromAPI() {
        String filieresUrl = "http://10.0.2.2:8082/api/filieres";

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, filieresUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            filieres = new ArrayList<>(); // Initialiser la liste de filières

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject filiereObject = response.getJSONObject(i);
                                int id = filiereObject.getInt("id");
                                String code = filiereObject.getString("code");
                                Filiere filiere = new Filiere(id, code);
                                filieres.add(filiere);
                                filiereList.add(code);
                            }

                            spinnerAdapter.notifyDataSetChanged();
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

}