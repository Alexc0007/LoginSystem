import java.util.*;
public class Authenticate 
{
	/*********************************
	 * Instance Variables
	 *********************************/
	private String[] specialCharacters; //array of special characters
	private int passMinLength;
	private int passMaxLength;
	private int userMinLength;
	private int userMaxLength;
	private int SpecialChars; //amount of special characters in the password
	private String[] capitalLetters;
	private String[] letters;
	private String[] numbers;
	private int minCapitalLetters;
	
	
	
	
	
	
	/*********************************
	 * Constructor
	 *********************************/
	public Authenticate ()
	{
		specialCharacters = new String[] { "+", "-", "&", "|", "!", "(", ")", "{", "}", "[", "]", "^","~", "*", "?", ":" , "\"" , "\\" , "#" }; //define the special characters
		letters = new String[] {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"}; //define normal letters
		capitalLetters = new String[] {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"}; //define capital letters
		numbers = new String[] {"0" , "1" , "2" , "3" , "4" , "5" , "6" , "7" , "8" , "9"};
		passMinLength = 8; //min password length
		passMaxLength = 12; //max password length
		SpecialChars = 1; //only 1 special character
		minCapitalLetters = 1; //at least 1 capital letter
		userMinLength = 3;
		userMaxLength = 10;
	}
	
	/*
	 * Method that performs authentication to the password inserted by the user - to make sure it fits all the initial limitations
	 * returns true if password stands to the initial limitations
	 * returns false when it doesnt
	 */
	public boolean authenticatePassword(String text)
	{
		/********************************************************************************************************************************************************************
		 * password should be constructed from lowercase letters , at least 1 capital letter , 1 special character , at least 8 characters long , at most 12 characters long
		 * and any numbers
		 ********************************************************************************************************************************************************************/
		List<String> allowedCharacters = new ArrayList<String>(Arrays.asList(letters));
		allowedCharacters.addAll(Arrays.asList(capitalLetters));
		allowedCharacters.addAll(Arrays.asList(specialCharacters));
		allowedCharacters.addAll(Arrays.asList(numbers));
		int specialCounter = 0;
		int capitalCounter = 0;
		
		//check for password length
		if(text.length() > passMaxLength || text.length() < passMinLength)
			return false;
		
		//go over the password
		for(int i=0;i<text.length();i++)
		{
			char testeLetter = text.charAt(i);
			//check for allowed characters
			boolean found = allowedCharacters.contains(Character.toString(testeLetter));    //Arrays.asList(allowedCharacters).contains(Character.toString(testeLetter));
			if(!found)
			{
				return false;
			}
			
			//count the number of special characters in password
			boolean foundSpecial = Arrays.asList(specialCharacters).contains(Character.toString(testeLetter));
			if(foundSpecial)
			{
				specialCounter++;
			}
			//count the number of capital letters in the password
			boolean foundCapital = Arrays.asList(capitalLetters).contains(Character.toString(testeLetter));
			if(foundCapital)
			{
				capitalCounter++;
			}
		}
		if(specialCounter != SpecialChars)
			return false;
		if(capitalCounter < minCapitalLetters)
			return false;
		//if all the checks pass - then return true
		return true;
	}
	
	/*
	 * Method that performs authentication to the username inserted by the user - to make sure it fits all the initial limitations
	 * returns true if username stands to the initial limitations
	 * returns false when it doesnt
	 */
	public boolean authenticateUsername(String text)
	{
		/*********************************************************************************************************************************************
		 * username should be constructed only from lowercase letters or capital letters , at most 10 characters long , at least 3 characters long
		 *********************************************************************************************************************************************/
		List<String> allowedCharacters = new ArrayList<String>(Arrays.asList(letters));
		allowedCharacters.addAll(Arrays.asList(capitalLetters));
		
		//check for username length
		if(text.length() > userMaxLength || text.length() < userMinLength)
			return false;
		
		//go over the username
		for(int i=0;i<text.length();i++)
		{
			char testeLetter = text.charAt(i);
			//check for allowed characters
			boolean found = allowedCharacters.contains(Character.toString(testeLetter));    //Arrays.asList(allowedCharacters).contains(Character.toString(testeLetter));
			if(!found)
			{
				return false;
			}
		}
		//if all the checks pass - then return true
		return true;
	}
	
	
	
	
	
}
