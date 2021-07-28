
import java.util.ArrayList;

/**
 * This FalloutTerminal program       and is part of
 * Assignment 1. I love Fallout!
 * 
 * @author Ian Kenny, Munir Suleman
 * @version 1.0
 * @since 2020-10-15
 * 
 */

import java.util.Random;
import java.util.Scanner;

public class FalloutTerminal {

	/**
	 * This method creates a single line of the terminal display.
	 * @param startAddress The first address of the line.
	 * @param wordProbability If less than or equal to 0.6 the line should contain one word.
	 * @return The line for display.
	 */
	public String buildDisplayLine(int startAddress, float wordProbability) {

		/*
		 *  Add your code here. When you have selected a random word
		 *  to add to the display, you must call addSelectedWord() to 
		 *  save that word. For example, if your variable holding the word is
		 *  called `word' you would call as follows:
		 * 	addSelectedWord(word);
		 * 
		 */
		
		StringBuilder oneLine = new StringBuilder();
		
		int a;
		char letter;
		StringBuilder allChar = new StringBuilder();
		//creat string of ascii characters from 33-126
		for(a = 33; a <= 126; a++)
	      {
	        letter = (char)a;
	        allChar.append(letter);
	      }
		//remove the number, non cap and cap letters from the string leaving only special characters
		StringBuilder allChar1 = allChar.delete(15, 25);
		StringBuilder allChar2 = allChar.delete(22, 48);
		StringBuilder specChar = allChar.delete(28, 54);
		
		// Randomly generate a number and pick the character corresponding to that number from the special string
		Random r = new Random();
		int index = r.nextInt(specChar.length());
		char c = specChar.charAt(index);
		StringBuilder randomString1 = new StringBuilder();
		randomString1 = randomString1.append(c);
		// do last step to create a string of 30 random characters
		for(int i = 1;i <= 74;i++)
		{
			index = r.nextInt(specChar.length());
			c = specChar.charAt(index);
			randomString1 = randomString1.append(c);
		}
		//full string of random characters
		oneLine = oneLine.append(randomString1);
		//Replace the first few characters with the first address converted to Hex
		String hexStartAddress = Integer.toHexString(startAddress);
		int otherAddress = startAddress + 240; //240 because 30*8bits
		
		if(hexStartAddress.length()>=5)
			hexStartAddress = hexStartAddress.substring(hexStartAddress.length()-4);
		
		String firstAddress = ("0x" + hexStartAddress + " ");
		oneLine = oneLine.replace(0, 7, firstAddress);
		
		//Replace the second address into the string
		String hexOtherAddress = Integer.toHexString(otherAddress);
		if(hexOtherAddress.length()>=5)
			hexOtherAddress = hexOtherAddress.substring(hexOtherAddress.length()-4);
		
		String secondAddress = (" 0x" + hexOtherAddress + " ");
		oneLine = oneLine.replace(37, 45, secondAddress);
		
		// from 7-29 and 45-67 random number generator
		Random rand = new Random();
		int randStart = rand.nextInt(46); //random number from 0-45 inclusive
		if(randStart <= 22) {
			randStart = randStart + 7;
		}
		else {
			randStart = randStart + 22;
		}
		int randEnd = randStart + 8;
		//get a random word and place it somewhere in the special characters region if the wordProb <=0.6
		String word = getRandomWord();
		addSelectedWord(word);
		if(wordProbability <= 0.6f) {
			oneLine = oneLine.replace(randStart, randEnd, word);
		}
		//convert from stringbuilder to string
		String oneLineFinal = oneLine.toString();
		
		return oneLineFinal;
		
	}

/*
 ***************************************************************************************	
 */

	private static final int START_ADDRESS = 0x9380;
	private static final int DISPLAY_HEIGHT = 20;
	private static final int NUM_CHARACTERS_PER_ROW = 60;
	private static final float WORD_PROB_1 = 0.6f;
	private static final int WORD_LENGTH = 8;
	private static final int NUM_GUESSES_ALLOWED = 4;
	private static final int BYTE_SIZE = 8;
	private static Random rand;
	private static final String[] words = {
			"flourish",
			"appendix",
			"separate",
			"unlawful",
			"platform",
			"shoulder",
			"marriage",
			"attitude",
			"reliable",
			"contempt",
			"prestige",
			"evaluate",
			"division",
			"birthday",
			"orthodox",
			"appetite",
			"perceive",
			"pleasant",
			"surprise",
			"elephant",
			"incident",
			"medieval",
			"absolute",
			"dominate",
			"designer",
			"misplace",
			"possible",
			"graduate",
			"solution",
			"governor"
	};
	
	private static String password;

	private static ArrayList<String> selectedWords = new ArrayList<String>();
	
	private void run() {
		
		System.out.println(buildDisplay());
		
		Scanner in = new Scanner(System.in);
		
		int guessCount = NUM_GUESSES_ALLOWED;
		
		boolean done = false;
		
		do {
			System.out.println("> Password required.");
			System.out.println("> Attempts remaining = " + guessCount);
			System.out.println(">");
			String guess = in.nextLine();
			
			if (guess.equalsIgnoreCase(getPassword())) {
				System.out.println("> Access granted.");
				done = true;
			}
			
			else {
				--guessCount;
				System.out.println("> Access denied.");
				System.out.println("> Likeness = " + getCorrectCount(guess, password));
			}	
		} while (guessCount > 0 && !done);
		
		if (guessCount == 0) {
			System.out.println("> Initiating lockout");
		}
		
		in.close();
	}
	
	private int getCorrectCount(String guess, String password) {
		
		int count = 0;
		
		for (int i = 0; i < guess.length(); i++) {			
			if (guess.charAt(i) == password.charAt(i)) {
				count++;
			}
		}
		
		return count;
	}
		
	private String getRandomWord() {
		return words[rand.nextInt(words.length)];
	}
	
	private String buildDisplay() {
		
		String ret = "";
		
		int address = START_ADDRESS;
						
		for (int i = 0; i < DISPLAY_HEIGHT; i++) {
											
			float rf = rand.nextFloat();
			
			String line = buildDisplayLine(address, rf);

			ret += line + "\n";
			
			address += BYTE_SIZE * NUM_CHARACTERS_PER_ROW;
		}
		
		setRandomPassword();
		
		return ret;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String pw) {
		password = pw;
	}

	private void addSelectedWord(String word) {
		selectedWords.add(word);
	}
	
	public void setRandomPassword() {
		password = selectedWords.get(rand.nextInt(selectedWords.size())).toLowerCase();
	}

	public FalloutTerminal() {
		rand = new Random(System.currentTimeMillis());	
	}
	
	public static void main(String[] args) {
		new FalloutTerminal().run();
	}
}
