package com.example.btl_appnotes.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.btl_appnotes.R;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.btl_appnotes.Model.Notes;
import com.example.btl_appnotes.ViewModel.NotesViewModel;
import com.example.btl_appnotes.databinding.ActivityInsertNotesBinding;

import java.io.InputStream;
import java.util.Date;

public class InsertNotesActivity extends AppCompatActivity {

    ActivityInsertNotesBinding bingding;
    String title, subtitle, notes;
    NotesViewModel noteViewModel;
    String priority = "1";
    String selectedImagePath;
    public boolean pinned = false;
    private static final int REQUEST_CODE_STORAGE_PERMISSION =1;
    private static final int REQUEST_CODE_SELECT_IMAGE =2;

    LinearLayout ab;
    private EditText notesTitle, notesSubtitle, notesData;
    ImageView note_image;
    ImageView addImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bingding = ActivityInsertNotesBinding.inflate(getLayoutInflater());
        setContentView(bingding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        note_image = findViewById(R.id.note_image);
        selectedImagePath = "";
        noteViewModel = new ViewModelProvider(this).get(NotesViewModel.class);
        bingding.greenPriority.setOnClickListener(v->{
            bingding.greenPriority.setImageResource(R.drawable.ic_baseline_done_24);
            bingding.yellowPriority.setImageResource(0);
            bingding.redPriority.setImageResource(0);
            priority = "1";
        });
        bingding.yellowPriority.setOnClickListener(v->{
            bingding.greenPriority.setImageResource(0);
            bingding.yellowPriority.setImageResource(R.drawable.ic_baseline_done_24);
            bingding.redPriority.setImageResource(0);
            priority = "2";
        });
        bingding.redPriority.setOnClickListener(v->{
            bingding.greenPriority.setImageResource(0);
            bingding.yellowPriority.setImageResource(0);
            bingding.redPriority.setImageResource(R.drawable.ic_baseline_done_24);
            priority = "3";
        });
        bingding.addImage.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(
                    getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ){
                ActivityCompat.requestPermissions(
                        InsertNotesActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_STORAGE_PERMISSION
                );
            }
            else{
                selectImage();
            }
        });

        bingding.doneNotesBtn.setOnClickListener(v->{
                title = bingding.notesTitle.getText().toString();
                subtitle = bingding.notesSubtitle.getText().toString();
                notes = bingding.notesData.getText().toString();
                if (title.isEmpty()){
                    Toast.makeText(this, "Need to enter title", Toast.LENGTH_SHORT).show();
                }
                else{
                    CreateNote(title, subtitle, notes);
                }

                
        });


    }
    private void CreateNote(String title, String subtitle, String notes) {
        Date date = new Date();
        CharSequence sequence = DateFormat.format("EEEE,dd MMMM yyyy HH:mm a",date.getTime());
        Notes notes1 = new Notes();
        notes1.notesTitle = title;
        notes1.notesSubtitle = subtitle;
        notes1.notes = notes;
        notes1.notesPriorty = priority;
        notes1.notesDate = sequence.toString();
        notes1.pinned = pinned;
        notes1.notesImage = selectedImagePath;
        noteViewModel.insertNotes(notes1);
        Toast.makeText(this, "Notes Created Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
    private void selectImage(){
        Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if(intent1.resolveActivity(getPackageManager()) !=null){
            startActivityForResult(intent1,REQUEST_CODE_SELECT_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length >0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                selectImage();
            }
            else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK){
            if (data != null){
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null){
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        note_image.setImageBitmap(bitmap);
                        note_image.setVisibility(View.VISIBLE);
                        selectedImagePath = getPathFromUri(selectedImageUri);

                    }catch (Exception exception){
                        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
    String getPathFromUri(Uri contentUri){
        String filePath;
        Cursor cursor = getContentResolver().query(contentUri, null,null, null,null);
        if (cursor == null){
            filePath = contentUri.getPath();
        }
        else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }
}