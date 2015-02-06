package com.movie.locations.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.movie.locations.R;
import com.movie.locations.database.PointsItemImpl;
import com.movie.locations.domain.BagItem;
import com.movie.locations.domain.BagItemArrayList;
import com.movie.locations.domain.PointsItem;
import com.movie.locations.domain.QuizItem;
import com.movie.locations.domain.User;
import com.movie.locations.utility.StaticSortingUtilities;
import com.nostra13.universalimageloader.core.ImageLoader;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends FragmentActivity {

	private QuizItem quizItem;
	private Intent intent;
	private Context context;
	private BagItem currentBagItem;
	private ImageView currentEquippedItemImage;
	private TextView currentEquippedItemText;
	protected static ImageLoader imageLoader = ImageLoader.getInstance();
	private BagItemArrayList bagItemArrayList;
	private String[] reactions;
	private String[] requiredItems;
	private String currentUserId;
	private User currentUser;
	private String currentTotalPoints;
	private PointsItemImpl pointsItemImpl;

    /**
     * The number of pages to show.
     */
    private final int NUM_PAGES = 4;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next pages
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;
	private FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		
		if (savedInstanceState == null) {	
			context = this;
			intent = getIntent();
			Bundle extras = intent.getExtras();
			quizItem = extras.getParcelable("quizItem");
			bagItemArrayList = extras.getParcelable("bagItemArrayList");
			currentUser = extras.getParcelable("currentUser");
			currentUserId = currentUser.getUserId();
			pointsItemImpl = new PointsItemImpl(context);
			pointsItemImpl.open();
			PointsItem currentPointsItem = pointsItemImpl.selectRecordById(currentUserId);
			pointsItemImpl.close();
			
			if (currentPointsItem != null) {
				String currentPoints = currentPointsItem.getPoints();
				if (currentPoints != null) {
					currentTotalPoints = currentPointsItem.getPoints();
				}
			}else {
				currentTotalPoints = "0";
			}
			
			currentUser.setCurrentPoints(currentTotalPoints);
			setTitle(quizItem.getWorldTitle());
			
			requiredItems = new String[4];
			requiredItems[0] = quizItem.getActiveItem1();
			requiredItems[1] = quizItem.getActiveItem2();
			requiredItems[2] = quizItem.getActiveItem3();
			requiredItems[3] = quizItem.getActiveItem4();
			
			reactions = new String[4];
			reactions[0] = quizItem.getReaction1();
			reactions[1] = quizItem.getReaction2();
			reactions[2] = quizItem.getReaction3();
			reactions[3] = quizItem.getReaction4();

			QuizFragment quizFragment = new QuizFragment();
			Bundle args = new Bundle();
			
			args.putParcelable("currentUser", currentUser);
			args.putParcelable("quizItem", quizItem);
			args.putParcelable("bagItemArrayList", bagItemArrayList);
			args.putStringArray("reactions", reactions);
			args.putStringArray("requiredItems", requiredItems);
			quizFragment.setArguments(args);
			getSupportFragmentManager().beginTransaction().add(R.id.container, quizFragment).commit();
			fragmentManager = getSupportFragmentManager();
			
		    // Instantiate a ViewPager and a PagerAdapter.
		    mPager = (ViewPager) findViewById(R.id.bagViewPager);
		    mPagerAdapter = new ScreenSlidePagerAdapter(fragmentManager);
		    mPager.setAdapter(mPagerAdapter);
		}
	}
	
	public int getCurrentLevelCap(String currentLevel) {
		int[] levelRange = StaticSortingUtilities.getLevelRange();
		int nextLevelIndex = Integer.parseInt(currentLevel) + 1;
		int finalLevelCap = levelRange[nextLevelIndex];
		return finalLevelCap;
	}

	/**
     * A simple pager adapter that represents all ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        	
        	BagFragment bagFragment = new BagFragment();
        	Bundle args = new Bundle();
        	String ARG_SECTION_NUMBER = bagFragment.getArgSectionNumber();
			args.putInt(ARG_SECTION_NUMBER, position);
			args.putParcelable("bagItemArrayList", bagItemArrayList);
			args.putStringArray("requiredItems", requiredItems);
			bagFragment.setArguments(args);
		    
            return bagFragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

	/**
	 * A placeholder fragment containing a simple view.
	 */
	private class QuizFragment extends Fragment {

		private final String NO_ITEM_EQUIPPED_MESSAGE_TEXT = "No item equipped";
		private Button submitQuizButton;
		private TextView quizQuestionText;
		private RadioGroup radioGroup;
		private int selectedRadioButtonIndex;
		private int ANSWERED_COUNT_MAX = 4;
		private int[] answeredCount;
		private int nullValue = -1;
		private TextView levelText;
		private QuizItem quizItem;
		private BagItem currentBagItem;
		private String[] reactions;
		private String[] requiredItems;
		
		public QuizFragment() {
			// empty constructor
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			final View rootView = inflater.inflate(R.layout.fragment_quiz, container, false);
			quizItem = getArguments().getParcelable("quizItem");
			reactions = getArguments().getStringArray("reactions");
			requiredItems = getArguments().getStringArray("requiredItems");
			answeredCount = new int[ANSWERED_COUNT_MAX];
			resetAnsweredCountArray();
			
			final int[] checkmarkIds = {
					R.id.quiz_answer_01,
					R.id.quiz_answer_02,
					R.id.quiz_answer_03,
					R.id.quiz_answer_04
			};

			radioGroup = (RadioGroup) rootView.findViewById(R.id.quiz_answer_radio_group_01);
			radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				public void onCheckedChanged(RadioGroup group, int checkedId) {

					// radio button interaction
					int radioButtonID = radioGroup.getCheckedRadioButtonId();
					View radioButton = radioGroup.findViewById(radioButtonID);
					int index = radioGroup.indexOfChild(radioButton);
					selectedRadioButtonIndex = index;
					updateAnsweredCount(selectedRadioButtonIndex);
					
					if (getCurrentBagItem() == null) {
						Toast.makeText(context, "No items equipped", Toast.LENGTH_SHORT).show();
					} else {
						if (!getCurrentBagItem().getItemTitle().equals(requiredItems[index])) {
							Toast.makeText(context, requiredItems[index] + " must be equipped", Toast.LENGTH_SHORT).show();
						}
					}
					
					quizItem.setSubmittedAnswerIndex(Integer.toString(index));
				}
			});
			
			currentEquippedItemImage = (ImageView) rootView.findViewById(R.id.current_equipped_item_01);
			currentEquippedItemText = (TextView) rootView.findViewById(R.id.currentEqupppedItemText_01);
			submitQuizButton = (Button) rootView.findViewById(R.id.submit_quiz_button);
			quizQuestionText = (TextView) rootView.findViewById(R.id.quizQuestionText1);
			levelText = (TextView) rootView.findViewById(R.id.levelText1);
			User tempUser = getArguments().getParcelable("currentUser");
			String bagTitle = "Current level: " + tempUser.getCurrentLevel();
			String CURRENT_USER_LEVEL = tempUser.getCurrentLevel();
			String CURRENT_USER_POINTS = tempUser.getCurrentPoints();
			int CURRENT_USER_POINTS_INT = Integer.parseInt(CURRENT_USER_POINTS);
			int CURRENT_USER_LEVEL_INT = StaticSortingUtilities.CHECK_LEVEL_RANGE(CURRENT_USER_LEVEL, CURRENT_USER_POINTS_INT);
			String FINAL_CURRENT_USER_LEVEL_STRING = Integer.toString(CURRENT_USER_LEVEL_INT);
			if (tempUser != null) {
				if (CURRENT_USER_POINTS != null && FINAL_CURRENT_USER_LEVEL_STRING != null) {
					bagTitle = "Level " + FINAL_CURRENT_USER_LEVEL_STRING + "    " + CURRENT_USER_POINTS + "/" + getCurrentLevelCap(FINAL_CURRENT_USER_LEVEL_STRING) + " XP";
				}
			}
			
			levelText.setText(bagTitle);
			
			if (currentBagItem != null) {
				updateBagItems(currentBagItem, 0);
			} else {
				currentEquippedItemImage.setBackgroundResource(R.drawable.x_button);
				currentEquippedItemText.setText(NO_ITEM_EQUIPPED_MESSAGE_TEXT);
			}
			
			// set question text
			quizQuestionText.setText(quizItem.getQuestionText());
			
			RadioButton radioButton1 = (RadioButton) rootView.findViewById(R.id.quiz_answer_01);
			radioButton1.setText(quizItem.getAnswer1());

			RadioButton radioButton2 = (RadioButton) rootView.findViewById(R.id.quiz_answer_02);
			radioButton2.setText(quizItem.getAnswer2());

			RadioButton radioButton3 = (RadioButton) rootView.findViewById(R.id.quiz_answer_03);
			radioButton3.setText(quizItem.getAnswer3());

			RadioButton radioButton4 = (RadioButton) rootView.findViewById(R.id.quiz_answer_04);
			radioButton4.setText(quizItem.getAnswer4());
			
			if (quizItem.getAnswered().equals("true") && quizItem.getCorrectAnswerIndex() != null) {	
				// disable correct answer radio button
				switch (Integer.parseInt(quizItem.getCorrectAnswerIndex())) {
					case 1: radioButton1.setEnabled(false);
					radioButton1.setTextColor(Color.GRAY);
					break;
					case 2: radioButton2.setEnabled(false);
					radioButton2.setTextColor(Color.GRAY);
					break;
					case 3: radioButton3.setEnabled(false);
					radioButton3.setTextColor(Color.GRAY);
					break;
					case 4: radioButton4.setEnabled(false);
					radioButton4.setTextColor(Color.GRAY);
					break;
				}
				
				quizQuestionText.setText(quizItem.getQuestionText());
			}
			
			submitQuizButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					
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
								
								// all quiz submissions are outgoing server requests
								QuizSubmissionAsyncTaskRunner runner = new QuizSubmissionAsyncTaskRunner();
								runner.execute("submit");
								
							} else {
								// this is the default behavior
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
				}

				private void showAnswerSelectedIcon(int index) {
					RadioButton currentRadioButton = (RadioButton) rootView.findViewById(checkmarkIds[index]);
					Drawable checkmarkImage = rootView.getContext().getResources().getDrawable(R.drawable.icon_checkmark_green_small);
					checkmarkImage.setBounds(0, 0, 60, 60);
					currentRadioButton.setCompoundDrawables(null, null, checkmarkImage, null);
				};
			});

			return rootView;
		}

		private void resetAnsweredCountArray() {
			for (int i = 0; i < answeredCount.length; i++) {
				answeredCount[i] = nullValue;
			}
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
		
		private class QuizSubmissionAsyncTaskRunner extends AsyncTask<String, String, String> {

			private String resp;
			private ProgressDialog dialog;
		
			@Override
			protected String doInBackground(String... params) {
				publishProgress("Sleeping...");
				try {
					// send to callback
					resp = "true";
				} catch (Exception e) {
					e.printStackTrace();
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
				// For example showing ProgessDialog
		
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
		
			/*
			 * (non-Javadoc)
			 * 
			 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
			 */
			@Override
			protected void onProgressUpdate(String... text) {
				// finalResult.setText(text[0]);
				// Things to be done while execution of long running operation
				// is in progress. For example updating ProgessDialog
			}
		}
		
		public void updateUserInterface(String result) {
		
			if (result.equals("true")) {
				Intent resultIntent = new Intent();
				quizItem.setAnswered("true");
				resultIntent.putExtra("quizItem", quizItem);
				getActivity().setResult(Activity.RESULT_OK, resultIntent);
		
				// close quiz activity
				getActivity().finish();
			}
			
			String reactionMessage = reactions[selectedRadioButtonIndex];
			Toast.makeText(context, reactionMessage, Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * A Bag fragment containing a view pager
	 * and a view pager for inventory items.
	 */
	private class BagFragment extends Fragment {

		private String ARG_SECTION_NUMBER;
		private SparseArray<BagItem> bagItemSparseArray;
		private BagItemArrayList bagItemArrayList;
		private String[] requiredItems;
		
		public BagFragment() { 
			// empty constructor
		}
		
		public String getArgSectionNumber() {
			return ARG_SECTION_NUMBER;
		}

		public void createData() {
			ArrayList<BagItem> bagList = bagItemArrayList.getBagItemArrayList();
			BagItem bagItem = new BagItem();
			
			for (BagItem item : bagList) {
				for (int i = 0; i < requiredItems.length; i++) {
					if (requiredItems[i] == null) {
						requiredItems[i] = "null";
					}
				}
				
				String currentTitle = item.getItemTitle();
				
				if (requiredItems[0].equals(currentTitle)
						|| requiredItems[1].equals(currentTitle)
						|| requiredItems[2].equals(currentTitle)
						|| requiredItems[3].equals(currentTitle)) {
					bagItem.itemList.add(item);
				}
			}
			
			bagItemSparseArray = new SparseArray<BagItem>();
			bagItemSparseArray.append(0, bagItem);
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			View rootView = inflater.inflate(R.layout.fragment_bag, container, false);
			final int position = getArguments().getInt(ARG_SECTION_NUMBER);
			bagItemArrayList = getArguments().getParcelable("bagItemArrayList");
			requiredItems = getArguments().getStringArray("requiredItems");
			
			// CREATE A NEW VIEW PAGER BAG
			createData();		
			List<BagItem> localBagItemList = bagItemSparseArray.get(0).itemList;

			// sort the list
			Collections.sort(localBagItemList, StaticSortingUtilities.BAG_ITEMS_ALPHABETICAL_ORDER);
	    	final BagItem localBagItem = localBagItemList.get(position);
			TextView text = (TextView) rootView.findViewById(R.id.bagTextView1);
			text.setText(localBagItem.getItemTitle());
			ImageView bagIconView = (ImageView) rootView.findViewById(R.id.bag_icon);
			imageLoader.displayImage(localBagItem.getImageUrl(), bagIconView);
			 
			OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {
		        @Override
		        public void onPageScrollStateChanged(int arg0) {

		        }

		        @Override
		        public void onPageScrolled(int arg0, float arg1, int arg2) {

		        }

		        @Override
		        public void onPageSelected(int pos) {
		        	mPager.setSelected(false);
		        }
		    };
		    
		    mPager.setOnPageChangeListener(mPageChangeListener);    
			rootView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					mPager.setSelected(true);
					updateBagItems(localBagItem, position);
				}
			});
			
			return rootView;
		}
	}

	private void updateBagItems(BagItem item, int index) {
		setCurrentBagItem(item);
		String currentItemText = item.getItemTitle() + " equipped";
		imageLoader.displayImage(item.getImageUrl(), currentEquippedItemImage);
		currentEquippedItemText.setText(currentItemText);
	}

	private void setCurrentBagItem(BagItem item) {
		currentBagItem = item;
	}
	
	private BagItem getCurrentBagItem() {
		return currentBagItem;
	}
}