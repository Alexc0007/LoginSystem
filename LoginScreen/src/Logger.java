import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger 
{
	/***************************
	 * Instance Variables
	 ***************************/
	private String fileName;
	private BufferedReader reader;
	
	/**************
	 * Constructor
	 *************/
	public Logger ()
	{
		fileName = "passwords.txt";
	}
	
	
	
	
	
	/************
	 * Methods
	 ***********/
	
	/*
	 * method that stores credentials in the passwords file
	 * return false when failed
	 * return true when successfully stored credentials
	 */
	public boolean storeCredentials(String username , String password , String salt)
	{
		String content = username + " " + password + " " + salt; //create the line to write to the passwords file
		try 
		{
			FileWriter fw = new FileWriter(fileName , true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw);
			out.println(content); //write the credentials to the passwords file
			out.close(); //close the writer
		} catch (IOException e) 
		{
			System.out.println(fileName + "is already open!");
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/*
	 * a method that confirm's the user's credentials on the passwords file
	 * if credentials are confirmed and true - return true
	 * if not , return false
	 */
	public boolean confirmCredentials(String username , String hash)
	{
		try 
		{
			reader = new BufferedReader(new FileReader(fileName));
			String[] lineArray;
			String line = reader.readLine(); //read the first line
			if(line != null)
			{
				lineArray = line.split(" ");
				if (lineArray[0].compareTo(username) == 0 && lineArray[1].compareTo(hash) == 0)
					return true;
			}
			while(line != null)
			{
				line = reader.readLine(); //read the next line
				if(line != null)
				{
					lineArray = line.split(" "); //make split to array
					if (lineArray[0].compareTo(username) == 0 && lineArray[1].compareTo(hash) == 0)
						return true;
				}
			}
			reader.close();
		} catch (IOException e) 
		{
			System.out.println(fileName + "not found!");
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	
	/*
	 * method to find the salt of a specific username
	 * return the salt if found
	 * return "NO" if didnt find the current username on the passwords file
	 */
	public String getSalt(String username)
	{
		String[] lineArray;
		try 
		{
			reader = new BufferedReader(new FileReader(fileName));
			String line = reader.readLine(); //read the first line
			if(line != null)
			{
				lineArray = line.split(" ");
				if(lineArray[0].compareTo(username) == 0)
					return lineArray[2]; //return the salt of username
			}
			while(line != null)
			{
				line = reader.readLine();
				if(line != null)
				{
					lineArray = line.split(" ");
					if(lineArray[0].compareTo(username) == 0)
						return lineArray[2]; //return the salt of username
				}
			}
			reader.close();
		} catch (IOException e) 
		{
			System.out.println("IO Exception on getSalt method");
			e.printStackTrace();
		}
		return "NO";
	}
}
