package ma.ensa.volley;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import ma.ensa.volley.beans.Role;
import ma.ensa.volley.beans.Student;

public class AddStudentRoleActivity extends AppCompatActivity implements View.OnClickListener{

    private Spinner spinnerRole,spinnerStudent;
    RequestQueue requestQueue;
    private List<Role> roles;
    private List<String> roleList=new ArrayList<>();

    private List<Student> students;
    private List<String> studentList =new ArrayList<>();
    private ArrayAdapter<String> roleAdapter;
    private ArrayAdapter<String> studentAdapter;
    String insertUrl = "http://10.0.2.2:8082/api/students/add";
    private ImageView menu;

    private Button adRS;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student_role);
        spinnerRole =findViewById(R.id.sRole);
        spinnerStudent=findViewById(R.id.sStudent);
        adRS=findViewById(R.id.btnRS);

        adRS.setOnClickListener(this);





         studentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, studentList);
        studentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStudent.setAdapter(studentAdapter);

       roleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roleList);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(roleAdapter);

        getRole();
        getStudent();

        menu =findViewById(R.id.menu1);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(i);
            }
        });




    }

    public void getStudent(){

        String studentUrl="http://10.0.2.2:8082/api/students";
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,studentUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            students = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject studentObject = response.getJSONObject(i);
                                int id = studentObject.getInt("id");
                                String name = studentObject.getString("name");
                                Student student = new Student(id, name);
                                students.add(student);
                                studentList.add(name);
                            }

                            studentAdapter.notifyDataSetChanged();

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

                            roleAdapter.notifyDataSetChanged();
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



    public void onClick(View v) {
        JSONObject jsonBody = new JSONObject();
        try {
            // Récupérer l'ID de l'étudiant sélectionné
            int selectedStudentId = students.get(spinnerStudent.getSelectedItemPosition()).getId();
            jsonBody.put("id", selectedStudentId);

            // Créer un objet de rôle avec l'ID du rôle sélectionné
            JSONObject roleObject = new JSONObject();
            int selectedRoleId = roles.get(spinnerRole.getSelectedItemPosition()).getId();
            roleObject.put("id", selectedRoleId);

            // Créer un tableau JSON de rôles et y ajouter le rôle sélectionné
            JSONArray rolesArray = new JSONArray();
            rolesArray.put(roleObject);

            // Ajouter le tableau de rôles à l'objet JSON principal
            jsonBody.put("roles", rolesArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Créer la requête JSON avec les données préparées
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                insertUrl, jsonBody, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("resultat", response + "");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Erreur", error.toString());
            }
        });
        requestQueue.add(request);
    }



}