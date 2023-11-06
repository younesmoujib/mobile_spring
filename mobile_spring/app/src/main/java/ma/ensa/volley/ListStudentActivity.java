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

import ma.ensa.volley.adapter.StudentAdapter;
import ma.ensa.volley.beans.Student;

public class ListStudentActivity extends AppCompatActivity {
    private List<Student> students;
    private List<String> studentList = new ArrayList<>();
    RequestQueue requestQueue;

    private RecyclerView recyclerView;
    private StudentAdapter studentAdapter;
    private ImageView menu;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_student);

        recyclerView = findViewById(R.id.Recycleview2);
        getStudent();
        menu =findViewById(R.id.menu1);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), addStudentActivity.class);
                startActivity(i);
            }
        });


    }

    public void getStudent() {

        String studentUrl = "http://10.0.2.2:8082/api/students";
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, studentUrl, null,
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
                                int phone = studentObject.getInt("phone");
                                Student student = new Student(id, email, name, phone);
                                students.add(student);
                                studentList.add(name);
                            }

                            Log.d("re", response.toString());


                            studentAdapter = new StudentAdapter(ListStudentActivity.this, students);
                            recyclerView.setAdapter(studentAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(ListStudentActivity.this));


                            studentAdapter.setOnItemClickListener((view, position) -> {

                                Student studentSelectd = students.get(position);

                                AlertDialog.Builder builder = new AlertDialog.Builder(ListStudentActivity.this);
                                builder.setTitle("Choose an action")
                                        .setMessage("Do you want to delete or modify the student?")
                                        .setPositiveButton("Delete", (dialog, which) -> {
                                            deleteStudent(studentSelectd);
                                            students.remove(studentSelectd);
                                            recyclerView.setAdapter(studentAdapter);
                                            recyclerView.setLayoutManager(new LinearLayoutManager(ListStudentActivity.this));

                                        })
                                        .setNegativeButton("Modify", (dialog, which) -> {
                                            showEditDialog(studentSelectd);

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

    public void deleteStudent(Student student) {

        int studentId = student.getId();
        String url = "http://10.0.2.2:8082/api/students/" + studentId;

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

                        Log.e("DELETE", "Error deleting student: " + error.toString());
                    }
                });


        requestQueue.add(deleteRequest);
    }

    private void showEditDialog(final Student student) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modification de l'étudiant : " + student.getName());

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText nomInput = new EditText(this);
        nomInput.setHint("Nom");
        nomInput.setText(student.getName());
        layout.addView(nomInput);

        final EditText emailInput = new EditText(this);
        emailInput.setHint("Email");
        emailInput.setText(student.getEmail());
        layout.addView(emailInput);

        final EditText phoneInput = new EditText(this);
        phoneInput.setHint("Phone :");
        phoneInput.setText(String.valueOf(student.getPhone()));
        layout.addView(phoneInput);

        builder.setView(layout);

        builder.setPositiveButton("Enregistrer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newNom = nomInput.getText().toString();
                String newEmail = emailInput.getText().toString();
                String newPhone = phoneInput.getText().toString();

                Student updatedStudent = new Student(
                        student.getId(),
                        newEmail,
                        newNom,
                        Integer.parseInt(newPhone)
                );

                updateStudent(updatedStudent);
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

    private void updateStudent(Student student) {
        String url = "http://10.0.2.2:8082/api/students/" + student.getId();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", student.getId());
            jsonObject.put("name", student.getName());
            jsonObject.put("email", student.getEmail());
            jsonObject.put("phone", student.getPhone());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "Student updated successfully");
                        getStudent();
                        recyclerView.setAdapter(studentAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(ListStudentActivity.this));

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error updating student: " + error.toString());

                    }
                });

        requestQueue.add(request);



    }

}