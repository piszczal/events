package com.example.szymon.events_mobile.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.szymon.events_mobile.Model.Comment;
import com.example.szymon.events_mobile.R;

import java.util.List;

/**
 * Created by Szymon on 07.05.2017.
 */

public class CommentsListAdapter extends ArrayAdapter<Comment> {

    private Context context;
    private List<Comment> commentsList;

    public CommentsListAdapter(Context context, int resource, List<Comment> objects){
        super(context,resource,objects);
        this.context = context;
        this.commentsList = objects;
    }

    @Override
    public int getCount(){
        return commentsList.size();
    }

    @Override
    public Comment getItem(int position){
        return commentsList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.comment_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.content.setText(getItem(position).getContent());
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(getItem(position).getContent().charAt(0));
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(String.valueOf(getItem(position).getContent().charAt(0)), color);
        //holder.image.setImageDrawable(drawable);
        holder.rating.setRating(getItem(position).getRating());
        return convertView;
    }

    private class ViewHolder {

        private TextView content;
        //private ImageView image;
        private RatingBar rating;

        public ViewHolder(View v){
            content = (TextView) v.findViewById(R.id.comment_content);
            // image = (ImageView) v.findViewById(R.id.comment_image);
            rating = (RatingBar) v.findViewById(R.id.comment_rating);
        }
    }
}
