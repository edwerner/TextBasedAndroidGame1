package com.movie.locations.application;
import java.util.ArrayList;
import java.util.List;
import com.movie.locations.R;
import com.movie.locations.dao.UserImpl;
import com.movie.locations.domain.BagItem;
import com.movie.locations.domain.BagItemArrayList;
import com.movie.locations.domain.QuizItem;
import com.movie.locations.domain.User;
import com.movie.locations.util.StaticSortingUtilities;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends FragmentActivity {

	private QuizItem quizItem;
	private Intent intent;
	private static Context context;
	private static BagItem currentBagItem;
	private static ImageView currentEquippedItemImage;
	private static TextView currentEquippedItemText;
//	private ExpandableListView listView;
//	private LinearLayout layout;
	protected static ImageLoader imageLoader = ImageLoader.getInstance();
	private BagItemArrayList bagItemArrayList;
	
	private String[] reactions;
	private String[] requiredItems;
	private String quizItemSid;
	private String currentUserId;
	private UserImpl userSource;
	private User currentUser;
	private String currentPoints;
	private String currentBonusPoints;
	private int currentTotalPoints;

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
    private PagerAdapter mPagerAdapter;
	private FragmentManager fragmentManager;
	private String currentLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		if (savedInstanceState == null) {
			
			context = this;

			// quizItem = new quizItem();
			intent = getIntent();

			// get intent string attributes
			Bundle extras = intent.getExtras();

			quizItem = extras.getParcelable("quizItem");
			System.out.println("PARCEL QUIZ ITEM CORRECT ANSWER INDEX: " + quizItem.getCorrectAnswerIndex());
			System.out.println("PARCEL QUIZ ITEM TEXT: " + quizItem.getQuestionText());
			bagItemArrayList = extras.getParcelable("bagItemArrayList");
			quizItemSid = extras.getString("quizItemSid");
			currentUserId = extras.getString("currentUserId");
			
			// select current user so we can update current xp
			userSource = new UserImpl(context);

//			currentUser = userSource.selectRecordById(localUser.getUserId());
			
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
//			worldLocationList = extras.getParcelable("worldLocationList");

			QuizFragment quizFragment = new QuizFragment();
			Bundle args = new Bundle();
			
			// SEND CURRENT USER PARCEL
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

	public void setCurrentTotalPoints(int currentTotalPoints) {
		this.currentTotalPoints = currentTotalPoints;
	}
	
	public int getCurrentLevelCap(String currentLevel) {
		int[] levelRange = StaticSortingUtilities.getLevelRange();
		int nextLevelIndex = Integer.parseInt(currentLevel) + 1;
		final int finalLevelCap = levelRange[nextLevelIndex];
		return finalLevelCap;
	}
	
	public int getCurrentTotalPoints() {
		return currentTotalPoints;
	}
	
	public String getCurrentLevel() {
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
	public class QuizFragment extends Fragment {

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
		private QuizItem quizItem;
		private BagItem currentBagItem;
		private String[] reactions;
		private String[] requiredItems;
		
//		private Context context;
		
		public QuizFragment() {
			
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			final View rootView = inflater.inflate(R.layout.fragment_quiz,
					container, false);
			
//			context = getActivity().getApplicationContext();

			quizItem = getArguments().getParcelable("quizItem");
			reactions = getArguments().getStringArray("reactions");
			requiredItems = getArguments().getStringArray("requiredItems");
			
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

			radioGroup = (RadioGroup) rootView.findViewById(R.id.quiz_answer_radio_group_01);

			radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				public void onCheckedChanged(RadioGroup group, int checkedId) {

				// radio clicked
				int radioButtonID = radioGroup.getCheckedRadioButtonId();
				View radioButton = radioGroup.findViewById(radioButtonID);
				int index = radioGroup.indexOfChild(radioButton);
				selectedRadioButtonIndex = index;
				updateAnsweredCount(selectedRadioButtonIndex);
				
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
			
			currentEquippedItemImage = (ImageView) rootView.findViewById(R.id.current_equipped_item_01);
			currentEquippedItemText = (TextView) rootView.findViewById(R.id.currentEqupppedItemText_01);
			submitQuizButton = (Button) rootView.findViewById(R.id.submit_quiz_button);
			quizQuestionText = (TextView) rootView.findViewById(R.id.quizQuestionText1);
			levelText = (TextView) rootView.findViewById(R.id.levelText1);
			final User tempUser = getArguments().getParcelable("currentUser");
			String bagTitle = "Current level: " + tempUser.getCurrentLevel();
			final String CURRENT_USER_LEVEL = tempUser.getCurrentLevel();
			final String CURRENT_USER_POINTS = tempUser.getCurrentPoints();
			final int CURRENT_USER_POINTS_INT = Integer.parseInt(CURRENT_USER_POINTS);
			final int CURRENT_USER_LEVEL_INT = StaticSortingUtilities.CHECK_LEVEL_RANGE(CURRENT_USER_LEVEL, CURRENT_USER_POINTS_INT);
			final String FINAL_CURRENT_USER_LEVEL_STRING = Integer.toString(CURRENT_USER_LEVEL_INT);
			if (tempUser != null) {
				if (CURRENT_USER_POINTS != null && FINAL_CURRENT_USER_LEVEL_STRING != null) {
					bagTitle = "Level " + FINAL_CURRENT_USER_LEVEL_STRING + "    " + CURRENT_USER_POINTS + "/" + getCurrentLevelCap(FINAL_CURRENT_USER_LEVEL_STRING) + " XP";
				}
			}
			
			levelText.setText(bagTitle);

			// set quizitem attributes
			System.out.println("QUIZ ITEM ANSWERED: " + quizItem.getAnswered());
			System.out.println("QUIZ ITEM AT ALL: " + quizItem);
			System.out.println("HAVEN'T ANSWERED BEFORE");
			
			if (currentBagItem != null) {
				updateBagItems(currentBagItem, 0);
			} else {
				imageLoader.displayImage(NO_ITEM_EQUIPPED_IMAGE_ICON, currentEquippedItemImage);
				currentEquippedItemText.setText(NO_ITEM_EQUIPPED_MESSAGE_TEXT);
			}
			
			// set question text
			quizQuestionText.setText(quizItem.getQuestionText());

			// final TextView errorText = (TextView) rootView
			// .findViewById(R.id.quiz_error_text);

			RadioButton radioButton1 = (RadioButton) rootView.findViewById(R.id.quiz_answer_01);
			radioButton1.setText(quizItem.getAnswer1());

			RadioButton radioButton2 = (RadioButton) rootView.findViewById(R.id.quiz_answer_02);
			radioButton2.setText(quizItem.getAnswer2());

			RadioButton radioButton3 = (RadioButton) rootView.findViewById(R.id.quiz_answer_03);
			radioButton3.setText(quizItem.getAnswer3());

			RadioButton radioButton4 = (RadioButton) rootView.findViewById(R.id.quiz_answer_04);
			radioButton4.setText(quizItem.getAnswer4());
			
			if (quizItem.getAnswered().equals("true")) {
				System.out.println("QUIZ ITEM GET CORRECT ANSWER INDEX: " + quizItem.getCorrectAnswerIndex());
				System.out.println("QUIZ ITEM GET CORRECT ANSWER INDEX CLASS: " + quizItem.getCorrectAnswerIndex().getClass());
				
				if (quizItem.getCorrectAnswerIndex() != null) {
					
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
				}
				
//				radioGroup.setVisibility(RadioGroup.GONE);
//				submitQuizButton.setVisibility(Button.GONE);
				quizQuestionText.setText(quizItem.getQuestionText());
//				System.out.println("ALREADY ANSWERED THIS QUESTION");
			}
			
			submitQuizButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					System.out.println("CLICKED SUBMIT BUTTON");
					
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
		
		private class QuizSubmissionAsyncTaskRunner extends AsyncTask<String, String, String> {

		private String resp;
	//	private JsonNode json;
		private ProgressDialog dialog;
	//	private boolean initialized = false;
	
		@Override
		protected String doInBackground(String... params) {
			publishProgress("Sleeping..."); // Calls onProgressUpdate()
			try {
				// Do your long operations here and return the result
	//			String url = params[0];
				
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
		
		if (fullClear == true) {
			reactionMessage = reactionMessage.concat(bonusMessage);
			System.out.println("GET ANSWERED COUNT: " + getAnsweredCount());
		}
	//	setAnsweredCount(0);
	
		Toast.makeText(context, reactionMessage, Toast.LENGTH_LONG).show();
	
		// reloadArrayAdapterData();
	}
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class BagFragment extends Fragment {

		public static String ARG_SECTION_NUMBER;
		private SparseArray<BagItem> bagItemSparseArray;
		private BagItemArrayList bagItemArrayList;
		private String[] requiredItems;

		public BagFragment() {
		}

		public void createData() {
			ArrayList<BagItem> bagList = bagItemArrayList.getBagItemArrayList();
			BagItem bagItem = new BagItem();
			
			for (BagItem item : bagList) {
				
				System.out.println("BAG LOG requiredItems[0]: " + requiredItems[0]);
				System.out.println("BAG LOG requiredItems[1]: " + requiredItems[1]);
				System.out.println("BAG LOG requiredItems[2]: " + requiredItems[2]);
				System.out.println("BAG LOG requiredItems[3]: " + requiredItems[3]);
				System.out.println("BAG LOG item.getItemTitle(): " + item.getItemTitle());
				
				for (int i = 0; i < requiredItems.length; i++) {
					if (requiredItems[i] == null) {
						requiredItems[i] = "null";
					}
				}
				
				String currentTitle = item.getItemTitle();
				
				System.out.println("CURRENT BAG ITEM TITLE: " + currentTitle);
				
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
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			final View rootView = inflater.inflate(R.layout.fragment_bag, container, false);
			
			final int position = getArguments().getInt(ARG_SECTION_NUMBER);
			bagItemArrayList = getArguments().getParcelable("bagItemArrayList");
			requiredItems = getArguments().getStringArray("requiredItems");
			
			System.out.println("BAG ITEM ARRAY LIST: " + bagItemArrayList);

			// CREATING A NEW VIEW PAGER BAG
			createData();
			
			System.out.println("ARGS POSITION: " + position);
			
			List<BagItem> localBagItemList = bagItemSparseArray.get(0).itemList;
	    	final BagItem localBagItem = localBagItemList.get(position);
			// SEND STRING PARCELS TO SET ATTRIBUTES

			TextView text = (TextView) rootView.findViewById(R.id.bagTextView1);
			text.setText(localBagItem.getItemTitle());

//				TextView text2 = (TextView) rootView.findViewById(R.id.bagTextView2);
//				text2.setText(localBagItem.getDescription());

			ImageView bagIconView = (ImageView) rootView.findViewById(R.id.bag_icon);
			imageLoader.displayImage(localBagItem.getImageUrl(), bagIconView);

			final int selectedColor = R.color.pressed_color;
			 
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
			return rootView;
		}
	}

	public static void updateBagItems(BagItem item, int index) {
		setCurrentBagItem(item);

		String currentItemText = item.getItemTitle() + " equipped";
		System.out.println("SET CURRENT BAG ITEM: " + item);
		System.out.println("SET CURRENT BAG ITEM: " + index);

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
}