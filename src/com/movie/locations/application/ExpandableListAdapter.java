package com.movie.locations.application;

import com.movie.locations.R;
import com.movie.locations.domain.BagItem;
import com.movie.locations.util.StaticSortingUtilities;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Toast;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private final SparseArray<BagItem> sparseItemArrayList;
	public LayoutInflater inflater;
	public Activity activity;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private int currentTotalPoints;
	private int currentLevelCap;
	private String currentLevel;

	public ExpandableListAdapter(Activity act,
			SparseArray<BagItem> sparseItemArrayList) {
		activity = act;
		this.sparseItemArrayList = sparseItemArrayList;
		inflater = act.getLayoutInflater();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return sparseItemArrayList.get(groupPosition).itemList
				.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			final boolean isLastChild, View convertView, ViewGroup parent) {
		final BagItem bagItem = (BagItem) getChild(groupPosition, childPosition);
		final String message = bagItem.getItemTitle() + " equipped";

		TextView text = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listrow_details, null);
		}
		text = (TextView) convertView.findViewById(R.id.bagTextView1);
		text.setText(bagItem.getItemTitle());

		TextView text2 = (TextView) convertView.findViewById(R.id.bagTextView2);
		text2.setText(bagItem.getDescription());

		ImageView bagIconView = (ImageView) convertView
				.findViewById(R.id.bag_icon);

		// String[] bagIconArray = {
		// "http://www.zeldadungeon.net/Zelda14/Items/Air-Potion-Icon.png",
		// "https://cdn1.iconfinder.com/data/icons/all_google_icons_symbols_by_carlosjj-du/128/glasses.png",
		// "http://us.cdn2.123rf.com/168nwm/thirteenfifty/thirteenfifty1201/thirteenfifty120100330/12093404-skeleton-key-silhouette.jpg",
		// "http://www.zeldadungeon.net/Zelda14/Items/Heart-Potion-Icon.png",
		// "http://a4.mzstatic.com/us/r30/Purple/e4/33/a9/mzl.isoelwer.175x175-75.png"
		// };

		// String UNIQUE_MAP_IMAGE_URL;
		imageLoader.displayImage(bagItem.getImageUrl(), bagIconView);

		// int count = getGroupCount();
		// // String counter = Integer.toString(count);
		// //
		// for (int j = 0; j < count; j++) {
		// Object child = getChild(0, j);
		// // System.out.println("asdf");
		// // convertView.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);
		// // convertView.getC
		// }

		// convertView.setFocusableInTouchMode(true);

		final View selectorView = convertView;
		convertView.setOnClickListener(new OnClickListener() {

			//

			@Override
			public void onClick(View view) {
				
				
				

//				currentBagItem = item;
//				System.out.println("SET CURRENT BAG ITEM: " + item);
//				System.out.println("SET CURRENT BAG ITEM: " + index);
//				for (int j = 0; j < getGroupCount(); j++) {
//					if (j != childPosition) {
//						((ViewGroup) selectorView).getChildAt(j).setSelected(false);
//					} else {
//						((ViewGroup) selectorView).getChildAt(j).setSelected(true);
//					}
//				}
				
				
				
				
//				view.setSelected(!view.isSelected());
				
//				for (int j = 0; j < getGroupCount(); j++) {
//					if (j != childPosition) {
//						view.setSelected(!view.isSelected());
//					} else {
//						((ViewGroup) selectorView).getChildAt(j).setBackgroundColor(Color.WHITE);
//					}
//					
//				}
					
//				// remove current item selected
//				for (int j = 0; j < ((ViewGroup) selectorView).getChildCount(); j++) {
//					((ViewGroup) selectorView).getChildAt(j).setBackgroundColor(Color.WHITE);
//				}

				// selectorView.setBackgroundColor(Color.WHITE);
				// v.setSelected(false);
////				v.refreshDrawableState();
//				v.setSelected(!v.isSelected());
//				// convertView.setSelected(false);
//
//				// convertView.setChoiceMode(childPosition);
//
//				// v.setSelected(true);
//
				Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();

				// v.setSelected(true);
				//
				QuizActivity.updateBagItems(bagItem, childPosition);
				
				view.setSelected(true);
			}
			// convertView.refreshDrawableState();
		});

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return sparseItemArrayList.get(groupPosition).itemList.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return sparseItemArrayList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return sparseItemArrayList.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
//		activity.getApplicationContext().setTheme(R.style.AppBaseTheme);
//		QuizActivity.hideTransparencyOverlay();
		hideTransparencyOverlay();
		System.out.println("GROUP COLLAPSED");
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
//		activity.getApplicationContext().setTheme(R.style.Transparent);
//		QuizActivity.showTransparencyOverlay();
		showTransparencyOverlay();
		System.out.println("GROUP OPEN");
	}

	public void hideTransparencyOverlay() {
//		LinearLayout overlay = (LinearLayout) activity.findViewById(R.id.transparent_quiz_overlay);
//		overlay.setVisibility(RelativeLayout.GONE);
		
	}

	public void showTransparencyOverlay() {
//		LinearLayout overlay = (LinearLayout) activity.findViewById(R.id.transparent_quiz_overlay);
//		overlay.setVisibility(RelativeLayout.VISIBLE);
	
	}
	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	} 

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.listrow_group, null);
		}
		BagItem group = (BagItem) getGroup(groupPosition);
		String bagTitle = "Equip an item";
		if (getCurrentTotalPoints() != -1 && getCurrentLevel() != null) {
			bagTitle = "Level " + getCurrentLevel() + "    " + getCurrentTotalPoints() + "/" + getCurrentLevelCap() + " XP";
		}
		((CheckedTextView) convertView).setText(bagTitle);
		
		// SET BAG ICON
		Drawable bagIconImage = convertView.getContext().getResources().getDrawable(R.drawable.bag_icon);
		bagIconImage.setBounds(0, 0, 150, 150);
		((CheckedTextView) convertView).setCompoundDrawables(null, null, bagIconImage, null);
		
//		((CheckedTextView) convertView).setB(R.drawable.bag_icon);
		((CheckedTextView) convertView).setChecked(isExpanded);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	public void setCurrentTotalPoints(int currentTotalPoints) {
		this.currentTotalPoints = currentTotalPoints;
		
	}
	
	public int getCurrentLevelCap() {
		int[] levelRange = StaticSortingUtilities.getLevelRange();
		int nextLevelIndex = Integer.parseInt(getCurrentLevel()) + 1;
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
}