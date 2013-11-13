package iebaker.xenon.util;

public class Utils {
	public static boolean overlap(float min1, float max1, float min2, float max2) {
		return min1 < max2 && min2 < max1;
	}

	public static boolean within(float min, float max, float N) {
		return N >= min && N <= max;
	}

	public static float clamp(float min, float max, float N) {
		if(N > max) {
			return max;
		} else if(N < min) {
			return min;
		}
		return N;
	}

	public static Float intervalMTV(float amin, float amax, float bmin, float bmax) {
		Float aRight = bmax - amin;
		Float aLeft = amax - bmin;

		if(aLeft < 0 || aRight < 0) return null;
		if(aRight < aLeft) return aRight;
		else return aLeft;
	}
}
