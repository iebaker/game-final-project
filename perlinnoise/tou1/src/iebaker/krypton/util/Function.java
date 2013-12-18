package iebaker.krypton.util;

public class Function<I,O>{
	public void on(I input, O output) { 
		return;
	}

	public static void main(String... args) {
		Function<Integer, String> int_to_string = new Function<Integer, String>() {
			@Override public void on(Integer input, String output) {
				output = input + " as a string";
				System.out.println(output);
			}
		};

		String result = null; int_to_string.on(4, result);
		System.out.println(result);
	}
}


