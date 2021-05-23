package com.cswala.cswala.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cswala.cswala.Models.User;
import com.cswala.cswala.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class EditProfile extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private TextView etDate, txtdob;
    private EditText txtname, txtemail, txtphone;
    private Spinner txtgender;
    private Button save;
    private ImageView imageView;
    private Bitmap bitmap = null;

    DatabaseReference reference;
    StorageReference storageRef;


    String userid = FirebaseAuth.getInstance().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        etDate = findViewById(R.id.date);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        etDate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfile.this, R.style.DatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        String date = day + "/" + month + "/" + year;
                        etDate.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        txtname = findViewById(R.id.name_text);
        txtemail = findViewById(R.id.email_text);
        txtphone = findViewById(R.id.phone);
        txtgender = findViewById(R.id.spinner);
        txtdob = findViewById(R.id.date);
        imageView = findViewById(R.id.circleImageView);


        reference = FirebaseDatabase.getInstance().getReference();
        storageRef = FirebaseStorage.getInstance().getReference();

        Intent data = getIntent();
        String user_name = data.getStringExtra("name");
        String user_email = data.getStringExtra("email");
        String user_phn = data.getStringExtra("phone");
        String user_gndr = data.getStringExtra("gender");
        String user_dob = data.getStringExtra("dob");
        String imageLink = data.getStringExtra("image");
        if(!imageLink.equals("none")) {
            Uri uri = Uri.parse(imageLink);
            Picasso.get().load(uri).into(imageView);
        }



        txtname.setText(user_name);
        txtemail.setText(user_email);
        txtphone.setText(user_phn);
        if (user_gndr.equals("Female")) {
            Spinner colouredSpinner = findViewById(R.id.spinner);
            ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.gender2, R.layout.color_spinner_layout);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
            colouredSpinner.setAdapter(adapter);
            colouredSpinner.setOnItemSelectedListener(EditProfile.this);
        } else {
            Spinner colouredSpinner = findViewById(R.id.spinner);
            ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.gender1, R.layout.color_spinner_layout);
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
            colouredSpinner.setAdapter(adapter);
            colouredSpinner.setOnItemSelectedListener(EditProfile.this);
        }
        txtdob.setText(user_dob);


        save = findViewById(R.id.btnsave);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressBar progressbar = findViewById(R.id.progressBaredit);
                progressbar.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                Toast.makeText(EditProfile.this, "Saving Your changes...", Toast.LENGTH_SHORT).show();

                final String name = txtname.getText().toString().trim();
                final String email = txtemail.getText().toString().trim();
                final String phone = txtphone.getText().toString();
                final String gender = txtgender.getSelectedItem().toString();
                final String dob = txtdob.getText().toString();
                Drawable drawable = imageView.getDrawable();
                BitmapDrawable bitmapDrawable = ((BitmapDrawable) drawable);
                Bitmap img_bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                img_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte bb[] = baos.toByteArray();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
                final String img_string = sdf.format(new Date());


                StorageReference sr = storageRef.child(userid);
                sr.putBytes(bb).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(email)) {
                            Toast.makeText(EditProfile.this, "Please fill all fields.", Toast.LENGTH_SHORT).show();
                        } else {
                            User nuser = new User(img_string, name, email, phone, gender, dob);

                            Task initTask = reference.child("User").child(userid).setValue(nuser);
                            initTask.addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {

                                    Thread thread = new Thread() {
                                        @Override
                                        public void run() {
                                            try {
                                                Thread.sleep(5000);
                                            } catch (InterruptedException e) {
                                            }

                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    progressbar.setVisibility(View.GONE);
                                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                }
                                            });
                                        }
                                    };
                                    thread.start();

                                }
                            });
                            initTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EditProfile.this, "Unable to edit profile", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this, "Image failed to upload", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage(EditProfile.this);
            }
        });

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {

                    if (ContextCompat.checkSelfPermission(EditProfile.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(EditProfile.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(EditProfile.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 110);
                    } else {
                        takePicture();
                    }

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }


        });
        builder.show();
    }

    public void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 22);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 110) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takePicture();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {

            switch (requestCode) {
                case 22:
                    if (resultCode == RESULT_OK && data != null) {

                        Bundle extra = data.getExtras();
                        bitmap = (Bitmap) extra.get("data");
                        imageView.setImageBitmap(bitmap);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {

                        Uri uri = data.getData();
                        bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        imageView.setImageBitmap(bitmap);

                    }
                    break;
            }


        }
    }


}

