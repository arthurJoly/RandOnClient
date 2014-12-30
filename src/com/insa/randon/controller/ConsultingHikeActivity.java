package com.insa.randon.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.insa.randon.R;
import com.insa.randon.model.Comment;
import com.insa.randon.model.Hike;

public class ConsultingHikeActivity extends BaseActivity {
	Hike hike;
	Context context;
	
	private TextView nameTextView;
	private TextView distanceTextView;
	private ListView commentsListView ;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulting_hike);
        context=this;
        
        Intent intent = getIntent();
        hike = (Hike)intent.getParcelableExtra(MapActivity.EXTRA_HIKE);
        
        commentsListView = (ListView) findViewById(R.id.comments_list);       
        nameTextView = (TextView) findViewById(R.id.name_textView);
        distanceTextView = (TextView) findViewById(R.id.distance_textView);
        nameTextView.setText(hike.getName());
        distanceTextView.setText(String.valueOf(hike.getDistance()));
        
        //TEST COMMENTS LIST
        // we can live this feature apart! We won't have time to develop it
        //TODO : replace by hike.getComments();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        
        List<Comment> comments = new ArrayList<Comment>();
        comments.add(new Comment("Trop Bien", "rando bien",formattedDate));
        comments.add(new Comment("Trop cool", "rando cool",formattedDate));
        comments.add(new Comment("Trop g�nial", "rando g�niale",formattedDate));
        
        //Set up the hikes list
        CommentsListAdapter customAdapter = new CommentsListAdapter(context, R.layout.search_list_item, comments);
        commentsListView.setAdapter(customAdapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.consulting_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_download_hike:
            	
            default:
                return super.onOptionsItemSelected(item);
        }
    }

	
	//--------------- LIST ADAPTER ----------------------------
	public class CommentsListAdapter extends ArrayAdapter<Comment> {
	
		public CommentsListAdapter(Context context, int textViewResourceId) {
		    super(context, textViewResourceId);
		}
	
		public CommentsListAdapter(Context context, int resource, List<Comment> items) {
		    super(context, resource, items);
		}
	
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
	
		    View view = convertView;
	
		    if (view == null) {
	
		        LayoutInflater vi;
		        vi = LayoutInflater.from(getContext());
		        view = vi.inflate(R.layout.comments_list_item, null);
		    }
	
		    Comment comment = (Comment) getItem(position);
	
		    if (comment != null) {
		        TextView contentTextView = (TextView) view.findViewById(R.id.content_item);
		        TextView titleTextView = (TextView) view.findViewById(R.id.title_item);
		        TextView dateTextView = (TextView) view.findViewById(R.id.date_item);
		        if (contentTextView != null) {
		        	contentTextView.setText(comment.getContent());
		        }
		        if (titleTextView != null) {
		        	titleTextView.setText(comment.getTitle());
		        }
		        if (dateTextView != null) {
		        	dateTextView.setText(comment.getDate());
		        }
		    }
		    return view;
		}
	}
}

