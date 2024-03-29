package com.movie.locations.service;

import java.io.IOException;
import java.io.InputStream;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import com.movie.locations.database.UserImpl;
import com.movie.locations.domain.User;
import android.content.Context;

public class UserService implements IService {
	
	private Context context;
	private UserImpl userImpl;

	public UserService(Context context) {
		this.context = context;
	}
	
	public void createUserImpl() {
		userImpl = new UserImpl(context);
	}

	public User selectRecordById(String userId) {
		userImpl.open();
		User currentUser = userImpl.selectRecordById(userId);
		userImpl.close();
		return currentUser;
	}
	
	public void createRecord(User user) {
		userImpl.open();
		userImpl.createRecord(user);
		userImpl.close();
	}
	
	public void setCurrentPoints(String userId, String updatedPoints) {
		userImpl.open();
		userImpl.setCurrentPoints(userId, updatedPoints);
		userImpl.close();
	}
	
	public void setCurrentLevel(String userId, String updatedLevel) {
		userImpl.open();
		userImpl.close();
	}

	public void setWorldCount(String currentUserId, String updatedWorldCount) {
		userImpl.open();
		userImpl.setWorldCount(currentUserId, updatedWorldCount);
		userImpl.close();
	}

	public void updateUserNotificationPreferences(String userId,
			String email, String notifications) {
		userImpl.open();
		userImpl.updateUserNotificationPreferences(userId, email, notifications);
		userImpl.close();
	}
	
	@Override
	public JsonNode createJsonNode(String msg) throws JsonParseException,
			IOException {

		return null;
	}

	@Override
	public void createContentValues(InputStream stream) {

		
	}

	@Override
	public String removeDoubleQuotes(String string) {

		return null;
	}
}