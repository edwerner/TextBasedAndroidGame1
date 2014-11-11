package com.movie.locations.application;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

//import com.movie.locations.BagActivity;
import com.movie.locations.R;
import com.movie.locations.dao.MovieLocationsImpl;
import com.movie.locations.dao.QuizItemImpl;
import com.movie.locations.dao.UserImpl;
import com.movie.locations.domain.BagItem;
import com.movie.locations.domain.CheckIn;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.BagItemArrayList;
//import com.movie.locations.domain.BagItem;
import com.movie.locations.domain.QuizItem;
import com.movie.locations.domain.User;
import com.movie.locations.domain.WorldLocationArrayList;
import com.movie.locations.domain.WorldLocationObject;
import com.movie.locations.service.FilmLocationService;
import com.movie.locations.service.QuizItemService;
import com.movie.locations.service.WorldLocationService;
import com.movie.locations.util.StaticSortingUtilities;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class QuizActivity extends FragmentActivity {

	// // private Intent intent;
	private static String currentAnswerIndex;
	private static QuizItem quizItem;
	// private static String[] answersArray;
	// private static String movieTitle;
	private static Context context;
	// // private static RadioGroup radioGroup;
	// private static String position;
	// private static String createdAt;
	// private static String createdMeta;
	// private static String updatedAt;
	// private static String updatedMeta;
	// private static String meta;
	// private static String title;
	// private static String releaseYear;
	// private static String funFacts;
	// private static String productionCompany;
	// private static String distributor;
	// private static String director;
	// private static String writer;
	// private static String actor1;
	// private static String actor2;
	// private static String actor3;
	// private static String geolocation;
	// private static String locations;
	// private static String latitude;
	// private static String longitude;
	// private static String sid;
	// private static String id;
	private static String level;
	// private static String staticMapImageUrl;
	private static String questionText;
	private static String questionId;
	private static String dateTime;
	private static String answer1;
	private static String answer2;
	private static String answer3;
	private static String answer4;
	private static String reaction1;
	private static String reaction2;
	private static String reaction3;
	private static String reaction4;
	private static String worldId;
	private static String worldTitle;
	private static String submittedAnswerIndex;
	private static String answered;
	private static String activeItem;
	private static String activeItem1;
	private static String activeItem2;
	private static String activeItem3;
	private static String activeItem4;
	// private static String answered;
	private static String correctAnswerIndex;
	// private static QuizItemImpl quizitemsource;
	public static WorldLocationArrayList worldLocationList;
	public static WorldLocationArrayList reloadWorldLocationList;
	public static Intent intent;
	// public static QuizItem quizItem;

	private static QuizItemImpl quizitemsource;
	private static BagItem currentBagItem;

	private static final int MENU_ONE = Menu.FIRST;
	private static final int MENU_TWO = Menu.FIRST + 1;
	private static final int MENU_THREE = Menu.FIRST + 2;
	private static final int MENU_FOUR = Menu.FIRST + 3;

	public static ExpandableListView listView;
	private static LinearLayout layout;
	protected static ImageLoader imageLoader = ImageLoader.getInstance();
	private static ImageView currentEquippedItemImage;
	private static TextView currentEquippedItemText;
	
	private static BagItemArrayList bagItemArrayList;


	private static String[] reactions;
	private static String[] requiredItems;
	
	private static String quizItemSid;
	private static String currentUserId;
	


	private static UserImpl userSource;
	private static User currentUser;
	private static String currentPoints;
	private static String currentBonusPoints;
	private static int currentTotalPoints;


    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 4;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private static ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private static PagerAdapter mPagerAdapter;
	private static FragmentManager fragmentManager;
	private static String currentLevel;
    

	
	


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_screen_slide);
//
//        // Instantiate a ViewPager and a PagerAdapter.
//        mPager = (ViewPager) findViewById(R.id.pager);
//        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
//        mPager.setAdapter(mPagerAdapter);
//    }

//    @Override
//    public void onBackPressed() {
//        if (mPager.getCurrentItem() == 0) {
//            // If the user is currently looking at the first step, allow the system to handle the
//            // Back button. This calls finish() on this activity and pops the back stack.
//            super.onBackPressed();
//        } else {
//            // Otherwise, select the previous step.
//            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
//        }
//    }

//    /**
//     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
//     * sequence.
//     */
//    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
//        public ScreenSlidePagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return new ScreenSlidePageFragment();
//        }
//
//        @Override
//        public int getCount() {
//            return NUM_PAGES;
//        }
//    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);

		layout = (LinearLayout) findViewById(R.layout.activity_quiz);

		context = this;

		if (savedInstanceState == null) {
			// this is true because we're always
			// destroying this activity after
			// quiz activity is resolved

			// initialize database connection
			quizitemsource = new QuizItemImpl(context);

			// quizItem = new quizItem();
			intent = getIntent();

			// get intent string attributes
			Bundle extras = intent.getExtras();

			quizItem = extras.getParcelable("quizItem");
			System.out.println("PARCEL QUIZ ITEM CORRECT ANSWER INDEX: " + quizItem.getCorrectAnswerIndex());
			System.out.println("PARCEL QUIZ ITEM TEXT: " + quizItem.getQuestionText());
			bagItemArrayList = extras.getParcelable("bagItemArrayList");
//			System.out.println("BAG ITEM LIST SIZE: " + bagItemArrayList.getBagItemArrayList().size());
			quizItemSid = extras.getString("quizItemSid");
			currentUserId = extras.getString("currentUserId");
			
			// select current user so we can update current xp
			userSource = new UserImpl(context);
//			currentUser = userSource.selectRecordById(currentUserId);
			currentUser = extras.getParcelable("currentUser");
			currentPoints = currentUser.getCurrentPoints();
			currentBonusPoints = currentUser.getBonusPoints();
			currentTotalPoints = Integer.parseInt(currentPoints) + Integer.parseInt(currentBonusPoints);
			
			System.out.println("CURRENT TOTAL POINTS: " + currentPoints);
			

			setCurrentTotalPoints(currentTotalPoints);
			setCurrentLevel(currentUser.getCurrentLevel());
			
			setTitle(quizItem.getWorldId());
			
			requiredItems = new String[4];
			requiredItems[0] = quizItem.getActiveItem1();
			requiredItems[1] = quizItem.getActiveItem2();
			requiredItems[2] = quizItem.getActiveItem3();
			requiredItems[3] = quizItem.getActiveItem4();
			
			for (int a = 0; a < requiredItems.length; a++) {
				System.out.println("REQUIRED ITEMS ARRAY "+ a + " : " + requiredItems[a]);
			}
			
			System.out.println("LOGGING QUIZ ITEM ANSWERED: " + quizItem.getAnswered());
			
			reactions = new String[4];
			reactions[0] = quizItem.getReaction1();
			reactions[1] = quizItem.getReaction2();
			reactions[2] = quizItem.getReaction3();
			reactions[3] = quizItem.getReaction4();
			worldLocationList = extras.getParcelable("worldLocationList");

			

			
			
			QuizFragment quizFragment = new QuizFragment();
			Bundle args = new Bundle();
			// SEND CURRENT USER PARCEL
			args.putParcelable("currentUser", currentUser);
			quizFragment.setArguments(args);
			getSupportFragmentManager().beginTransaction().add(R.id.container, quizFragment).commit();
			
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.container, new BagFragment()).commit();
			

			fragmentManager = getSupportFragmentManager();
			
			
//			setContentView(R.layout.listrow_details);
			
