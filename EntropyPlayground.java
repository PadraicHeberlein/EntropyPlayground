import java.lang.Math;
import java.util.*;

public class EntropyPlayground
{					
	static final int BYTE_LENGTH = 8;

	String message;										
	int n;												

	// -------------------------------------------------
	// | 	The main method for testing this class:	   |
	// -------------------------------------------------
	public static void main(String[] args)
	{
		int len = 64;
		String randoMessage = getRandomBitString(len);
		String message = "Eight sided whispering " +
			"haleluluia hat rack!";

		//System.out.println("random message of "
		//	+ len + " bits :\n" + message
		//);

		//int wrapAmount = 79;

		//System.out.println(
		//	"wrap by " 
		//		+ wrapAmount + " bits (modular) : "
		//);

		//System.out.println(wrap(message, wrapAmount));
		//System.out.println();
		
		byte[] bytes = message.getBytes();
		String bitString = "";
		
		for (int i = 0; i < bytes.length; i++)
			bitString += 
				byteToBitString(bytes[i]);

		System.out.println("message : " + message);
		System.out.println("bitString : " + bitString);

		byte[] split = bitStringToByteArray(
			bitString, 4);

		for (int i = 0; i < split.length; i++)
			System.out.print(" [" 
				+ byteToBitString(split[i]) + "]");
		System.out.print("\n");

		byte[] alphaBytes = getAlphabet(message); 

		System.out.println("ab :" 
			+ Arrays.toString(alphaBytes));
	}
	
	// -------------------------------------------------
	// | 	The common constructor:					   |
	// -------------------------------------------------
	public EntropyPlayground(String theMessage)
	{
		message = theMessage;
		n = message.length();
	}

	// -------------------------------------------------
	// | Returns a bit string representation of the	  |
	// | passed-in byte with the LSB as left-most bit |
	// -------------------------------------------------
	public static String byteToBitString(byte b)
	{
		String bitString = "";
		byte mask = 1;

		for (int i = 0; i < BYTE_LENGTH; i++)
			bitString +=
				((mask << i) & b) >> i; 

		return bitString;
	}

	// -------------------------------------------------
	// |	String getRandomBitString(int length)	   |
	// -------------------------------------------------
	// | Returns random string of 1's & 0's of length  |
	// -------------------------------------------------
	static String getRandomBitString(int length)
	{
		double rando;
		String bitString = "";

		for (int i = 0; i < length; i++)
		{	
			rando = Math.random();

			if (rando < 0.5) bitString += '0';
			else bitString += '1';
		}

		return bitString;
	}

	// -------------------------------------------------
	// |	boolean validateBitString(				   |
	// |		String bitString)					   |
	// -------------------------------------------------
	// | Returns 'false' if the bit string contains    |
	// | characters other than (i.e., NOT) '0' OR '1'  |
	// | and returns true otherwise   				   |
	// -------------------------------------------------
	static boolean validateBitString(String bitString)
	{
		int k = bitString.length();

		for (int i = 0; i < k; i++)
		{
			char nextBit = bitString.charAt(i);

			if (!(nextBit == '0' || nextBit == '1'))
				return false; 
		}

		return true; 		
	}
	
	// -------------------------------------------------
	// |	String wrap(String bitString, int amount)  |
	// -------------------------------------------------
	// | If the amount to wrap is greater, then mods   |
	// | out by the length of the bit string and the   |
	// | wraps the first 'amount' number of bits by    |
	// | appending them to the end of the bit string   |
	// -------------------------------------------------
	static String wrap(String bitString, int amount)
	{
		if (amount > bitString.length())
			amount -= bitString.length();

		String wrapString = "";
	
		for (int i = 0; i < amount; i++)
			wrapString += bitString.charAt(i);
	
		return bitString + wrapString;
	}

	// -------------------------------------------------
	// |	String padZeros(						   |
	// |		String bitString, int amount)		   |
	// -------------------------------------------------
	// | Returns the bit string that was passed to the |
	// | function appended on the MSB side of the 	   |
	// | bit strin by 'amount' 0s 					   |
	// -------------------------------------------------
	static String padZeros(String bitString, int amount)
	{
		String padded = bitString;
	
		for (int i = 0; i < amount; i++)
			padded += '0';
		
		return padded;
	}
	
