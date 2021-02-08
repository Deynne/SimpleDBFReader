package br.com.pbprev.util;

public class DbfUtils {

	
	private static int getRightPos(byte[] b_array) {

		int pos = b_array.length - 1;
		while (pos >= 0 && b_array[pos] == (byte) ' ') {
			pos--;
		}
		return pos;
	}
	
	public static byte[] trimRightSpaces(byte[] b_array) {
		if (b_array == null || b_array.length == 0) {
			return new byte[0];
		}
		int pos = getRightPos(b_array);
		int length = pos + 1;
		byte[] newBytes = new byte[length];
		System.arraycopy(b_array, 0, newBytes, 0, length);
		return newBytes;
	}
}