//			LinearLayout quizLayout = (LinearLayout) findViewById(R.layout.fragment_quiz);
//			quizLayout.setVisibility(LinearLayout.VISIBLE);
			
		    // Instantiate a ViewPager and a PagerAdapter.
		    mPager = (ViewPager) findViewById(R.id.bagViewPager);
		    mPagerAdapter = new ScreenSlidePagerAdapter(fragmentManager);
		    mPager.setAdapter(mPagerAdapter);
		    
		  
		    
		}
	}

	


	public void setCurrentTotalPoints(int currentTotalPoints) {
		this.currentTotalPoints = currentTotalPoints;
		
	}
	
	public static int getCurrentLevelCap(String currentLevel) {
		int[] levelRange = StaticSortingUtilities.getLevelRange();
		int nextLevelIndex = Integer.parseInt(currentLevel) + 1;
		final int finalLevelCap = levelRange[nextLevelIndex];
		return finalLevelCap;
	}
	
	public static int getCurrentTotalPoints() {
		return currentTotalPoints;
	}
	
	public static String getCurrentLevel() {
		return currentLevel;
	}
	
	public void setCurrentLevel(String currentLevel) {
		this.currentLevel = currentLevel;
	}



	/**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        	
        	Fragment bagFragment = new BagFragment();
        	Bundle args = new Bundle();
			args.putInt(BagFragment.ARG_SECTION_NUMBER, position);
			bagFragment.setArguments(args);
		    
            return bagFragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quiz, menu);
		return true;
	}

	/**
	 * Gets called every time the user presses the menu button. Use if your menu
	 * is dynamic.
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// menu.clear();
		// menu.add(0, MENU_ONE, Menu.NONE,
		// "BagItem one").setIcon(R.drawable.ic_launcher);
		// menu.add(0, MENU_TWO, Menu.NONE,
		// "BagItem two").setIcon(R.drawable.ic_launcher);
		// menu.add(0, MENU_THREE, Menu.NONE,
		// "BagItem three").setIcon(R.drawable.ic_launcher);
		// menu.add(0, MENU_FOUR, Menu.NONE,
		// "BagItem four").setIcon(R.drawable.ic_launcher);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}

		// switch (item.getItemId()) {
		// case MENU_ONE:
		// System.out.println("MENU_ONE");
		// break;
		// case MENU_TWO:
		// System.out.println("MENU_ONE");
		// break;
		// case MENU_THREE:
		// System.out.println("MENU_THREE");
		// break;
		// case MENU_FOUR:
		// System.out.println("MENU_FOUR");
		// break;
		// }
		//
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class QuizFragment extends Fragment {

		private static final String NO_ITEM_EQUIPPED_MESSAGE_TEXT = "No item equipped";
		public static final String NO_ITEM_EQUIPPED_IMAGE_ICON = "http://iconbug.com/data/86/256/ee219aab434e79e76663997728469fea.png";
		private QuizItem localQuizItem;
		private Button submitQuizButton;
		private TextView quizQuestionText;
		private RadioGroup radioGroup;
		private int selectedRadioButtonIndex;
		private int ANSWERED_COUNT_MAX = 4;
		private int[] answeredCount;
		private int nullValue = -1;
		private TextView levelText;
		private String currentLevel;
		private int currentTotalPoints;

		public QuizFragment() {
			
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			final View rootView = inflater.inflate(R.layout.fragment_quiz,
					container, false);
			
//			System.out.println("ANSWERED COUNT ARRAY BEFORE: " + answeredCount);
			
			answeredCount = new int[ANSWERED_COUNT_MAX];
			resetAnsweredCountArray();
//			
			final int[] checkmarkIds = {
					R.id.quiz_answer_01,
					R.id.quiz_answer_02,
					R.id.quiz_answer_03,
					R.id.quiz_answer_04
			};

			radioGroup = (RadioGroup) rootView
					.findViewById(R.id.quiz_answer_radio_group_01);

			radioGroup
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						public void onCheckedChanged(RadioGroup group,
								int checkedId) {

							
							
							// radio clicked
							int radioButtonID = radioGroup
								.getCheckedRadioButtonId();
						View radioButton = radioGroup
								.findViewById(radioButtonID);
						int index = radioGroup.indexOfChild(radioButton);
						
						selectedRadioButtonIndex = index;
						
						updateAnsweredCount(selectedRadioButtonIndex);
						
//						String requiredItem = quizItem.getActiveItem();
						
//						for (int i = 0; i < requiredItems.length; i++) {
//							System.out.println("REQUIRED ITEMS: " + requiredItems[i]);
//						}
						
						
						if (getCurrentBagItem() == null) {
							Toast.makeText(context, "No items equipped", Toast.LENGTH_SHORT).show();
						} else {
							// check for correct item
							System.out.println("REQUIRED ITEMS: " + getCurrentBagItem().getItemTitle());
							System.out.println("REQUIRED ITEMS: " + requiredItems[index]);
							
							if (!getCurrentBagItem().getItemTitle().equals(requiredItems[index])) {
								Toast.makeText(context, requiredItems[index] + " must be equipped", Toast.LENGTH_SHORT).show();
							}
						}
						
						System.out.println("getSubmittedAnswerIndex: "
								+ index);
						String indexString = Integer.toString(index);
						quizItem.setSubmittedAnswerIndex(indexString);
						}
					});
			
			currentEquippedItemImage = (ImageView) rootView
					.findViewById(R.id.current_equipped_item_01);
			currentEquippedItemText = (TextView) rootView
					.findViewById(R.id.currentEqupppedItemText_01);

			submitQuizButton = (Button) rootView
					.findViewById(R.id.submit_quiz_button);
			quizQuestionText = (TextView) rootView
					.findViewById(R.id.quizQuestionText1);
			
			levelText = (TextView) rootView.findViewById(R.id.levelText1);
			
			
			final User tempUser = getArguments().getParcelable("currentUser");

			String bagTitle = "Current level: " + tempUser.getCurrentLevel();
			final String CURRENT_USER_LEVEL = tempUser.getCurrentLevel();
			final String CURRENT_USER_POINTS = tempUser.getCurrentPoints();
			
			if (tempUser != null) {
				
				if (CURRENT_USER_POINTS != null && CURRENT_USER_LEVEL != null) {
					bagTitle = "Level " + CURRENT_USER_LEVEL + "    " + CURRENT_USER_POINTS + "/" + getCurrentLevelCap(CURRENT_USER_LEVEL) + " XP";
				}
			}
			
			levelText.setText(bagTitle);

			// set quizitem attributes
			System.out.println("QUIZ ITEM ANSWERED: " + quizItem.getAnswered());
			System.out.println("QUIZ ITEM AT ALL: " + quizItem);
			System.out.println("HAVEN'T ANSWERED BEFORE");
			
//			public static void updateBagItems(BagItem item, int index) {
			if (currentBagItem != null) {
				updateBagItems(currentBagItem, 0);
			} else {
//				currentEquippedItemImage.setVisibility(ImageView.GONE);
				imageLoader.displayImage(NO_ITEM_EQUIPPED_IMAGE_ICON, currentEquippedItemImage);
				currentEquippedItemText.setText(NO_ITEM_EQUIPPED_MESSAGE_TEXT);
			}
			
			
			
//			if () {
//				
//			}
			
			
			// set question text
			quizQuestionText.setText(quizItem.getQuestionText());

			// final TextView errorText = (TextView) rootView
			// .findViewById(R.id.quiz_error_text);

			RadioButton radioButton1 = (RadioButton) rootView
					.findViewById(R.id.quiz_answer_01);
			radioButton1.setText(quizItem.getAnswer1());

			RadioButton radioButton2 = (RadioButton) rootView
					.findViewById(R.id.quiz_answer_02);
			radioButton2.setText(quizItem.getAnswer2());

			RadioButton radioButton3 = (RadioButton) rootView
					.findViewById(R.id.quiz_answer_03);
			radioButton3.setText(quizItem.getAnswer3());

			RadioButton radioButton4 = (RadioButton) rootView
					.findViewById(R.id.quiz_answer_04);
			radioButton4.setText(quizItem.getAnswer4());
			
			if (quizItem.getAnswered().equals("true")) {
				System.out.println("QUIZ ITEM GET CORRECT ANSWER INDEX: " + quizItem.getCorrectAnswerIndex());
				System.out.println("QUIZ ITEM GET CORRECT ANSWER INDEX CLASS: " + quizItem.getCorrectAnswerIndex().getClass());
				
				if (quizItem.getCorrectAnswerIndex() != null) {
					
					// disable correct answer radio button
					switch (Integer.parseInt(quizItem.getCorrectAnswerIndex())) {
						case 0: radioButton1.setEnabled(false);
						radioButton1.setTextColor(Color.GRAY);
						break;
						case 1: radioButton2.setEnabled(false);
						radioButton2.setTextColor(Color.GRAY);
						break;
						case 2: radioButton3.setEnabled(false);
						radioButton3.setTextColor(Color.GRAY);
						break;
						case 3: radioButton4.setEnabled(false);
						radioButton4.setTextColor(Color.GRAY);
						break;
					}	
				}
				
//				radioGroup.setVisibility(RadioGroup.GONE);
//				submitQuizButton.setVisibility(Button.GONE);
				quizQuestionText.setText(quizItem.getQuestionText());
//				System.out.println("ALREADY ANSWERED THIS QUESTION");
			}
			


//			System.out.println("HAVEN'T ANSWERED BEFORE");

			// TODO: create datetime function
			// quizItem.setDateTime(dateTime);
			
			
			// TODO: update this function so we can't click through
			submitQuizButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					System.out.println("CLICKED SUBMIT BUTTON");
					
//					System.out.println("LOGGING CURRENT BAG ITEM: " + getCurrentBagItem().getItemTitle());
//					if (getCurrentBagItem() == null) {
//						Toast.makeText(context, "No items equipped", Toast.LENGTH_SHORT).show();
//					} else {
//						// check for correct item
//						System.out.println("REQUIRED ITEMS: " + getCurrentBagItem().getItemTitle());
//						System.out.println("REQUIRED ITEMS: " + requiredItems[index]);
//						
//						if (!getCurrentBagItem().getItemTitle().equals(requiredItems[index])) {
//							Toast.makeText(context, requiredItems[index] + " must be equipped", Toast.LENGTH_SHORT).show();
//						}
//					}
					
					if (quizItem.getSubmittedAnswerIndex() != null) {
						if (getCurrentBagItem() != null && getCurrentBagItem().getItemTitle().equals(requiredItems[selectedRadioButtonIndex])) {
							
							// set current answer checked
							showAnswerSelectedIcon(selectedRadioButtonIndex);
							
							// create string message
							String message = "";
							
							if (getCurrentBagItem().getItemTitle().equals(quizItem.getActiveItem())) {
								
								// set current answer count
								String ANSWER_SUBMIT_COUNT = Integer.toString(getAnsweredCount());
								quizItem.setAnswerSubmitCount(ANSWER_SUBMIT_COUNT);
								
								System.out.println("LOGGING CURRENT BAG ITEM: " + getCurrentBagItem().getItemTitle());
								
								// all quiz submissions are outgoing server requests
								QuizSubmissionAsyncTaskRunner runner = new QuizSubmissionAsyncTaskRunner();
								runner.execute("submit");
								
//								WorldTitleAsyncTaskRunner runner = new WorldTitleAsyncTaskRunner();  
//								runner.execute("http://movie-locations-app.appspot.com/secure/list/WORLD_TITLE");
								
								
								
//								message = reactions[selectedRadioButtonIndex];
//								Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
							} else {
								// this is the default behavior
								//
								// check if correct item is selected to issue reaction toast
								if (getCurrentBagItem().getItemTitle().equals(requiredItems[selectedRadioButtonIndex])) {
									message = reactions[selectedRadioButtonIndex];
								} else {
									message = requiredItems[selectedRadioButtonIndex] + " must be equipped";
								}
								Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
							}
							
						} else {
							String message = requiredItems[selectedRadioButtonIndex] + " must be equipped";
							Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
						}
						
					} else {
						Toast.makeText(context, "No answer selected", Toast.LENGTH_SHORT).show();
					}
					
//					if (quizItem.getSubmittedAnswerIndex() != null) {
//						QuizSubmissionAsyncTaskRunner runner = new QuizSubmissionAsyncTaskRunner();
//						runner.execute("http://movie-locations-app.appspot.com/submit");
//					} else {
//						Toast.makeText(context,
//								"No answer selected, try again.",
//								Toast.LENGTH_SHORT).show();
//					}
				}

				private void showAnswerSelectedIcon(int index) {
//					
					RadioButton currentRadioButton = (RadioButton) rootView.findViewById(checkmarkIds[index]);
//					checkMarkImage.setVisibility(ImageView.VISIBLE);
					
					Drawable checkmarkImage = rootView.getContext().getResources().getDrawable(R.drawable.icon_checkmark_green_small);
					checkmarkImage.setBounds(0, 0, 60, 60);
					currentRadioButton.setCompoundDrawables(null, null, checkmarkImage, null);
				};
			});

			

			
//			
			return rootView;
		}




		private void resetAnsweredCountArray() {
			answeredCount[0] = nullValue;
			answeredCount[1] = nullValue;
			answeredCount[2] = nullValue;
			answeredCount[3] = nullValue;
			
		}

		private void updateAnsweredCount(int selectedRadioButtonIndex) {
			answeredCount[selectedRadioButtonIndex] = selectedRadioButtonIndex;
		}
		
		private int getAnsweredCount() {
			int answeredCountTotal = 0;
			for (int i = 0; i < answeredCount.length; i++) {
				if (answeredCount[i] != nullValue) {
					answeredCountTotal += 1;
				}
			}
			return answeredCountTotal;
		}

		private void setAnsweredCount(int index) {

			
		}
		
		// 

		
//		class WorldTitleAsyncTaskRunner extends AsyncTask<String, String, String> {
//
//			private String resp;
//			private JsonNode json;
//			private ProgressDialog dialog;
//			private boolean initialized = false;
//
//			@Override
//			protected String doInBackground(String... params) {
//				publishProgress("Sleeping..."); // Calls onProgressUpdate()
//				try {
//					// Do your long operations here and return the result
//					String url = params[0];
//					// resp = "async call in progress";
//					// Set the Content-Type header
//					HttpHeaders requestHeaders = new HttpHeaders();
//					requestHeaders.setContentType(new MediaType("application", "json"));
//					HttpEntity<User> requestEntity = new HttpEntity<User>(currentUser, requestHeaders);
////					System.out.println("REST TEMPLATE PRE RESPONSE: " + quizItem.getAnswered());
//
//					// Create a new RestTemplate instance
//					RestTemplate restTemplate = new RestTemplate();
//
//					// Add the Jackson and String message converters
//					restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//					restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
//
//					// Make the HTTP POST request, marshaling the request to
//					// JSON, and the response to a String
//					// ResponseEntity<String> responseEntity =
//					// restTemplate.exchange(url, HttpMethod.POST,
//					// requestEntity, String.class);
//					// String response = responseEntity.getBody();
//
//					ResponseEntity<User> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, User.class);
//					User localUser = responseEntity.getBody();
//					
////					if (quizItem.getCorrectAnswerIndex() != null) {
////						// store correct answer index reference to update interface
////						currentAnswerIndex = quizItem.getCorrectAnswerIndex();
////					}
//
//					// setCurrentQuestion(response);
//
//					
//
//					// send to callback
//					resp = localUser.getDisplayName();
//					System.out.println("REST TEMPLATE POST RESPONSE DISPLAY NAME FROM TITLE API: " + resp);
//
//				} catch (Exception e) {
//					e.printStackTrace();
//					System.out.println("ERROR STACK TRACE");
//					resp = e.getMessage();
//				}
//				return resp;
//			}
//
//			/*
//			 * (non-Javadoc)
//			 * 
//			 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
//			 */
//			@Override
//			protected void onPostExecute(String result) {
//				// execution of result of Long time consuming operation
//				// finalResult.setText(result);
//				// set json data here to serialize
//
//				updateUserInterface(result);
//
//				if (dialog != null) {
//					dialog.dismiss();
//				}
//
//			}
//
//			/*
//			 * (non-Javadoc)
//			 * 
//			 * @see android.os.AsyncTask#onPreExecute()
//			 */
//			@Override
//			protected void onPreExecute() {
//				// Things to be done before execution of long running operation.
//				// For
//				// example showing ProgessDialog
//
//				int randomPhraseIndex = generateRandomNumber(0, 6);
//				dialog = new ProgressDialog(context);
//				dialog.setTitle("Submitting answer...");
//				String message = "<i>" + randomPhrases[randomPhraseIndex] + "</i>";
//				dialog.setMessage(Html.fromHtml(message));
//				dialog.setCancelable(false);
//				dialog.setIndeterminate(true);
//				dialog.show();
//			}
//
//			private int generateRandomNumber(int min, int max) {
//				int randomNumber = min + (int)(Math.random() * ((max - min) + 1));
//				return randomNumber;
//			}
//			
//			// multi-lingual message prompts 
//			public String[] randomPhrases = {
//					"One moment please",
//					"Un instant s'il vous plait",
//					"Un momento por favor",
//					"Einen Moment bitte",
//					"Un momento per favore",
//					"Ett ogonblick",
//					"Een ogenblik geduld aub",
//					"Odota hetki"
//			};
//			
//			public String[] quotes = {
//					"Ard-galen - 'The Green Region'. A grassy area.",
//					"Cirion (Lord of Ships) - The twelfth Ruling Steward of Gondor."
//			};
//
//			/*
//			 * (non-Javadoc)
//			 * 
//			 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
//			 */
//			@Override
//			protected void onProgressUpdate(String... text) {
//				// finalResult.setText(text[0]);
//				// Things to be done while execution of long running operation
//				// is in
//				// progress. For example updating ProgessDialog
//			}
//		}
		
		
		
		
		
		
		
		
		
		
		class QuizSubmissionAsyncTaskRunner extends
				AsyncTask<String, String, String> {

			private String resp;
			private JsonNode json;
			private ProgressDialog dialog;
			private boolean initialized = false;

			@Override
			protected String doInBackground(String... params) {
				publishProgress("Sleeping..."); // Calls onProgressUpdate()
				try {
					// Do your long operations here and return the result
					String url = params[0];
					// resp = "async call in progress";
					

					if (url.equals("submit")) {
						System.out.println("SUBMITTED ANSWER INDEX: " + quizItem.getSubmittedAnswerIndex());
						System.out.println("CORRECT ANSWER INDEX: " + quizItem.getCorrectAnswerIndex());
						if (quizItem.getSubmittedAnswerIndex().equals(quizItem.getCorrectAnswerIndex())) {
							System.out.println("CORRECT ANSWER");
						} else {
							System.out.println("INCORRECT ANSWER");
						}
					}
//					System.out.println("QUIZ ITEM SID: " + quizItemSid);
//					
//					// set identifiable attribute to server to verify
//					quizItem.setAnswered(quizItemSid);
//
//					System.out.println("CORRECT INDEX SUBMITTED: "
//							+ quizItem.getSubmittedAnswerIndex());
//					// Set the Content-Type header
//					HttpHeaders requestHeaders = new HttpHeaders();
//					requestHeaders.setContentType(new MediaType("application",
//							"json"));
//					HttpEntity<QuizItem> requestEntity = new HttpEntity<QuizItem>(
//							quizItem, requestHeaders);
//					System.out.println("REST TEMPLATE PRE RESPONSE: "
//							+ quizItem.getAnswered());
//
//					// Create a new RestTemplate instance
//					RestTemplate restTemplate = new RestTemplate();
//
//					// Add the Jackson and String message converters
//					restTemplate.getMessageConverters().add(
//							new MappingJackson2HttpMessageConverter());
//					restTemplate.getMessageConverters().add(
//							new StringHttpMessageConverter());
//
//					// Make the HTTP POST request, marshaling the request to
//					// JSON, and the response to a String
//					// ResponseEntity<String> responseEntity =
//					// restTemplate.exchange(url, HttpMethod.POST,
//					// requestEntity, String.class);
//					// String response = responseEntity.getBody();
//
//					ResponseEntity<QuizItem> responseEntity = restTemplate
//							.exchange(url, HttpMethod.POST, requestEntity,
//									QuizItem.class);
//					quizItem = responseEntity.getBody();
//					
////					if (quizItem.getCorrectAnswerIndex() != null) {
////						// store correct answer index reference to update interface
////						currentAnswerIndex = quizItem.getCorrectAnswerIndex();
////					}
//
//					// setCurrentQuestion(response);
//
//					System.out.println("REST TEMPLATE POST RESPONSE: "
//							+ quizItem.getAnswered());

					// send to callback
					resp = "true";


				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("ERROR STACK TRACE");
					resp = e.getMessage();
				}
				return resp;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
			 */
			@Override
			protected void onPostExecute(String result) {
				// execution of result of Long time consuming operation
				// finalResult.setText(result);
				// set json data here to serialize

				updateUserInterface(result);

				if (dialog != null) {
					dialog.dismiss();
				}

			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see android.os.AsyncTask#onPreExecute()
			 */
			@Override
			protected void onPreExecute() {
				// Things to be done before execution of long running operation.
				// For
				// example showing ProgessDialog

				int randomPhraseIndex = generateRandomNumber(0, 6);
				dialog = new ProgressDialog(context);
				dialog.setTitle("Submitting answer...");
				String message = "<i>" + randomPhrases[randomPhraseIndex] + "</i>";
				dialog.setMessage(Html.fromHtml(message));
				dialog.setCancelable(false);
				dialog.setIndeterminate(true);
				dialog.show();
			}

			private int generateRandomNumber(int min, int max) {
				int randomNumber = min + (int)(Math.random() * ((max - min) + 1));
				return randomNumber;
			}
			
			// multi-lingual message prompts 
			public String[] randomPhrases = {
					"One moment please",
					"Un instant s'il vous plait",
					"Un momento por favor",
					"Einen Moment bitte",
					"Un momento per favore",
					"Ett ogonblick",
					"Een ogenblik geduld aub",
					"Odota hetki"
			};
			
			public String[] quotes = {
					"Ard-galen - 'The Green Region'. A grassy area.",
					"Cirion (Lord of Ships) - The twelfth Ruling Steward of Gondor."
			};

			/*
			 * (non-Javadoc)
			 * 
			 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
			 */
			@Override
			protected void onProgressUpdate(String... text) {
				// finalResult.setText(text[0]);
				// Things to be done while execution of long running operation
				// is in
				// progress. For example updating ProgessDialog
			}
		}

		// public void setCurrentQuestion(QuizItem currentQuizItem) {
		// this.currentQuizItem = currentQuizItem;
		// }
		//
		// public QuizItem getCurrentQuizItem() {
		// return currentQuizItem;
		// }

		public void updateUserInterface(String result) {
//			String reactionText = "Wrong answer, try again!";

			if (result.equals("true")) {

				// // currentQuizItem.setAnswered("true");
				// // System.out.println("CURRENT QUIZ ITEM: " +
				// currentQuizItem.getAnswered());
				//
				// // open database session
				// quizitemsource.open();
				//
				// // create local quiz item records by id
				// localQuizItem =
				// quizitemsource.selectRecordById(quizItem.getQuestionId());
				//
				// System.out.println("LOCAL QUIZ ITEM BEFORE: " +
				// localQuizItem.getAnswered());
				//
				// // update quiz item
				// localQuizItem.setAnswered("true");
				//
				// // update database record
				// quizitemsource.updateRecord(quizItem.getQuestionId(),
				// "true");
				//
				// System.out.println("LOCAL QUIZ ITEM AFTER: " +
				// localQuizItem.getAnswered());
				//
				// // close database session
				// quizitemsource.close();

				// ArrayAdapterBuilder builder = new ArrayAdapterBuilder();
				// builder.rebuildArrayAdapter();

				//
				// worldLocationList =
				// bundle.getParcelable("worldLocationList");
				//
				//
				//
				// public static WorldLocationArrayAdapter
				// locationQuizItemAdapter;
				// public WorldLocationArrayList worldLocationList;
				// locationQuizItemAdapter = new WorldLocationArrayAdapter(this,
				// intent, worldLocationList.getWorldLocationList());
				// locationQuizItemAdapter = new WorldLocationArrayAdapter(this,
				// intent, worldLocationList.getWorldLocationList());
				// public WorldLocationArrayList worldLocationList;

//				quizItem.setAnswered("true");
				
//				this is already getting set on the server before returning				
//				quizItem.setCorrectAnswerIndex(currentAnswerIndex);

				Intent resultIntent = new Intent();
				// TODO Add extras or a data URI to this intent as appropriate.
				quizItem.setAnswered("true");
				resultIntent.putExtra("quizItem", quizItem);
				getActivity().setResult(Activity.RESULT_OK, resultIntent);
				// finish();

				// close quiz activity
				getActivity().finish();
			}

			
			
			
//			switch (Integer.parseInt(quizItem.getSubmittedAnswerIndex())) {
//
//			case 0:
//				reactionText = quizItem.getReaction1();
//				break;
//
//			case 1:
//				reactionText = quizItem.getReaction2();
//				break;
//
//			case 2:
//				reactionText = quizItem.getReaction3();
//				break;
//
//			case 3:
//				reactionText = quizItem.getReaction4();
//				break;
//			}
			
			String reactionMessage = reactions[selectedRadioButtonIndex];
			final String FULL_CLEAR_BONUS_POINTS = "5";
			
			// append bonus message
			String bonusMessage = "+" + FULL_CLEAR_BONUS_POINTS + " XP (Full clear)";
			
			

			boolean fullClear = true;
			for (int i = 0; i < answeredCount.length; i++) {
				System.out.println("ANSWERED COUNT ARRAY BEFORE: " + answeredCount[i]);
				
				if (answeredCount[i] == nullValue) {
					fullClear = false;
					break;
				}
			}
			
//			// reset the array
//			resetAnsweredCountArray();
			
			
			if (fullClear == true) {
				reactionMessage = reactionMessage.concat(bonusMessage);
				System.out.println("GET ANSWERED COUNT: " + getAnsweredCount());
			}
//			setAnsweredCount(0);

			Toast.makeText(context, reactionMessage, Toast.LENGTH_LONG).show();

			// reloadArrayAdapterData();
		}

		// @Override
		// public void onActivityResult(int requestCode, int resultCode, Intent
		// data) {
		// super.onActivityResult(requestCode, resultCode, data);
		// if(resultCode == RESULT_OK){
		// Intent refresh = new Intent(context,
		// WorldLocationDetailActivity.class);
		// startActivity(refresh);
		// getActivity().finish();
		// }
		// }

		// private void reloadArrayAdapterData() {
		//
		//
		// // ArrayList<FilmLocation> locationList;
		// MovieLocationsImpl datasource = new MovieLocationsImpl(context);
		// ArrayList<FilmLocation> newLocationList = datasource.selectRecords();
		// QuizItemImpl quizsource = new QuizItemImpl(context);
		// ArrayList<QuizItem> newQuizList = quizsource.selectRecords();
		//
		// WorldLocationService worldLocationService = new
		// WorldLocationService();
		//
		// // ArrayList<WorldLocationObject> reloadWorldLocationList;
		//
		//
		//
		// ArrayList<WorldLocationObject> localWorldLocationList;
		// // WorldLocationArrayList tempWorldLocationArrayList = null;
		// try {
		// WorldLocationArrayAdapter locationQuizItemAdapter;
		// localWorldLocationList =
		// worldLocationService.buildWorldLocationObjects(context, newQuizList,
		// newLocationList);
		// WorldLocationArrayList tempWorldLocationArrayList = new
		// WorldLocationArrayList();
		// tempWorldLocationArrayList.setWorldLocationList(localWorldLocationList);
		//
		//
		// // ArrayList<WorldLocationArrayList> worldLocationObjectArrayList =
		// new ArrayList<WorldLocationArrayList>();
		// // public WorldLocationArrayList worldLocationList;
		//
		// // ArrayList<WorldLocationArrayList> values;
		// // WorldLocationArrayList worldLocationList;
		//
		// ArrayList<WorldLocationArrayList> worldLocationObjectArrayList = new
		// ArrayList<WorldLocationArrayList>();
		// // public WorldLocationArrayList worldLocationList;
		// // worldLocationObjectArrayList.add(worldLocationList);
		//
		//
		// worldLocationObjectArrayList.add(tempWorldLocationArrayList);
		// //
		// // public static WorldLocationArrayAdapter locationQuizItemAdapter;
		// locationQuizItemAdapter = new WorldLocationArrayAdapter(context,
		// intent, localWorldLocationList);
		//
		//
		// // Fragment worldLocationFragment = (Fragment)
		// getActivity().getSupportFragmentManager().findFragmentById(R.layout.fragment_film_detail);
		//
		//
		// Fragment worldLocationFragment = (Fragment)
		// getActivity().getSupportFragmentManager().findFragmentById(R.layout.fragment_film_detail);
		//
		// System.out.println("LOGGING worldLocationFragment: " +
		// worldLocationFragment);
		//
		// ListView locationsList = (ListView)
		// worldLocationFragment.getView().findViewById(R.id.locationsView1);
		// // locationQuizItemAdapter.clear();
		//
		// System.out.println("LOGGING locationQuizItemAdapter: " +
		// locationQuizItemAdapter);
		// System.out.println("LOGGING locationsList: " + locationsList);
		//
		//
		// locationsList.setAdapter(locationQuizItemAdapter);
		//
		// // localWorldLocationList =
		// worldLocationService.buildWorldLocationObjects(context, newQuizList,
		// newLocationList);
		// //
		// tempWorldLocationArrayList.setWorldLocationList(localWorldLocationList);
		// } catch (JsonParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// // WorldLocationArrayList worldLocationList =
		// reloadWorldLocationList;
		//
		// // WorldLocationArrayAdapter locationQuizItemAdapter;
		// // localWorldLocationList =
		// worldLocationService.buildWorldLocationObjects(context, newQuizList,
		// newLocationList);
		// // WorldLocationArrayList tempWorldLocationArrayList = new
		// WorldLocationArrayList();
		// //
		// tempWorldLocationArrayList.setWorldLocationList(localWorldLocationList);
		// //
		// //
		// //// ArrayList<WorldLocationArrayList> worldLocationObjectArrayList =
		// new ArrayList<WorldLocationArrayList>();
		// //// public WorldLocationArrayList worldLocationList;
		// //
		// //// ArrayList<WorldLocationArrayList> values;
		// //// WorldLocationArrayList worldLocationList;
		// //
		// // ArrayList<WorldLocationArrayList> worldLocationObjectArrayList =
		// new ArrayList<WorldLocationArrayList>();
		// //// public WorldLocationArrayList worldLocationList;
		// //// worldLocationObjectArrayList.add(worldLocationList);
		// //
		// //
		// // worldLocationObjectArrayList.add(tempWorldLocationArrayList);
		// // //
		// //// public static WorldLocationArrayAdapter locationQuizItemAdapter;
		// // locationQuizItemAdapter = new WorldLocationArrayAdapter(context,
		// intent, worldLocationObjectArrayList);
		// //
		// //
		// // ListView locationsList = (ListView)
		// getView().findViewById(R.id.locationsView1);
		// // locationQuizItemAdapter.clear();
		// // locationsList.setAdapter(locationQuizItemAdapter);
		//
		//
		//
		// // ArrayList<WorldLocationArrayList> worldLocationObjectArrayList =
		// new ArrayList<WorldLocationArrayList>();
		// //// public WorldLocationArrayList worldLocationList;
		// // worldLocationObjectArrayList.add(worldLocationList);
		//
		// //
		// // public static WorldLocationArrayAdapter locationQuizItemAdapter;
		// // locationQuizItemAdapter = new WorldLocationArrayAdapter(this,
		// intent, worldLocationObjectArrayList);
		//
		// //
		// // WorldLocationService worldLocationService = new
		// WorldLocationService();
		// // ArrayList<QuizItem> newquizList = quizsource.selectRecords();
		// //
		//
		// //
		// //// for (WorldLocationObject location : worldLocationList) {
		// //// System.out.println("WORLD LOCATION SERVICE OBJECT WORLD ID: " +
		// location.getWorldId());
		// //// }
		// //
		// // WorldLocationArrayAdapter locationQuizItemAdapter;
		// // ArrayList<WorldLocationArrayList> worldLocationObjectArrayList =
		// new ArrayList<WorldLocationArrayList>();
		// //// public WorldLocationArrayList worldLocationList;
		// // worldLocationObjectArrayList.add(reloadWorldLocationList);
		// //
		// //// public static WorldLocationArrayAdapter locationQuizItemAdapter;
		// // locationQuizItemAdapter = new WorldLocationArrayAdapter(context,
		// intent, worldLocationObjectArrayList);
		// //
		// // ListView locationsList = (ListView)
		// getView().findViewById(R.id.locationsView1);
		// // locationsList.setAdapter(locationQuizItemAdapter);
		//
		// }

		// // these callbacks are issued once the correct
		// // answer has been verified on the application server
		// public void updateUserInterface(String result) {
		// System.out.println("updateUserInterface: " + result);
		//
		// if (result == "true") {
		// Toast.makeText(context, "Correct!", Toast.LENGTH_SHORT).show();
		//
		// // close activity
		// getActivity().finish();
		//
		// // TODO: save this success state to database
		// } else {
		// Toast.makeText(context, "Wrong answer, try again.",
		// Toast.LENGTH_SHORT).show();
		// }
		// }
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class BagFragment extends Fragment {

		public static String ARG_SECTION_NUMBER;
		// private QuizItem localQuizItem;
		//
		// // private QuizItem currentQuizItem;
		// private Button submitQuizButton;
		// private TextView quizQuestionText;
		// // private QuizItemImpl quizitemsource;
		// private RadioGroup radioGroup;
		private SparseArray<BagItem> bagItemSparseArray;


		public BagFragment() {
		}

		public void createData() {
			// String[] activeItems = { "jacket",
			// "coin", // later - textbook
			// "translator", "pen", "key", "flashlight", "guide",
			// "glasses", "textbook", "paperback" };

//			String[] activeItems = { "blue potion", "glasses", "key",
//					"red potion", "coin" };
//			String[] itemPhrases = { "\"Potion of illusion\"",
//					"\"Where did I put my glasses?\"",
//					"\"It's a vintage skeleton key\"", "\"Potion of reality\"",
//					"\"Currency unknown\"" };
//			String[] bagIconArray = {
//					"http://www.zeldadungeon.net/Zelda14/Items/Air-Potion-Icon.png",
//					"https://cdn1.iconfinder.com/data/icons/all_google_icons_symbols_by_carlosjj-du/128/glasses.png",
//					"http://us.cdn2.123rf.com/168nwm/thirteenfifty/thirteenfifty1201/thirteenfifty120100330/12093404-skeleton-key-silhouette.jpg",
//					"http://www.zeldadungeon.net/Zelda14/Items/Heart-Potion-Icon.png",
//					"http://a4.mzstatic.com/us/r30/Purple/e4/33/a9/mzl.isoelwer.175x175-75.png" };

			ArrayList<BagItem> bagList = bagItemArrayList.getBagItemArrayList();
//			System.out.println("BAG LIST SIZE : " + bagList.size());
//
//			// sort array list
//			Collections.sort(bagList, StaticSortingUtilities.BAG_ITEMS_ALPHABETICAL_ORDER);
			
			String bagItemGroupTitle = "Equip an item";

//			for (int a = 0; a < activeItems.length; a++) {
//				BagItem item = new BagItem();
//				item.setBagGroupTitle(bagItemGroupTitle);
//				item.setItemTitle(activeItems[a]);
//				item.setDescription(itemPhrases[a]);
//				item.setImageUrl(bagIconArray[a]);
//				bagList.add(item);
//			}
//			bagItemArrayList
			// for (int j = 0; j < 5; j++) {
			
			// reactions[]
			// requiredItems[]
//			ArrayList<String> itemTitles = new ArrayList<String>();
			
			BagItem bagItem = new BagItem();
			
//			ArrayList<BagItem> tempBagItemList = new ArrayList<BagItem>();
			
//			for (BagItem item : bagList) {
//				tempBagItemList.add(item);
//			}
			
//			for (BagItem newItem : tempBagItemList) {
//				if (!requiredItems[0].equals(newItem.getItemTitle())
//					|| !requiredItems[1].equals(newItem.getItemTitle())
//					|| !requiredItems[2].equals(newItem.getItemTitle())
//					|| !requiredItems[3].equals(newItem.getItemTitle())) {
//					tempBagItemList.remove(newItem);
//				}
//			}
			
			for (BagItem item : bagList) {
				
				System.out.println("BAG LOG requiredItems[0]: " + requiredItems[0]);
				System.out.println("BAG LOG requiredItems[1]: " + requiredItems[1]);
				System.out.println("BAG LOG requiredItems[2]: " + requiredItems[2]);
				System.out.println("BAG LOG requiredItems[3]: " + requiredItems[3]);
				System.out.println("BAG LOG item.getItemTitle(): " + item.getItemTitle());
				
				// TODO: remove null values from game data
				for (int i = 0; i < requiredItems.length; i++) {
					if (requiredItems[i] == null) {
						requiredItems[i] = "null";
					}
				}
				
				String currentTitle = item.getItemTitle();
				
				System.out.println("CURRENT BAG ITEM TITLE: " + currentTitle);
//				System.out.println("REQUIRE");
				
				if (requiredItems[0].equals(currentTitle)
						|| requiredItems[1].equals(currentTitle)
						|| requiredItems[2].equals(currentTitle)
						|| requiredItems[3].equals(currentTitle)) {
//					tempBagItemList.remove(item);
					bagItem.itemList.add(item);
				}
//				bagItem.itemList.add(item);
			}
			
			System.out.println("BAG ITEM LIST LENGTH: " + bagItem.itemList.size());
			
			// more efficient than HashMap for mapping integers to objects
			bagItemSparseArray = new SparseArray<BagItem>();
						
			bagItemSparseArray.append(0, bagItem);
			
//			Collections.sort(itemTitles, StaticSortingUtilities.TITLE_STRINGS_ALPHABETICAL_ORDER);
			
//			BagItem bagItem = new BagItem();
			
			
//			for (int i = 0; i < requiredItems.length; i++) {
//				if (bagList.get(i).getItemTitle().equals(requiredItems[i])) {
//					bagItem.itemList.add(bagList.get(i));
//				}
//			}
			
////			BagItem matchingBagItem = 
//			int counter = 0;
//			for (BagItem item : bagList) {
//				
//				if (item.getItemTitle().equals(itemTitles.get(counter))) {
//					bagItem.itemList.add(item);
//					counter++;
//				}
//				
//
////				if (bagList.contains(bagItem)) {
////					
////				}
//				
//			}
//			for (BagItem item : bagList) {
//				for (int i = 0; i < requiredItems.length; i++) {
//					if (item.getItemTitle().equals(requiredItems[i])) {
//						bagItem.itemList.add(bagList.get(i));
//					}
//				}
//				
////				if () {
////					
////				}
//			}
			
//			for (int b = 0; b < bagList.size(); b++) {
////				if () {
//					bagItem.itemList.add(bagList.get(b));	
////				}
//				
//			}
//			bagItemSparseArray.append(0, bagItem);
			// }
		}
		
//		private int focusedPage = 0;
//		private class MyPageChangeListener extends ViewPager.SimpleOnPageChangeListener {
//		    @Override
//		    public void onPageSelected(int position) {
//		        focusedPage = position;
//		        System.out.println("PAGER FOCUSED");
//		    }
//		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			final View rootView = inflater.inflate(R.layout.fragment_bag,
					container, false);

			// CREATING A NEW VIEW PAGER BAG
			
			
			
			
			// more efficient than HashMap for mapping integers to objects
//			bagItemSparseArray = new SparseArray<BagItem>();

			createData();
			
			// GET CURRENT USER GLUED TO THIS THING
			
			final int position = getArguments().getInt(ARG_SECTION_NUMBER);
			System.out.println("ARGS POSITION: " + position);
			
			List<BagItem> localBagItemList = bagItemSparseArray.get(0).itemList;
			
		    
		    
		    
//		    if (position >= localBagItemList.size()) {
//
//				
//		    	String CONFIRM_MESSAGE = "Okay";
//		    	String DIALOG_TITLE = "You're missing some game data.";
//		    	String DIALOG_MESSAGE = "Check your wi-fi connection and restore current world to continue.";
//				
//				
//				
//		    	// CREATE CONFIRMATION DIALOG
//		    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
//		    	
//		    	builder.setMessage(DIALOG_MESSAGE).setTitle(DIALOG_TITLE);
//		    	
//		    	// TODO: ITERATE AND DELETE CURRENT LEVEL DATA
//		    	// - BAG ITEMS
//		    	// - QUIZ ITEMS
//		    	// - LOCATIONS
//		    	
//		    	
//		    	
//
//		    	
//		    	
//		    	
//				// Add the buttons
//		    	builder.setPositiveButton(CONFIRM_MESSAGE, new DialogInterface.OnClickListener() {
//		           public void onClick(DialogInterface dialog, int id) {
//		        	   getActivity().finish();
//		           }
//		       });
//		    	
//		    	
//		    	final AlertDialog dialog = builder.create();
//		    	dialog.show();
//				
//		    } 
//		    
//		    else {
//			System.out.println("BAG LIST SIZE 1: " + bagItemSparseArray.size());
//			System.out.println("BAG LIST SIZE 2: " + localBagItemList.size());
		    	final BagItem localBagItem = localBagItemList.get(position);
				// SEND STRING PARCELS TO SET ATTRIBUTES

				TextView text = (TextView) rootView.findViewById(R.id.bagTextView1);
				text.setText(localBagItem.getItemTitle());

//				TextView text2 = (TextView) rootView.findViewById(R.id.bagTextView2);
//				text2.setText(localBagItem.getDescription());

				ImageView bagIconView = (ImageView) rootView.findViewById(R.id.bag_icon);
				imageLoader.displayImage(localBagItem.getImageUrl(), bagIconView);
				
//				final int idleColor = R.color.white_overlay;
//				
////				rootView.setBackgroundColor(idleColor);
//				rootView.setFocusable(true);
//				rootView.setSelected(false);
//				rootView.setFocusableInTouchMode(true);
////				rootView.setOnClickListener(l)
//				
				final int selectedColor = R.color.pressed_color;
//				rootView.setOnClickListener(new View.OnClickListener() {
//				
//					@Override
//					public void onClick(View v) {
////						rootView.setBackgroundColor(selectedColor);
//						rootView.setSelected(true);
//						
//						
//					}
//				 });
				 

				  OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {

				        @Override
				        public void onPageScrollStateChanged(int arg0) {
				            // TODO Auto-generated method stub

				        }

				        @Override
				        public void onPageScrolled(int arg0, float arg1, int arg2) {
				            // TODO Auto-generated method stub

				        }

				        @Override
				        public void onPageSelected(int pos) {
//				        	rootView.setSelected(false);
//				        	fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
				        	mPager.setSelected(false);
				        }

				    };
				    mPager.setOnPageChangeListener(mPageChangeListener);
//				    mPager.set
				
//				rootView.setFocusable(true);
//				rootView.setFocusableInTouchMode(true);
				
//				mPager.setOnPageChangeListener(new MyPageChangeListener());
				rootView.setOnClickListener(new View.OnClickListener() {
				
					@Override
					public void onClick(View view) {
//						rootView.setBackgroundColor(selectedColor);
//						rootView.setSelected(false);
//						view.setSelected(true);
						mPager.setSelected(true);
						System.out.println("PAGER CLICKED");
						
						updateBagItems(localBagItem, position);
					}
				});
//		    }
			
//			listView = (ExpandableListView) rootView.findViewById(R.id.bagView);
//			ExpandableListAdapter adapter = new ExpandableListAdapter(
//					getActivity(), bagItemSparseArray);
//			
//			// set points in adapter
//			adapter.setCurrentTotalPoints(currentTotalPoints);
//			adapter.setCurrentLevel(currentUser.getCurrentLevel());
//			listView.setAdapter(adapter);

			// listView.setOnItemClickListener(new
			// AdapterView.OnItemClickListener() {
			//
			//
			// @Override
			// public void onItemClick(AdapterView<?> parent,
			// final View view, int position, long id) {
			//
			// // parent.setSelected(false);
			// // listView.requestFocus();
			//
			// // listView.setChoiceMode(position);
			//
			// // Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
			//
			// // QuizActivity.setCurrentBagItem(children);
			//
			//
			// // // remove current item selected
			// // for (int j = 0; j < listView.getChildCount(); j++) {
			// // listView.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
			// // }
			// //
			// // final BagItemGroup item = (BagItemGroup)
			// parent.getItemAtPosition(position);
			// System.out.println("clicked current item: ");
			// // QuizActivity.setCurrentBagItem(bagItem.getItemTitle(),
			// childPosition);
			// }
			//
			// });

			// // final Button button = (Button) findViewById(R.id.button_id);
			// rootView.setOnClickListener(new View.OnClickListener() {
			// public void onClick(View v) {
			// System.out.println("Clicked on the bag");
			//
			// // toggle bag height
			// if (rootView.getLayoutParams().height == 50) {
			// rootView.getLayoutParams().height = 250;
			// System.out.println("Bag is 250dp tall");
			// } else {
			// rootView.getLayoutParams().height = 50;
			// System.out.println("Bag is 50dp tall");
			// }
			// }
			// });
			//
			//
			// // System.out.println("CURRENT QUIZ ITEM: " +
			// quizItem.getAnswered());
			// radioGroup = (RadioGroup)
			// rootView.findViewById(R.id.quiz_answer_radio_group_01);
			//
			//
			//
			//
			// radioGroup.setOnCheckedChangeListener(new
			// OnCheckedChangeListener() {
			// public void onCheckedChanged(RadioGroup group, int checkedId) {
			//
			// int radioButtonID = radioGroup.getCheckedRadioButtonId();
			// View radioButton = radioGroup.findViewById(radioButtonID);
			// int index = radioGroup.indexOfChild(radioButton);
			//
			// // // checkedId is the RadioButton selected
			// // RadioButton radioButton = (RadioButton)
			// getView().findViewById(radioGroup.getCheckedRadioButtonId());
			// // Integer index = radioGroup.indexOfChild(radioButton);
			// // boolean checked = ((RadioButton) view).isChecked();
			// //
			// // if (checked) {
			// // System.out.println(index);
			// // setCurrentAnswer(index);
			// // quizItem.setSubmittedAnswerIndex(index.toString());
			// // System.out.println("getSubmittedAnswerIndex: " +
			// quizItem.getSubmittedAnswerIndex());
			// System.out.println("getSubmittedAnswerIndex: " + index);
			// String indexString = Integer.toString(index);
			// quizItem.setSubmittedAnswerIndex(indexString);
			// // }
			// }
			// });
			//
			//
			//
			// // CheckBox checkbox1 = (CheckBox)
			// rootView.findViewById(R.id.quiz_answer_01);
			// // CheckBox checkbox2 = (CheckBox)
			// rootView.findViewById(R.id.quiz_answer_02);
			// // CheckBox checkbox3 = (CheckBox)
			// rootView.findViewById(R.id.quiz_answer_03);
			// // CheckBox checkbox4 = (CheckBox)
			// rootView.findViewById(R.id.quiz_answer_04);
			//
			//
			//
			//
			//
			// submitQuizButton = (Button)
			// rootView.findViewById(R.id.submit_quiz_button);
			// quizQuestionText = (TextView)
			// rootView.findViewById(R.id.quizQuestionText1);
			//
			// // set quizitem attributes
			//
			// if (quizItem.getAnswered().equals("true")) {
			// radioGroup.setVisibility(RadioGroup.GONE);
			// submitQuizButton.setVisibility(Button.GONE);
			// quizQuestionText.setText(quizItem.getQuestionText() +
			// ": You've already answered this question.");
			// System.out.println("ALREADY ANSWERED THIS QUESTION");
			// } else {

			// System.out.println("HAVEN'T ANSWERED BEFORE");

			// // TODO: create datetime function
			// // quizItem.setDateTime(dateTime);
			//
			//
			// // quizItem.setSubmittedAnswerIndex(submittedAnswerIndex);
			// // String questionText = quizItem.getQuestionText();
			//
			// // set question text
			// quizQuestionText.setText(quizItem.getQuestionText());
			//
			// // final TextView errorText = (TextView) rootView
			// // .findViewById(R.id.quiz_error_text);
			//
			// RadioButton radioButton1 = (RadioButton)
			// rootView.findViewById(R.id.quiz_answer_01);
			// radioButton1.setText(quizItem.getAnswer1());
			//
			// RadioButton radioButton2 = (RadioButton)
			// rootView.findViewById(R.id.quiz_answer_02);
			// radioButton2.setText(quizItem.getAnswer2());
			//
			// RadioButton radioButton3 = (RadioButton)
			// rootView.findViewById(R.id.quiz_answer_03);
			// radioButton3.setText(quizItem.getAnswer3());
			//
			// RadioButton radioButton4 = (RadioButton)
			// rootView.findViewById(R.id.quiz_answer_04);
			// radioButton4.setText(quizItem.getAnswer4());

			// submitQuizButton.setOnClickListener(new View.OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// if (quizItem.getSubmittedAnswerIndex() != null) {
			// QuizSubmissionAsyncTaskRunner runner = new
			// QuizSubmissionAsyncTaskRunner();
			// runner.execute("http://movie-locations-app.appspot.com/submit");
			// } else {
			// Toast.makeText(context, "No answer selected, try again.",
			// Toast.LENGTH_SHORT).show();
			// }
			// }
			// });
			// }

			return rootView;
		}
	}

	// public static void setCurrentBagItem(String item, int index) {
	// currentBagItem = item;
	// System.out.println("SET CURRENT BAG ITEM: " + item);
	// System.out.println("SET CURRENT BAG ITEM: " + index);
	//
	// for (int j = 0; j < listView.getCount(); j++) {
	// // if (j != index) {
	// listView.getChildAt(j).setSelected(false);
	// // } else {
	// // listView.getChildAt(j).setSelected(true);
	// // }
	// }
	// }

	public static void updateBagItems(BagItem item, int index) {
		setCurrentBagItem(item);

		String currentItemText = item.getItemTitle() + " equipped";
		System.out.println("SET CURRENT BAG ITEM: " + item);
		System.out.println("SET CURRENT BAG ITEM: " + index);

//		for (int j = 0; j < listView.getCount(); j++) {
//			// if (j != index) {
//			listView.getChildAt(j).setSelected(false);
//			// } else {
//			// listView.getChildAt(j).setSelected(true);
//			// }
//		}

		// current selected item on bag
		// currentEquippedItemImage =
		// context.getResources().getDrawable(R.drawable.bagHandleTextView);
		// bagHandleImageView1
		// imageLoader.displayImage(MOVIE_POSTER_URL, bgImage);

		// currentEquippedItemImage
		imageLoader.displayImage(item.getImageUrl(), currentEquippedItemImage);
		currentEquippedItemText.setText(currentItemText);

	}

	private static void setCurrentBagItem(BagItem item) {
		currentBagItem = item;
		
	}
	
	private static BagItem getCurrentBagItem() {
		return currentBagItem;
	}

	public static void showTransparencyOverlay() {
		layout.setBackgroundResource(R.color.transparent_overlay);

	}

	public static void hideTransparencyOverlay() {
		layout.setBackgroundResource(R.color.white_overlay);

	}
	
	
