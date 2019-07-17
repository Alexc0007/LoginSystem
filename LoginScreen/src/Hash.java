import java.security.*;
import java.util.Random;
import javax.xml.bind.DatatypeConverter;



public class Hash 
{
	/**********************
	 * Instance Variables
	 **********************/
	private int saltLength;
	private String algorithm;
	final int DEFAULT_SALT_LENGTH = 8;
	
	/********************************************************
	 * Constructors - to choose from , depending on the need
	 ********************************************************/
	//empty constructor - default values
	public Hash ()
	{
		algorithm = "SHA-256";
		saltLength = DEFAULT_SALT_LENGTH;
	}
	
	//constructor with the option to select a different algorithm
	public Hash(String algorithm)
	{
		this.algorithm = algorithm;
		saltLength = DEFAULT_SALT_LENGTH;
	}
	
	//constructor with the option to select hashing algorithm + salt length
	public Hash (String algorithm , int saltLength)
	{
		this.algorithm = algorithm;
		this.saltLength = saltLength;
	}
	
	
	
	/****************
	 * Methods
	 ***************/
	
	/*
	 * method that generates random salt - 8 characters long
	 * returns the salt generated
	 */
	public String generateSalt()
	{
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"; //allowed chars for salt
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < saltLength)
		{
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}

		String generatedSalt = salt.toString();
		return generatedSalt;
	}
	
	/*
	 * method that generates a hash from a given password and salt
	 * will return the hash value
	 */
	public String getHash(String password , String salt)
	{
		String saltedInput = password + salt; //concat the password with the salt
		byte[] input = saltedInput.getBytes(); //make an array of bytes out of it
		String hashValue = ""; 
		
		try 
		{
			MessageDigest hashObject = MessageDigest.getInstance(algorithm);
			hashObject.update(input);
			byte[] digestedBytes = hashObject.digest();
			hashValue = DatatypeConverter.printHexBinary(digestedBytes).toLowerCase();
		} catch (NoSuchAlgorithmException e) 
		{
			System.out.println("the requested algorithm for hashing doesnt exist");
			e.printStackTrace();
		}
		return hashValue;
	}
}


