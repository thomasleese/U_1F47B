package bot;

public class Util {

	public static int modulo(int c, int mod) {
		int result = c % mod;
		return result < 0 ? mod + result : result;
	}

	public static double round(double d, int digits) {
		double factor = Math.pow(10.0, (double)digits);
		return (double)Math.round(d * factor) / factor;
	}

	public static double roundTo1(double d) {
			return (double)Math.round(d * 10) / 10;
	}

}