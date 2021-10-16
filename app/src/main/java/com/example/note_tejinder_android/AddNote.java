package com.example.note_tejinder_android;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.note_tejinder_android.model.Note;
import com.example.note_tejinder_android.model.NoteViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddNote extends AppCompatActivity {

//    public static String NOTE_TITLE = "noteTitle";
//    public static String NOTE_DETAIL = "noteDetail";

    public byte[] img = null;

    private static final int REQUEST_CODE = 1;
    EditText noteTitle, noteDetail;
    Button saveNote, back;
    ImageView imgUpload;
    TextView address;
    List<Address> addresses;

    Boolean imgset = false;

    NoteViewModel noteViewModel;

    private static final int UPDATE_INTERVAL = 5000; // 5 seconds
    private static final int FASTEST_INTERVAL = 1000; // 3 seconds
    private static final int SMALLEST_DISPLACEMENT = 200; // 200 meters

    private List<String> permissionsToRequest;
    private List<String> permissions = new ArrayList<>();
    private List<String> permissionsRejected = new ArrayList<>();

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    Geocoder geocoder;
    LatLng latLangNote,latLangNote1 = null;
    double lat;
    double log;

    int noteCategoryID1;


    int noteID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        noteTitle = findViewById(R.id.noteTitle);
        noteDetail = findViewById(R.id.noteDetail);
        saveNote = findViewById(R.id.saveNote);
        back = findViewById(R.id.backtToNotes);
        imgUpload = findViewById(R.id.noteImage);
        address = findViewById(R.id.address);


        ///Set values-----------------------------------------------------------
        noteCategoryID1 = getIntent().getIntExtra("noteCategoryID", 0);
        noteID = getIntent().getIntExtra("noteID",0);
        if(noteID>0){
            latLangNote = new LatLng(getIntent().getDoubleExtra("latitude" , 0), getIntent().getDoubleExtra("longitutde" , 0));

            noteTitle.setText(getIntent().getStringExtra("noteTitle"));
            noteDetail.setText(getIntent().getStringExtra("noteDetail"));

            byte[] noteImageArr = getIntent().getByteArrayExtra("noteImage");
            if(noteImageArr != null){
                Drawable image = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(noteImageArr, 0, noteImageArr.length));
                imgUpload.setImageDrawable(image);
                imgset = true;
            }

        }


        ///-----------------------------------------------------------------------



        noteViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(NoteViewModel.class);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
            }
        });

        imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        // instantiate the fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // add permissions

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        permissionsToRequest = permissionsToRequest(permissions);
        if (permissionsToRequest.size() > 0) {
            requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), REQUEST_CODE);
        }


    }

    private List<String> permissionsToRequest(List<String> permissions) {
        ArrayList<String> results = new ArrayList<>();
        for (String perm: permissions) {
            if (!hasPermission(perm))
                results.add(perm);
        }

        return results;
    }

    private boolean hasPermission(String perm) {
        return checkSelfPermission(perm) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        int errorCode = GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(this);

        if (errorCode != ConnectionResult.SUCCESS) {
            Dialog errorDialog = GoogleApiAvailability.getInstance()
                    .getErrorDialog(this, errorCode, errorCode, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            Toast.makeText(AddNote.this, "No Services", Toast.LENGTH_SHORT).show();
                        }
                    });
            errorDialog.show();
        } else {
            Log.i("TAG", "onPostResume: ");
            startUpdateLocation();
        }
    }

    private void findLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        address.setText(String.format("Lat: %s, Lng: %s", location.getLatitude(), location.getLongitude()));
                    }
                }
            });
        }

        startUpdateLocation();
    }

    @SuppressLint("MissingPermission")
    private void startUpdateLocation() {
        Log.d("TAG", "startUpdateLocation: ");
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setSmallestDisplacement(SMALLEST_DISPLACEMENT);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault()); // sets the geocoder object

                if (locationResult != null) {
                    Location location = locationResult.getLastLocation();
                    try {
                        if(latLangNote == null)
                            latLangNote = new LatLng(location.getLatitude(), location.getLongitude());

                        addresses = geocoder.getFromLocation(latLangNote.latitude, latLangNote.longitude, 1);
                        String address1  = ""; // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                        if (addresses != null && addresses.size() > 0) { // if the addressList gets a result
                            address1 = ""; // empty the address message
                            // street name
                            if (addresses.get(0).getThoroughfare() != null) // if there is a street name
                                address1 += addresses.get(0).getThoroughfare() + ", "; // add the street name
                            if (addresses.get(0).getPostalCode() != null)  // if there is a postal code name
                                address1 += addresses.get(0).getPostalCode() + ", "; // add the postal code name
                            if (addresses.get(0).getLocality() != null)  // if there is a city name
                                address1 += addresses.get(0).getLocality() + ", "; // add the city name
                            if (addresses.get(0).getAdminArea() != null)  // if there is a province name
                                address1 += addresses.get(0).getAdminArea(); // add the province name

                        }
                        address.setText(address1);
                    } catch (Exception e) {
                        e.printStackTrace(); // catch the error
                    }
                }
            }
        };
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            for (String perm: permissions) {
                if (!hasPermission(perm))
                    permissionsRejected.add(perm);
            }

            if (permissionsRejected.size() > 0 ) {
                if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                    new AlertDialog.Builder(AddNote.this)
                            .setMessage("The location permission is mandatory")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), REQUEST_CODE);
                                    }

                                }
                            }).setNegativeButton("Cancel", null)
                            .create()
                            .show();
                }
            }else {
                startUpdateLocation();
            }
        }
    }
    public void pickImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        imageActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> imageActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK){
                        Intent data = result.getData();
                        imgUpload.setImageURI(data.getData());

                        imgset = true;

                    }
                }
            });



    public void addNote(){
        String title = noteTitle.getText().toString().trim();
        String detail = noteDetail.getText().toString().trim();

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM, yyyy hh:mm aa");
        String date1 = dateFormat.format(date);


        if(title.isEmpty()){
            noteTitle.setError("Please fill");
            noteTitle.requestFocus();
            return;
        }
        if(detail.isEmpty()){
            noteDetail.setError("Please fill");
            noteDetail.requestFocus();
            return;
        }

        if(imgset){
            //image into byte

            Bitmap bitmap = ((BitmapDrawable) imgUpload.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, baos);
            img = baos.toByteArray();
        }

        if(noteID>0){
            noteViewModel.getNoteById(noteID).observe(AddNote.this,note -> {
                note.setNoteCategoryID(noteCategoryID1);
                note.setNoteTitle(title);
                note.setNoteDetail(detail);
                note.setNoteImage(img);
                note.setLatitude(latLangNote.latitude);
                note.setLongitude(latLangNote.longitude);
                noteViewModel.update(note);
            });
        }
        else{
            noteViewModel.insertNote(new Note(noteCategoryID1,title,detail,date1,img,log,lat));
//            System.out.println("Hello");
//            Intent intent = new Intent();
//            intent.putExtra(NOTE_TITLE,title);
//            intent.putExtra(NOTE_DETAIL,detail);
//            setResult(RESULT_OK,intent);
        }

        finish();
    }
}