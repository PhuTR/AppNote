package com.example.btl_appnotes.Adapter;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.btl_appnotes.Activity.InsertNotesActivity;
import com.example.btl_appnotes.Activity.UpdateNotesActivity;
import com.example.btl_appnotes.Dao.NotesDao;
import com.example.btl_appnotes.MainActivity;
import com.example.btl_appnotes.Model.Notes;
import com.example.btl_appnotes.R;
import com.example.btl_appnotes.ViewModel.NotesViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NonNls;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.notesViewholder> {
    MainActivity mainActivity;
    List<Notes> notes;
    List<Notes> allNotesitem;
    List<Notes> filternotesalllist;
    NotesViewModel noteViewModel;
    NotesAdapter adapter;
    Notes selectNote;
    NotesDao notesDao;
    RecyclerView notesRecycler;
    int iid;

    public NotesAdapter(MainActivity mainActivity, List<Notes> notes) {
        this.mainActivity = mainActivity;
        this.notes = notes;
        allNotesitem = new ArrayList<>(notes);
    }

    public void searchNotes(List<Notes> filterredName){
        this.notes = filterredName;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public notesViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new notesViewholder(LayoutInflater.from(mainActivity).inflate(R.layout.item_notes, parent, false));
    }

    @Override
    public void onBindViewHolder(notesViewholder holder, int position) {

        noteViewModel = new ViewModelProvider(mainActivity).get(NotesViewModel.class);
        Notes note = notes.get(position);
        switch (note.notesPriorty) {
            case "1":
                holder.notesPriority.setBackgroundResource(R.drawable.green_shape);
                break;
            case "2":
                holder.notesPriority.setBackgroundResource(R.drawable.yellow_shape);
                break;
            case "3":
                holder.notesPriority.setBackgroundResource(R.drawable.red_shape);
                break;
        }
        holder.title.setText(note.notesTitle);
        holder.subtitle.setText(note.notesSubtitle);
        holder.notesDate.setText(note.notesDate);
        holder.noteImage.setImageBitmap(BitmapFactory.decodeFile(note.notesImage));


        holder.itemView.setOnLongClickListener(v -> {
            BottomSheetDialog sheetDialog = new BottomSheetDialog(mainActivity,
                    R.style.BottomSheetStyle);
            View view = LayoutInflater.from(mainActivity).
                    inflate(R.layout.nav_bottom_sheet, (LinearLayout) mainActivity.findViewById(R.id.bottomSheet));
            sheetDialog.setContentView(view);
            ImageView pin,delete;
            delete = view.findViewById(R.id.imageDelete);
            pin = view.findViewById(R.id.imagePin);
            pin.setOnClickListener(view1 -> {
                BottomSheetDialog sheetDialogs = new BottomSheetDialog(mainActivity,
                        R.style.BottomSheetStyle);
                if (note.pinned){
//                    noteViewModel.pinned.observe(mainActivity, new Observer<List<Notes>>() {
//                        @Override
//                        public void onChanged(List<Notes> notes) {
//                            mainActivity.setAdapter(notes);
//                            filternotesalllist = notes;
//                        }
//
//                    });
                    holder.pinned.setBackgroundResource(0);
                    Toast.makeText(mainActivity, "Unpinned!", Toast.LENGTH_SHORT).show();
                    note.pinned=false;

                }
                else {
//                    noteViewModel.pinned.observe(mainActivity, new Observer<List<Notes>>() {
//                        @Override
//                        public void onChanged(List<Notes> notes) {
//                            mainActivity.setAdapter(notes);
//                            filternotesalllist = notes;
//
//                        }
//
//                    });
                    Toast.makeText(mainActivity, "pinned!", Toast.LENGTH_SHORT).show();
                    holder.pinned.setBackgroundResource(R.drawable.ic_pin);
                    note.pinned=true;


                }



                sheetDialog.dismiss();

            });
           delete.setOnClickListener(view1 -> {
               BottomSheetDialog sheetDialogs = new BottomSheetDialog(mainActivity,
                       R.style.BottomSheetStyle);
               View views = LayoutInflater.from(mainActivity).
                       inflate(R.layout.delete_bottom_sheet, (LinearLayout) mainActivity.findViewById(R.id.bottomSheet));
               sheetDialogs.setContentView(views);
               TextView yes,no;
               no = views.findViewById(R.id.delete_no);
               yes = views.findViewById(R.id.delete_yes); 
               no.setOnClickListener(view2 -> {
                   sheetDialogs.dismiss();
                   sheetDialog.dismiss();
               });
               yes.setOnClickListener(view2 -> {
                   noteViewModel.deleteNotes(note.id);
                   sheetDialogs.dismiss();
                   sheetDialog.dismiss();
               });
               sheetDialogs.show();
           });
           
            sheetDialog.show();
            return  true;
        });


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mainActivity, UpdateNotesActivity.class);
            intent.putExtra("id",note.id);
            intent.putExtra("title",note.notesTitle);
            intent.putExtra("subtitle",note.notesSubtitle);
            intent.putExtra("priorty",note.notesPriorty);
            intent.putExtra("note",note.notes);
            intent.putExtra("image",note.notesImage);
            mainActivity.startActivity(intent);
        });
        
    }
    @Override
    public int getItemCount() {
        return notes.size();
    }

    static class notesViewholder extends RecyclerView.ViewHolder {
        TextView title, subtitle, notesDate;
        RoundedImageView noteImage;
        View notesPriority;
        View pinned;


        public notesViewholder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.notesTitle);
            subtitle = itemView.findViewById(R.id.notesSubtitle);
            noteImage = itemView.findViewById(R.id.imageNote);
            notesDate = itemView.findViewById(R.id.notesDate);
            notesPriority = itemView.findViewById(R.id.notesPriority);
            pinned = itemView.findViewById(R.id.notespin);
        }
    }

    
}
