package ma.ensa.volley;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ma.ensa.volley.adapter.StudentAdapter;
import ma.ensa.volley.beans.Filiere;
import ma.ensa.volley.beans.Student;

public class ListeStudentFiliereActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner spinner;
    private Button btnS;
    RequestQueue requestQueue;
    private List<Filiere> filieres;
    private ArrayList<String> filiereList = new ArrayList<>();
    private ArrayAdapter<String> spinnerAdapter;
    private List<Student> students;
    private List<String> studentList =new ArrayList<>();
    private StudentAdapter studentAdapter;
    private RecyclerView recyclerView;
    private ImageView menu;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_student_filiere);

        spinner=findViewById(R.id.spinnerfiliere1);
        btnS =findViewById(R.id.btnAL);
        btnS.setOnClickListener(this);
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filiereList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        getFilieresFromAPI();

        recyclerView=findViewById(R.id.recyclerView1);

        menu =findViewById(R.id.menu1);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(i);
            }
        });



    }

    private void getFilieresFromAPI() {
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

    @Override
    public void onClick(View v) {


        int id = filieres.get(spinner.getSelectedItemPosition()).getId();
        String ListeStudentUrl ="http://10.0.2.2:8082/api/students/filiere/"+id;
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,ListeStudentUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            students = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject studentObject = response.getJSONObject(i);
                                int id = studentObject.getInt("id");
                                String name = studentObject.getString("name");
                                String email = studentObject.getString("email");
                                int phone =studentObject.getInt("phone");
                                Student student = new Student(id,email, name,phone);
                                students.add(student);
                                studentList.add(name);
                            }
                            Log.d("re",response.toString());
                            studentAdapter=new StudentAdapter(ListeStudentFiliereActivity.this,students);
                            recyclerView.setAdapter(studentAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(ListeStudentFiliereActivity.this));



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