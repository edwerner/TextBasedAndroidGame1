/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.movie.locations.gcm;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.movie.locations.AchievementActivity;
import com.movie.locations.R;
import com.movie.locations.application.ConclusionActivity;
import com.movie.locations.application.MainActivity;
import com.movie.locations.domain.BagItem;
import com.movie.locations.domain.BagItemArrayList;
import com.movie.locations.domain.ConclusionCard;
import com.movie.locations.domain.FilmLocation;
import com.movie.locations.domain.GameTitle;
import com.movie.locations.domain.NewsItem;
import com.movie.locations.domain.PointsItem;
import com.movie.locations.domain.QuizItem;
import com.movie.locations.service.BagItemService;
import com.movie.locations.service.ConclusionCardService;
import com.movie.locations.service.FilmLocationService;
import com.movie.locations.service.GameTitleService;
import com.movie.locations.service.NewsItemService;
import com.movie.locations.service.PointsItemService;
import com.movie.locations.service.QuizItemService;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {
	public int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;
	private static Context context;
	private Bundle extras;
	private String ACHIEVEMENT_IMAGE_URL = "http://opengameart.org/sites/default/files/matriz_1_0.png";
	private BagItemArrayList bagItemArrayList;
	private static String currentUserId;

	public GcmIntentService() {
		super("GcmIntentService");
	}

	public static final String TAG = "GCM Demo";

	@Override
	protected void onHandleIntent(Intent intent) {
		extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) { // has effect of unparcelling Bundle
			/*
			 * Filter messages based on message type. Since it is likely that
			 * GCM will be extended in the future with new message types, just
			 * ignore any message types you're not interested in, or that you
			 * don't recognize.
			 */
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
				 sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
				sendNotification("Deleted messages on server: " + extras.toString());
				// If it's a regular GCM message, do some work.
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
//				System.out.println("MESSAGE_TYPE_MESSAGE: " + GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE);
//				System.out.println("MESSAGE_TYPE_: " + messageType);
//				// This loop represents the service doing some work.
//				for (int i = 0; i < 5; i++) {
//					Log.i(TAG,
//							"Working... " + (i + 1) + "/5 @ "
//									+ SystemClock.elapsedRealtime());
//					try {
//						Thread.sleep(5000);
//					} catch (InterruptedException e) {
//					}
//				}
//				Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
				
				// check if achievement message type
				// Post notification of received message.
				sendNotification("Received: " + extras.toString());
				Log.i(TAG, "Received: " + extras.toString());
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}

	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	private void sendNotification(String msg) {
		// log out the message
		
		String message = msg;
		String title = extras.getString("title");
		String copy = extras.getString("copy");
//		String worldCount = extras.getString("worldCount");
		
		String levelData = extras.getString("levelData");
		String quizData = extras.getString("quizData");
		String bagItemData = extras.getString("bagItemData");
		String welcomeMessageData = extras.getString("welcomeMessageData");
		String pointsData = extras.getString("pointsData");
//		String bonusPointsData = extras.getString("bonusPointsData");
		String newLevelMessageData = extras.getString("newLevelMessageData");
		String conclusionData = extras.getString("conclusionData");
		String conclusionCardData = extras.getString("conclusionCardData");
		String levelUp = extras.getString("levelUp");
		String gameTitleData = extras.getString("titleData");
		String notificationSettingsData = extras.getString("notificationSettingsData");
		String newsItemData = extras.getString("newsItemData");
		BagItem[] bagItems;
		ConclusionCard conclusionCard;
		
//		System.out.println("GCM INTENT SERVICE MESSAGE: " + message);
		
		if (message != null) {
			try {
				
				// build new data services
				FilmLocationService service = new FilmLocationService();
				QuizItemService quizService = new QuizItemService();
				BagItemService bagItemService = new BagItemService();
				PointsItemService pointsItemService = new PointsItemService();
				ConclusionCardService cardService = new ConclusionCardService();
				GameTitleService titleService = new GameTitleService(); 
				NewsItemService newsItemService = new NewsItemService();
				
//				// detect message type
//				if (title != null) {
//					
//				}
				

				if (newsItemData != null) {
					System.out.println("GCM INTENT SERVICE NEWS ITEM DATA: " + newsItemData);
					
					// CREATE NEWS ITEM SERVICE AND PERST DATA
					
					
//					mobileNotifications

					JsonNode notificationsJson = service.createJsonNode(newsItemData);
//					newsItemService
					ArrayList<NewsItem> titleList = newsItemService.buildNewsItemObject(notificationsJson, this);
					
					for (NewsItem localTitle : titleList) {
						System.out.println("GCM INTENT SERVICE GAME TITLE SERVICE: " + localTitle.getTitle());
					}
					
					// BUILD NOTIFICATION FOR REGISTERED USERS
					final String FINAL_MOBILE_NOTIFICATIONS_STRING = removeDoubleQuotes(notificationsJson.findPath("mobileNotifications").toString());
					
					System.out.println("FINAL_MOBILE_NOTIFICATIONS_STRING: " + FINAL_MOBILE_NOTIFICATIONS_STRING);
					
					
					
					if (FINAL_MOBILE_NOTIFICATIONS_STRING.equals("true")) {

						NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
								this).setSmallIcon(R.drawable.ic_launcher)
					            .setAutoCancel(true)
					            .setDefaults(Notification.DEFAULT_VIBRATE)
								.setContentTitle("News Update")
								.setContentText(title)
								.setStyle(new NotificationCompat.BigTextStyle().bigText(msg));
						System.out.println("TITLE: " + title);
						System.out.println("COPY: " + copy);

						// create and start achievement activity

						mNotificationManager = (NotificationManager) this
								.getSystemService(Context.NOTIFICATION_SERVICE);
						
						Intent achievementIntent = new Intent(this, AchievementActivity.class);
						AchievementActivity.setContext(this.getBaseContext());
						
						achievementIntent.putExtra("messageId", NOTIFICATION_ID);
						achievementIntent.putExtra("achievementTitle", title);
						achievementIntent.putExtra("achievementCopy", copy);
						
						
						if (levelUp != null) {
							System.out.println("GCM INTENT SERVICE CONCLUSION LEVEL UP DATA: " + levelUp);
//							levelUpImageUrl
							String levelUpImageUrl = extras.getString("levelUpImageUrl");
							ACHIEVEMENT_IMAGE_URL = levelUpImageUrl;
							achievementIntent.putExtra("levelUp", levelUp);
							
//							achievementIntent.putExtra("levelUpImageUrl", levelUpImageUrl);
						}
						
						achievementIntent.putExtra("achievementImageUrl", ACHIEVEMENT_IMAGE_URL);

						PendingIntent contentIntent = PendingIntent.getActivity(this, 0, achievementIntent, PendingIntent.FLAG_ONE_SHOT);

						mBuilder.setContentIntent(contentIntent);
						mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());	
					}
					
//					JsonNode gameTitleJson = service.createJsonNode(gameTitleData);
//					ArrayList<GameTitle> titleList = titleService.buildGameTitleObjects(gameTitleJson, this);
//					
//					for (GameTitle localTitle : titleList) {
//						System.out.println("GCM INTENT SERVICE GAME TITLE SERVICE: " + localTitle.getTitle());
//					}
				}
				
				if (gameTitleData != null) {
					System.out.println("GCM INTENT SERVICE GAME TITLE DATA: " + gameTitleData);
					JsonNode gameTitleJson = service.createJsonNode(gameTitleData);
					ArrayList<GameTitle> titleList = titleService.buildGameTitleObjects(gameTitleJson, this);
					
					for (GameTitle localTitle : titleList) {
						System.out.println("GCM INTENT SERVICE GAME TITLE SERVICE: " + localTitle.getTitle());
					}
				}
				
//				if (notificationSettingsData != null) {
//					System.out.println("GCM INTENT SERVICE NOTIFICATION SETTINGS DATA: " + notificationSettingsData);
//				}
				
				if (conclusionData != null) {
					System.out.println("GCM INTENT SERVICE CONCLUSION MESSAGE DATA: " + conclusionData);
				}
				
				if (conclusionCardData != null) {
					System.out.println("GCM INTENT SERVICE CONCLUSION CARD DATA: " + conclusionCardData);
//					
////					cardService
					JsonNode cardJson = service.createJsonNode(conclusionCardData);
					ArrayList<ConclusionCard> cardList = cardService.buildConclusionCardObject(cardJson, this);
					
					for (ConclusionCard card : cardList) {
						System.out.println("GCM INTENT SERVICE CONCLUSION CARD SERVICE: " + card.getTitle());
					}
				}

				if (newLevelMessageData != null) {
					System.out.println("GCM INTENT SERVICE NEW LEVEL MESSAGE DATA: " + newLevelMessageData);
				}
				
				if (welcomeMessageData != null) {
					System.out.println("GCM INTENT SERVICE WELCOME MESSAGE DATA: " + welcomeMessageData);
				}
				
				if (levelData != null) {
//					System.out.println("GCM INTENT SERVICE LEVEL DATA OBJECT: ");
					JsonNode locationJson = service.createJsonNode(levelData);
//
//					
					ArrayList<FilmLocation> locationList = service.buildWorldLocationObject(locationJson, this);
					
					
//					System.out.println("GCM INTENT SERVICE ACHIEVEMENT: ");
					
					for (FilmLocation loc : locationList) {
						System.out.println("GCM INTENT SERVICE WORLD LOCATION: " + loc.getLocations());
					}
					
				} 
				
				if (quizData != null) {
//					System.out.println("GCM INTENT SERVICE QUIZ DATA OBJECT: ");
					
					JsonNode quizJson = service.createJsonNode(quizData);
					ArrayList<QuizItem> quizList = quizService.buildWorldLocationQuizObject(quizJson, this);
					
					for (QuizItem quiz : quizList) {
						System.out.println("GCM INTENT SERVICE QUIZ DATA: " + quiz.getQuestionText());
					}
				} 
				
				if (notificationSettingsData != null) {
					System.out.println("GCM INTENT SERVICE CONCLUSION MESSAGE DATA: " + notificationSettingsData);
					
					Intent achievementIntent = new Intent(getBaseContext(), ConclusionActivity.class);
					achievementIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					Intent.FLAG_ACTIVITY_CLEAR_TOP | PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT
//					ConclusionActivity.setContext(this.getBaseContext());
					
					// TODO: SET USER LEVEL
					// TODO: SET WORLD COUNT
					
					// TODO: ADD CONCLUSION CARD PARCEL
					JsonNode notificationsJson = service.createJsonNode(notificationSettingsData);
					final String FINAL_EMAIL_NOTIFICATIONS_STRING = removeDoubleQuotes(notificationsJson.findPath("emailNotifications").toString());
					final String FINAL_MOBILE_NOTIFICATIONS_STRING = removeDoubleQuotes(notificationsJson.findPath("mobileNotifications").toString());
					
					achievementIntent.putExtra("conclusionTitle", "Success!");
					achievementIntent.putExtra("conclusionCopy", "User notification settings have been updated");
					achievementIntent.putExtra("conclusionImageUrl", "conclusionImageUrl");
					achievementIntent.putExtra("emailNotifications", FINAL_EMAIL_NOTIFICATIONS_STRING);
					achievementIntent.putExtra("mobileNotifications", FINAL_MOBILE_NOTIFICATIONS_STRING);
					achievementIntent.putExtra("currentUserId", getCurrentUserId());
					
					getApplication().startActivity(achievementIntent);
					
				}
				
				if (bagItemData != null) {
					System.out.println("BAG ITEM DATA NOT NULL");
					JsonNode bagJson = service.createJsonNode(bagItemData);
					ArrayList<BagItem> bagItemList = bagItemService.buildBagItemObject(bagJson, this);
					for (BagItem item : bagItemList) {
						System.out.println("GCM INTENT SERVICE BAG ITEM DATA: " + item.getItemTitle());
					}
					
					// CREATE BAG ITEM LIST PARCELABLE
					bagItemArrayList = new BagItemArrayList();
					bagItemArrayList.setBagItemArrayList(bagItemList);
					
					Intent achievementIntent = new Intent(getBaseContext(), ConclusionActivity.class);
					achievementIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//					Intent.FLAG_ACTIVITY_CLEAR_TOP | PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT
//					ConclusionActivity.setContext(this.getBaseContext());
					
					// TODO: SET USER LEVEL
					// TODO: SET WORLD COUNT
					
					// TODO: ADD CONCLUSION CARD PARCEL
					
					achievementIntent.putExtra("conclusionTitle", "Conclusion Title");
					achievementIntent.putExtra("conclusionCopy", "Conclusion copy");
					achievementIntent.putExtra("conclusionImageUrl", "conclusionImageUrl");
					achievementIntent.putExtra("bagItemArrayList", bagItemArrayList);
					achievementIntent.putExtra("currentUserId", getCurrentUserId());
					
					getApplication().startActivity(achievementIntent);
					
				}
				
				
				
				// TODO: PUT ALL GCM MESSAGE DATA AND PASS TO
				// CONCLUSION ACTIVITY INTENT
				// issue conclusion activity
				if (pointsData != null) {
					
					if (getCurrentUserId() != null) {
						JsonNode pointsJson = service.createJsonNode(pointsData);
						PointsItem pointsItem = pointsItemService.buildPointsItemObject(pointsJson, this);
						
						System.out.println("GCM INTENT SERVICE WELCOME POINTS DATA: " + pointsData);
						
//						if (bonusPointsData != null) {
//							System.out.println("GCM INTENT SERVICE WELCOME BONUS POINTS DATA OBJECT: " + bonusPointsData);
//						}
						
						Intent achievementIntent = new Intent(getBaseContext(), ConclusionActivity.class);
						achievementIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						Intent.FLAG_ACTIVITY_CLEAR_TOP | PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT
//						ConclusionActivity.setContext(this.getBaseContext());
						
						// TODO: SET USER LEVEL
						// TODO: SET WORLD COUNT
						
						achievementIntent.putExtra("conclusionTitle", "Conclusion Title");
						achievementIntent.putExtra("conclusionCopy", "Conclusion copy");
						achievementIntent.putExtra("conclusionImageUrl", "conclusionImageUrl");
						
						// TODO: GET THIS CARD DATA FROM GCM MESSAGE
						conclusionCard = new ConclusionCard();
						conclusionCard.setId("cardId");
						conclusionCard.setTitle("title");
						conclusionCard.setCopy("card copy");
						conclusionCard.setImageUrl("http://wow.zamimg.com/images/wow/icons/large/spell_holy_summonlightwell.jpg");
						conclusionCard.setLevel("level");
						
						achievementIntent.putExtra("conclusionCard", conclusionCard);
//						achievementIntent.putExtra("bagItemArrayList", bagItemArrayList);
						
						
						JsonNode points = pointsJson.path("points");
						String worldCount;
						String currentLevel;
//						String notificationSettingsData = extras.getString("currentLevel");
						
						for (JsonNode point : points) {
							if (point.get("worldCount") != null) {
//								System.out.println("WORLD LOCATIONS WORLD COUNT ****: " + removeDoubleQuotes(point.get("pointValue").toString()));
//								pointsItem.setPoints(removeDoubleQuotes(point.get("pointValue").toString()));
								worldCount = removeDoubleQuotes(point.get("worldCount").toString());
								achievementIntent.putExtra("worldCount", worldCount);
								System.out.println("GCM INTENT SERVICE WORLD COUNT: " + worldCount);
							}
							if (point.get("currentLevel") != null) {
//								System.out.println("WORLD LOCATIONS WORLD COUNT ****: " + removeDoubleQuotes(point.get("pointValue").toString()));
//								pointsItem.setPoints(removeDoubleQuotes(point.get("pointValue").toString()));
								currentLevel = removeDoubleQuotes(point.get("currentLevel").toString());
								achievementIntent.putExtra("currentLevel", currentLevel);
								System.out.println("GCM INTENT SERVICE CURRENT LEVEL: " + currentLevel);
							}	
						}
//						
						
//						String worldCount = points.get("worldCount").toString();
//						// TRAVERSE JSON NODE TO GET THIS VALUE
//						if (worldCount != null) {
//							achievementIntent.putExtra("worldCount", worldCount);
//							System.out.println("GCM INTENT SERVICE WORLD COUNT: " + worldCount);
//						}
						
						// TODO: GET THIS VALUE FROM SOMEWHERE
//						achievementIntent.putExtra("worldCount", "1");
//						pointsItem.setUserId(getCurrentUserId());
						final String pointsUserId = "TEMP_POINTS_USER_ID_" + getCurrentUserId();
						pointsItem.setUserId(getCurrentUserId());
						pointsItem.setPointsUserId(pointsUserId);
						achievementIntent.putExtra("currentUserId", getCurrentUserId());
						achievementIntent.putExtra("pointsItem", pointsItem);
//						getApplication().startActivityForResult(achievementIntent, 1);
						getApplication().startActivity(achievementIntent);
					}

				}
				
				// TODO: create conslusion activity
			} catch (JsonParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
//		String achievementTitle = msg.
//		String json = getIntent().getExtras().getString("my_json_object");
//		JsonObject jObject = new JsonObject(json);
//		String title  = extras.getString("title");
//		String copy  = extras.getString("copy");
		

		
		if (title != null) {
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					this).setSmallIcon(R.drawable.ic_launcher)
		            .setAutoCancel(true)
		            .setDefaults(Notification.DEFAULT_VIBRATE)
					.setContentTitle("Trivia Warlock")
					.setContentText(title)
					.setStyle(new NotificationCompat.BigTextStyle().bigText(msg));
			System.out.println("TITLE: " + title);
			System.out.println("COPY: " + copy);

			// create and start achievement activity

			mNotificationManager = (NotificationManager) this
					.getSystemService(Context.NOTIFICATION_SERVICE);
			
			Intent achievementIntent = new Intent(this, AchievementActivity.class);
			AchievementActivity.setContext(this.getBaseContext());
			
			achievementIntent.putExtra("messageId", NOTIFICATION_ID);
			achievementIntent.putExtra("achievementTitle", title);
			achievementIntent.putExtra("achievementCopy", copy);
			
			
			if (levelUp != null) {
				System.out.println("GCM INTENT SERVICE CONCLUSION LEVEL UP DATA: " + levelUp);
//				levelUpImageUrl
				String levelUpImageUrl = extras.getString("levelUpImageUrl");
				ACHIEVEMENT_IMAGE_URL = levelUpImageUrl;
				achievementIntent.putExtra("levelUp", levelUp);
				
//				achievementIntent.putExtra("levelUpImageUrl", levelUpImageUrl);
			}
			
			achievementIntent.putExtra("achievementImageUrl", ACHIEVEMENT_IMAGE_URL);

			PendingIntent contentIntent = PendingIntent.getActivity(this, 0, achievementIntent, PendingIntent.FLAG_ONE_SHOT);

			mBuilder.setContentIntent(contentIntent);
			mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());			
		}

//		context = this;
//		final AchievementHandler mHandler = new AchievementHandler(this);
	}

	public String removeDoubleQuotes(String string) {
		return string.replaceAll("(^\")|(\"$)", "");
	}
	
	// SET CURRENT USER ID FROM MAIN ACTIVITY
	public static void setCurrentUserId(String userId) {
		currentUserId = userId;
	}
	
	public static String getCurrentUserId() {
		return currentUserId;
	}

//	/**
//	 * Instances of static inner classes do not hold an implicit reference to
//	 * their outer class.
//	 */
//	public static class AchievementHandler extends Handler {
//		private final WeakReference<AchievementActivity> mActivity;
//
//		public AchievementHandler(AchievementActivity activity) {
//			mActivity = new WeakReference<AchievementActivity>(activity);
//		}
//
//		@Override
//		public void handleMessage(Message msg) {
//			AchievementActivity activity = mActivity.get();
//			if (activity != null) {
////				activity.setContext(context);
//			}
//		}
//	}

}
