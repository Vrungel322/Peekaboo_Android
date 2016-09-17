package com.peekaboo.presentation.adapters;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.peekaboo.R;
import com.peekaboo.presentation.widget.IndexScroller;
import com.peekaboo.presentation.widget.Record;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Record> records;
    private IndexScroller mScroller = null;
    private GestureDetector mGestureDetector = null;


    public RecyclerViewAdapter(List<Record> records) {
        this.records = records;
    }




    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Record record = records.get(i);
        int iconResourceId = 0;
//        switch (record.getType()) {
//            case GREEN:
////                iconResourceId = R.drawable.green_circle;
//                break;
//            case RED:
////                iconResourceId = R.drawable.red_circle;
//                break;
//            case YELLOW:
////                iconResourceId = R.drawable.yellow_circle;
//                break;
//        }
        iconResourceId = R.mipmap.ic_launcher;
        viewHolder.icon.setImageResource(iconResourceId);
        viewHolder.name.setText(record.getName());
//        viewHolder.deleteButtonListener.setRecord(record);
//        viewHolder.copyButtonListener.setRecord(record);
//        mScroller.draw(canvas);
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

//    private void copy(Record record) {
//        int position = records.indexOf(record);
//        Record copy = record.copy();
//        records.add(position + 1, copy);
//        notifyItemInserted(position + 1);
//    }
//
//    private void delete(Record record) {
//        int position = records.indexOf(record);
//        records.remove(position);
//        notifyItemRemoved(position);
//    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private ImageView icon;
//        private Button deleteButton;
//        private Button copyButton;
//        private DeleteButtonListener deleteButtonListener;
//        private CopyButtonListener copyButtonListener;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.recyclerViewItemName);
            icon = (ImageView) itemView.findViewById(R.id.ivDialog_contact);
//            deleteButton = (Button) itemView.findViewById(R.id.recyclerViewItemDeleteButton);
//            copyButton = (Button) itemView.findViewById(R.id.recyclerViewItemCopyButton);
//            deleteButtonListener = new DeleteButtonListener();
//            copyButtonListener = new CopyButtonListener();
//            deleteButton.setOnClickListener(deleteButtonListener);
//            copyButton.setOnClickListener(copyButtonListener);
//            mScroller.draw(canvas);
        }
    }

//    private class CopyButtonListener implements View.OnClickListener {
//        private Record record;
//
//        @Override
//        public void onClick(View v) {
//            copy(record);
//        }
//
//        public void setRecord(Record record) {
//            this.record = record;
//        }
//    }
//
//    private class DeleteButtonListener implements View.OnClickListener {
//        private Record record;
//
//        @Override
//        public void onClick(View v) {
//            delete(record);
//        }
//
//        public void setRecord(Record record) {
//            this.record = record;
//        }
//    }

    public void draw(Canvas canvas) {
//        super.draw(canvas);

        // Overlay index bar
        if (mScroller != null)
            mScroller.draw(canvas);
    }
    public void setAdapter(ContactsListAdapter adapter) {
//        super.setAdapter(adapter);
        if (mScroller != null)
            mScroller.setAdapter(adapter);
    }


    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
        if (mScroller != null)
            mScroller.onSizeChanged(w, h, oldw, oldh);
    }
}