//	public class ScreenSlidePageFragment extends Fragment {
//
//	    @Override
//	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//	            Bundle savedInstanceState) {
//	        ViewGroup rootView = (ViewGroup) inflater.inflate(
//	                R.layout.fragment_bag, container, false);
//
//	        return rootView;
//	    }
//	}
	

	// public static void hideTransparencyOverlay() {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// public static void showTransparencyOverlay() {
	// // TODO Auto-generated method stub
	//
	// }
}

// class BagFragmentAsyncTaskRunner extends AsyncTask<String, String, String> {
//
// private String resp;
// private JsonNode json;
// private ProgressDialog dialog;
// private boolean initialized = false;
//
// @Override
// protected String doInBackground(String... params) {
// publishProgress("Sleeping..."); // Calls onProgressUpdate()
// try {
// // // Do your long operations here and return the result
// // String url = params[0];
// // // resp = "async call in progress";
// //
// // System.out.println("CORRECT INDEX SUBMITTED: " +
// quizItem.getSubmittedAnswerIndex());
// // // Set the Content-Type header
// // HttpHeaders requestHeaders = new HttpHeaders();
// // requestHeaders.setContentType(new MediaType("application", "json"));
// // HttpEntity<QuizItem> requestEntity = new HttpEntity<QuizItem>(quizItem,
// requestHeaders);
// // System.out.println("REST TEMPLATE PRE RESPONSE: " +
// quizItem.getAnswered());
// //
// // // Create a new RestTemplate instance
// // RestTemplate restTemplate = new RestTemplate();
// //
// // // Add the Jackson and String message converters
// // restTemplate.getMessageConverters().add(new
// MappingJackson2HttpMessageConverter());
// // restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
// //
// // // Make the HTTP POST request, marshaling the request to
// // // JSON, and the response to a String
// // // ResponseEntity<String> responseEntity =
// // // restTemplate.exchange(url, HttpMethod.POST,
// // // requestEntity, String.class);
// // // String response = responseEntity.getBody();
// //
// // ResponseEntity<QuizItem> responseEntity = restTemplate.exchange(url,
// HttpMethod.POST, requestEntity, QuizItem.class);
// // QuizItem response = responseEntity.getBody();
// //
// //// setCurrentQuestion(response);
// //
// // System.out.println("REST TEMPLATE POST RESPONSE: " +
// response.getAnswered());
// //
// // // send to callback
// // resp = response.getAnswered();
//
// } catch (Exception e) {
// e.printStackTrace();
// System.out.println("ERROR STACK TRACE");
// resp = e.getMessage();
// }
// return resp;
// }
//
// /*
// * (non-Javadoc)
// *
// * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
// */
// @Override
// protected void onPostExecute(String result) {
// // execution of result of Long time consuming operation
// // finalResult.setText(result);
// // set json data here to serialize
//
// // updateUserInterface(result);
//
// if (dialog != null) {
// dialog.dismiss();
// }
// }
//
// /*
// * (non-Javadoc)
// *
// * @see android.os.AsyncTask#onPreExecute()
// */
// @Override
// protected void onPreExecute() {
// // Things to be done before execution of long running operation.
// // For
// // example showing ProgessDialog
//
// dialog = new ProgressDialog(context);
// dialog.setTitle("Submitting answer...");
// if (initialized) {
// dialog.setMessage("Caching URL to load from memory next time.");
// } else {
// dialog.setMessage("Submitting answer...");
// }
// dialog.setCancelable(false);
// dialog.setIndeterminate(true);
// dialog.show();
// }
//
// /*
// * (non-Javadoc)
// *
// * @see android.os.AsyncTask#onProgressUpdate(Progress[])
// */
// @Override
// protected void onProgressUpdate(String... text) {
// // finalResult.setText(text[0]);
// // Things to be done while execution of long running operation
// // is in
// // progress. For example updating ProgessDialog
// }
// }

// public void updateUserInterface(String result) {
// String reactionText = "Wrong answer, try again!";
//
// if (result.equals("true")) {
//
// quizItem.setAnswered("true");
//
// Intent resultIntent = new Intent();
// // TODO Add extras or a data URI to this intent as appropriate.
// resultIntent.putExtra("quizItem", quizItem);
// getActivity().setResult(Activity.RESULT_OK, resultIntent);
// // finish();
//
// // close quiz activity
// getActivity().finish();
// }
//
// switch (Integer.parseInt(quizItem.getSubmittedAnswerIndex())) {
//
// case 0: reactionText = quizItem.getReaction1();
// break;
//
// case 1: reactionText = quizItem.getReaction2();
// break;
//
// case 2: reactionText = quizItem.getReaction3();
// break;
//
// case 3: reactionText = quizItem.getReaction4();
// break;
// }
//
// Toast.makeText(context, reactionText, Toast.LENGTH_LONG).show();
// }
// }