	// -------------------------------------------------
	// | 	byte[] bitStringToByteArray(			   |
	// | 		String bitString, int window)   	   |
	// -------------------------------------------------
	// | Takes the passed-in bit string, pads it with  |
	// | zeros if the length isn't divisible by the    |
	// | size of 'window', then splits the bit string  |
	// | into an array of bytes constructed from 	   |
	// | consequitive substrings with: 				   |
	// |											   |
	// |	length = 'window':			   			   |
	// |											   |
	// -------------------------------------------------
	static byte[] bitStringToByteArray(
		String bitString, int window)
	{
		bitString = modularPadZeros(bitString, window);

		int m = bitString.length() / window;
		byte[] bytes = new byte[m];
		
		for (int i = 0; i < m; i++)
		{
			String nextBitString = "";

			for (int j = 0; j < window; j++)
			{
				int relativeIndex = (i * window) + j;

				nextBitString += 
					bitString.charAt(relativeIndex);
			}

			bytes[i] = bitStringToByte(nextBitString);
		} 

		return bytes;
	}

	// -------------------------------------------------
	// |	String modularPadZeros(					   |
	// | 		String bitString, int windowSize)	   |
	// -------------------------------------------------
	// | Returns the passed-in bit string with 		   |
	// | dependant padding on the length % window	   |
	// -------------------------------------------------
	static String modularPadZeros(
		String bitString, int window)
	{
		int k = bitString.length();
		int theRest = (k % window) == 0 ?
			0 : window - (k % window);
		
		return padZeros(bitString, theRest); 
	}
	
	// -------------------------------------------------
	// |	byte bitStringToByte(String bitString)	   |
	// -------------------------------------------------
	// | If the bit string's length is less than then  |
	// | the length of a byte, this returns a byte 	   |
	// | constructed from the passed in bit string 	   |
	// | argument									   |
	// -------------------------------------------------
	static byte bitStringToByte(String bitString)
	{
		if (bitString.length() > BYTE_LENGTH) 
		{
			throw new IllegalArgumentException(
				"bitString in bitStringToByte() too long!"
			);
		}

		int k = bitString.length();
		byte subEqByte = 0;

		for (int i = 0; i < k; i++)
		{
			int nextBit = (int)(bitString.charAt(i) - '0');
			subEqByte += (nextBit * Math.pow(2, i));
		}

		return subEqByte;	
	}

	// -------------------------------------------------
	static String charStringToBitString(
		String charString)
	{
		String bitString = "";
		byte[] bytes = charString.getBytes();
		
		for (int i = 0; i < bytes.length; i++)
			bitString += byteToBitString(bytes[i]);

		return bitString;
	}

	// -------------------------------------------------
	static double getFrequencyOf(
		byte[] byteArray, byte b)
	{
		double freq = 0.0;

		for (int i = 0; i < byteArray.length; i++)
		{
			if (byteArray[i] == b)
				freq++;
		}

		return freq;
	}

	// -------------------------------------------------
	static byte[] getAlphabet(String charString)
	{
		String bitString = 
			charStringToBitString(charString);

		byte[] byteArray = bitStringToByteArray(
			bitString, BYTE_LENGTH);

		int numSymbols = 0;
		List<Byte> alpha = new ArrayList<Byte>();

		for (int i = 0; i < byteArray.length; i++)
		{
			if (!alpha.contains(byteArray[i]))
				alpha.add(byteArray[i]);
		}
		
		int n = alpha.size();
		byte[] alphaBytes = new byte[n];
	
		for (int i = 0; i < n; i++)
			alphaBytes[i] = alpha.get(i);

		return alphaBytes;
	}
	
	static double calcEntropyFor(String message, int window)
	{
		byte[] messageBytes = bitStringToByteArray(
			charStringToBitString(message), window);
		byte[] alphaBytes = getAlphabet(message);


		return 0.0;
	}
}
