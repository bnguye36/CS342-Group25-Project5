import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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

/*Class that contains the important stuff for the game*/
/*
 * Inner classes include (short description):
 * - Words: Functions and data members that help create the three words that will be used in each round. Each round would have different words (ideally)
 * */
class Game
{
	String word = null;
	String word2 = null;
	String word3 = null;
	
	int numGameWords = 3;		//The number of words each round we decided on
	Words myWords = new Words();
	
	/*Words class*/
	class Words
	{
		/*
		 *  $$For Ali to look at$$
		 *  You can either tell me how you want to do this to aid in your part, or use what I have done for you. I have two options
		 *  I have stored the three words in their own variables called "word", "word2", and "word3"
		 *  or you can access the words in the arraylist called "wordsToGuess"
		 *  
		 *  I do this depending on how the group wants to handle the rest of the game
		 * */
		
		public ArrayList<String> wordsToGuess = new ArrayList<String>();		//The three words that are to be guessed
		
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
		 * Function Name: getFromFile()
		 * Importance: gets three words from the file for each round
		 * @param: NONE
		 * */
		public void getFromFile() throws IOException
		{
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
						}
						
						else 
						{
							break;
						}
						
						wordsToGuess.add(line);
						
					}
					
				}
				
				br.close();		//Close file
			}
				
			
			
		}
		
	}
	
	
}

public class Scramble {
	
	public static void main(String[] args) throws IOException
	{
		Game game = new Game();
		game.myWords.setWordsUsed();
		game.myWords.getFromFile();
		
		System.out.println(game.word);
		System.out.println(game.word2);
		System.out.println(game.word3);
		
		
	}
}
