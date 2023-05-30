package com.example.btl_appnotes.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.btl_appnotes.Model.Notes;
import com.example.btl_appnotes.R;
import com.example.btl_appnotes.ViewModel.NotesViewModel;
import com.example.btl_appnotes.databinding.ActivityUpdateNotesBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.InputStream;
import java.util.Date;

public class UpdateNotesActivity extends AppCompatActivity {
    ActivityUpdateNotesBinding bingding;
    NotesViewModel noteViewModel;
    String priority = "1";
    String stitle,ssubtile,snotes,spriorty;
    String snote_image;
    ImageView upnote_image;
    String selectedImagePath;
    private static final int REQUEST_CODE_STORAGE_PERMISSION =1;
    private static final int REQUEST_CODE_SELECT_IMAGE =2;
    int iid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bingding = ActivityUpdateNotesBinding.inflate(getLayoutInflater());
        setContentView(bingding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        upnote_image = findViewById(R.id.upnoteimage);
        iid = getIntent().getIntExtra("id",0);
        stitle = getIntent().getStringExtra("title");
        ssubtile = getIntent().getStringExtra("subtitle");
        spriorty = getIntent().getStringExtra("priorty");
        snotes = getIntent().getStringExtra("note");
        selectedImagePath = getIntent().getStringExtra("image");
        bingding.upTitle.setText(stitle);
        bingding.upSubtitle.setText(ssubtile);
        bingding.upData.setText(snotes);
        bingding.upnoteimage.setImageBitmap(BitmapFactory.decodeFile(selectedImagePath));
        if (spriorty.equals("1")){
            bingding.greenPriority.setImageResource(R.drawable.ic_baseline_done_24);
        }else if (spriorty.equals("2")){
            bingding.yellowPriority.setImageResource(R.drawable.ic_baseline_done_24);
        }else if (spriorty.equals("3")){
            bingding.redPriority.setImageResource(R.drawable.ic_baseline_done_24);
        }


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

        bingding.updateNotesBtn.setOnClickListener(v -> {
            String title = bingding.upTitle.getText().toString();
            String subtitle = bingding.upSubtitle.getText().toString();
            String notes = bingding.upData.getText().toString();
            UpdateNotes(title, subtitle, notes);
        });

        bingding.upImage.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(
                    getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ){
                ActivityCompat.requestPermissions(
                        UpdateNotesActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_STORAGE_PERMISSION
                );
            }
            else{
                selectImage();
            }
        });
    }

    private void UpdateNotes(String title, String subtitle, String notes) {

        Date date = new Date();
        CharSequence sequence =DateFormat.format("EEEE,dd MMMM yyyy HH:mm a",date.getTime());
        Notes updateNotes = new Notes();
        updateNotes.id = iid;
        updateNotes.notesTitle = title;
        updateNotes.notesSubtitle = subtitle;
        updateNotes.notes = notes;
        updateNotes.notesPriorty = priority;
        updateNotes.notesDate = sequence.toString();
        updateNotes.notesImage = selectedImagePath;
        noteViewModel.updateNotes(updateNotes);
        Toast.makeText(this, "Notes Update Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.delete_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId()==R.id.ic_delete){
            BottomSheetDialog sheetDialog = new BottomSheetDialog(UpdateNotesActivity.this,
                    R.style.BottomSheetStyle);
            View view = LayoutInflater.from(UpdateNotesActivity.this).
                    inflate(R.layout.delete_bottom_sheet, (LinearLayout) findViewById(R.id.bottomSheet));
            sheetDialog.setContentView(view);

            TextView yes,no;
            yes = view.findViewById(R.id.delete_yes);
            no = view.findViewById(R.id.delete_no);

            yes.setOnClickListener(v -> {
                noteViewModel.deleteNotes(iid);
                finish();
            });
            no.setOnClickListener(v -> {
                sheetDialog.dismiss();
            });

            sheetDialog.show();
        }
        return true;
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
                       upnote_image.setImageBitmap(bitmap);
                       upnote_image.setVisibility(View.VISIBLE);
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