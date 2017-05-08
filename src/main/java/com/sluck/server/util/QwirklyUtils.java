package com.sluck.server.util;

import java.math.BigInteger;
import java.security.SecureRandom;

public class QwirklyUtils {
	public static String generateToken() {
		SecureRandom random = new SecureRandom();
		return new BigInteger(130, random).toString(32);
	}
}
