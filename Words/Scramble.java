import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
/* 4/29/2019 Update
 * Added some stuff */
/*Class that contains the instructions*/
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
		/*Num words of dictionary (We agreed 15 is good, but actually maybe too little)*/
		public int numWords = 10;
		
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
			if(numWords - numWordsUsed <= 2)
			{
				System.out.println("Allow duplicates");
				
				numWordsUsed = 0;
				
				for (int i = 1; i <= numWords; i++)
				{
					wordsUsed.put(i, false);
				}
			}
			
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
				BufferedReader br = new BufferedReader(new FileReader("Names.txt"));
				String line = null;
				// = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =
				
				
				while ((line = br.readLine()) != null)		//Loop through the file
				{
					lineNum++;
					
					/*Check to see if this is the lineNum we want*/
					if(lineNum == randNum)
					{
						
						/*If statements to fill the words into the game*/
						if(word == null)
						{
							word = line;
						}
						
						else if (word2 == null)
						{
							word2 = line;
						}
						
						else if(word3 == null)
						{
							word3 = line;
							wordsSetBool = true;
						}
						
						else 
						{
							break;
						}
						
						numWordsUsed++;
						
					}
					
				}
				
				br.close();		//Close file
			}
				
		}
		
		/*
		 * Function Name: clearWords
		 * Clears words for the next round. Must do this to get new words from the text file
		 * @param: NONE
		 * */
		public void clearWords()
		{
			// Clear these word items to null
			// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
			word = null;
			word2 = null;
			word3 = null;
			
			randomizeWord = null;
			randomizeWord2 = null;
			randomizeWord3 = null;
			// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
			
			wordsSetBool = false;
		}
		
	}
	
	
}

public class Scramble {
	
	public static void main(String[] args) throws IOException
	{
		//Sample test
		Game game = new Game();
		game.myWords.setWordsUsed();		//Set words used to false for all words
		
		for(int i = 0; i < 7; i++)
		{
			System.out.println("Clearing words");
			game.myWords.clearWords();		//Must clear words to create new words
			
			System.out.println("Getting three words from file");
			game.myWords.getFromFile();
			
			//Print out the words that were generated
			System.out.println();
			System.out.println("-------------------------------");
			System.out.println(game.word);
			System.out.println(game.word2);
			System.out.println(game.word3);
			System.out.println("-------------------------------");
			System.out.println();
		}
		
	}
}
