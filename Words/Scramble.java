import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
/* Final update for text file */
/* Class that contains the instructions */
class instructions
{
	/*
	 * Function name: instructions()
	 * Importance: displays instructions to people who want to play
	 * @param: N/A
	 * */
	public void instructions()
	{
		String message = "Welcome to our game of scramble! (Probably not like the original game)\n"
					   + "In order to play the game, 4 players need to be connected and only four players\n"
					   + "Each player has to guess three words that are scrambled. The first player to guess\n"
					   + "the three words first in 2 mintues (each round) is the winner. There are infinitely many rounds\n"
					   + "the first player to win three rounds is the winner of the game.\n"
					   + "If no players guess the three words in 2 mintues, then no one wins.\n";
	}
}

/* Class that contains the important stuff for the game */
/*
 * Inner classes include (short description):
 * - Words: Functions and data members that help create the three words that will be used in each round. Each round would have different words (ideally)
 * */
class Game
{
	boolean wordsSetBool = false;
	int numWordsUsed = 0;
	
	String word = null;
	String word2 = null;
	String word3 = null;
	
	String randomizeWord = null;
	String randomizeWord2 = null;
	String randomizeWord3 = null;
	
	int numGameWords = 3;		//The number of words each round we decided on
	Words myWords = new Words();
	
	/*Words class*/
	class Words
	{		
		/* Dictionary size of 30 words */
		public int numWords = 30;
		
		/*HashMap to keep track if a word was already used to play the game. Don't want any repeats!*/
		public HashMap<Integer, Boolean> wordsUsed = new HashMap<Integer, Boolean>();
		
		/*
		 * Function Name: setWordsUsed()
		 * Importance: fills the wordsUsed hashmap with <lineNum, boolean> to show that each line (word) has not been used in the game
		 * @param: NONE
		 * */
		public void setWordsUsed()
		{
			for(int i = 1; i <= numWords; i++)
			{
				wordsUsed.put(i, false);
			}
		}
		
		/*
		 * Function name: allowDuplicates
		 * Importance: Function allows the reuse of words in the dictionary, if the dictionary is not big enough/game goes on forever
		 * @param: NONE
		 * */
		public void allowDuplicates()
		{
			/* Less than 3 words remaining in dictionary, can reuse words now, 
			   but reset the whole dictionary to get a better distribution */
			if(numWords - numWordsUsed <= 2)
			{
				System.out.println("Allow duplicates");
				
				numWordsUsed = 0;			
				
				// Set hashmap to false to allow reuse of lines in txt file
				for (int i = 1; i <= numWords; i++)
				{
					wordsUsed.put(i, false);
				}
			}
			
			// Duplicates not allowed as we have enough words
			else 
			{
				return;
			}
		}
		
		/*
		 * Function Name: getFromFile()
		 * Importance: gets three words from the file for each round
		 * @param: NONE
		 * */
		public void getFromFile() throws IOException
		{
			
			allowDuplicates();	//Reuse words in dictionary if words run out
			
			
			/* Within this block of code, I produce a random line number between 1
			 *  and numWords (the number of words in the dictionary)
			 */
			//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
			Random rand = new Random();
			int min = 1;
			int max = numWords;
			int randNum = rand.nextInt(max - min + 1) + min;
			//- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
			
			// Loop through to find a line we haven't used yet
			for(int i = 0; i < numGameWords; i++)
			{
				//Loop is here so I can make sure the line I get has never been used in the game yet
				//Note, I'm actually just looking at the line numbers, not actually getting the word here
				while(true)
				{
					if(wordsUsed.get(randNum) == true)		//line is already used, generate a new random number
					{
						randNum = rand.nextInt(max - min + 1) + min;
					}
					
					else		//Never used line, so break out of the while loop											
					{
						wordsUsed.put(randNum, true);
						break;
					}
				}
				
				/*Stuff to read the text file*/
				// = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
				int lineNum = 0;
				BufferedReader br = new BufferedReader(new FileReader("FinalDictionary.txt"));
				String line = null;
				// = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
				
				
				while ((line = br.readLine()) != null)		//Loop through the file
				{
					lineNum++;
					
					/*Check to see if this is the lineNum we want*/
					if(lineNum == randNum)
					{
						
						/*If statements to fill the words into the game*/
						// - - - - - - - - Start IF-ElSE-STATEMENTS - - - - - - - 
						if(word == null)
						{
							word = line;
							randomizeWord = shuffle(word);
						}
						
						else if (word2 == null)
						{
							word2 = line;
							randomizeWord2 = shuffle(word2);
						}
						
						else if(word3 == null)
						{
							word3 = line;
							randomizeWord3 = shuffle(word3);
							
							wordsSetBool = true;
							
						}
						
						// Stop getting words (we already got all three words for a round)
						else 
						{
							break;
						}
						// - - - - - - - - End IF-ElSE-STATEMENTS - - - - - - - 
						
						numWordsUsed++;
						
					}
					
				}
				
				br.close();		//Close file
			}
				
		}
		
		/*
		 * Function Name: clearWords
		 * Importance: Clears words for the next round. Must do this to get new words from the text file
		 * @param: NONE
		 * */
		public void clearWords()
		{
			/* Clear these word items to null */
			// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
			word = null;
			word2 = null;
			word3 = null;
			
			randomizeWord = null;
			randomizeWord2 = null;
			randomizeWord3 = null;
			// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
			
			wordsSetBool = false;		// Used for debugging only
		}
		
		/*
		 * Function Name: Shuffle
		 * Importance: Shuffles a string
		 * @param: (String input)
		 * Return value: String - a scrambled string
		 * */
		public String shuffle(String input)
		{
			
			ArrayList<Character> charAL = new ArrayList<Character>();
			
			// Store the characters of the string into a ArrayList
			for(char c:input.toCharArray())
			{
				charAL.add(c);
			}
			
			StringBuilder output = new StringBuilder(input.length());		//Use this to build the string
			
			// Work to randomize the string
			while(charAL.size() != 0)
			{
				/* Use a random number to randomize the string */
				// - - - - - - - - - - - - - - - - - - - - - - - -
				int min = 0;
				int max = charAL.size() - 1;
				Random rand = new Random();
				int randNum = rand.nextInt((max - min) + 1) + min;
				// - - - - - - - - - - - - - - - - - - - - - - - -
				
				output.append(charAL.remove(randNum));		//Append to builder to make a scrambled string
			}
			
		
			return output.toString();		// Return a scrambled string 
		}
		
	}
	
	
}

public class Scramble {
	
	public static void main(String[] args) throws IOException
	{
		// Sample test
		Game game = new Game();
		game.myWords.setWordsUsed();		//Set words used to false for all words (Must do this)
		
		for(int i = 0; i < 11; i++)
		{
			System.out.println("Clearing words");
			game.myWords.clearWords();		//Must clear words to create new words
			
			System.out.println("Getting three words from file");
			game.myWords.getFromFile();		// Get words from text file
			
			//Print out the words that were generated
			System.out.println();
			System.out.println("Real words");
			System.out.println("-------------------------------");
			System.out.println(game.word);
			System.out.println(game.word2);
			System.out.println(game.word3);
			System.out.println("-------------------------------");
			System.out.println();
			System.out.println("Scrambled words");
			System.out.println("-------------------------------");
			System.out.println(game.randomizeWord);
			System.out.println(game.randomizeWord2);
			System.out.println(game.randomizeWord3);
			System.out.println("-------------------------------");
		}
		
	}
}
