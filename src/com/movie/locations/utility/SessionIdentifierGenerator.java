package com.movie.locations.utility;

import android.annotation.SuppressLint;
import java.math.BigInteger;
import java.security.SecureRandom;

public final class SessionIdentifierGenerator {
	// this implementation is only to generate
	// a suitable pre-checkin 
	@SuppressLint("TrulyRandom")
	private SecureRandom random = new SecureRandom();

	public String nextSessionId() {
		return new BigInteger(130, random).toString(32);
	}
}
