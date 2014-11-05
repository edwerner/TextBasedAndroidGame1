package com.movie.locations.application;

import com.movie.locations.R;
import com.movie.locations.domain.MoviePostersHashMap;
import com.movie.locations.domain.User;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class UserWidgetFragment extends Fragment {
	
	public void onCreate(Bundle savedInstanceState){
		   super.onCreate(savedInstanceState);
		   setHasOptionsMenu(true);
		}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {

		// get intent string attributes
//    	Bundle bundle = getArguments();
//    	
//    	
//    	if (bundle != null) {
//    	
//    	}
        return inflater.inflate(R.layout.fragment_user_widget, container, false);
    }
}