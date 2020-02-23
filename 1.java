package oose.w2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * 
 * @author 
 *
 */
public class Util {
	/**
	 * A custom reader that reads first n characters from a file and swaps 
	 * every even char with a blank then prints the output. For a file with
	 * less than n chars, customCharReader does not care about how many 
	 * characters were actually read by the BufferedReader.
	 * @param file
	 * @param n
	 */
	public void customCharReader(File file, int n) {
		char[] buffer = new char[n];
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			int i = 0;
			int count =reader.read();
			while (count!=-1) {
				// when count == -1, the reading of file is finished
				char content = (char) count;
				// checking whether current character is empty
				if (content!= ' ' &&content!='\t'&&content!='\r' && content!='\n') {
					//if current char is not empty and must be int
					if (i<n) {
						// when index is small than n, copy the content to buffer
						buffer[i++] = content; 
					}else {
						// when the buffer is full, file content is greater than buffer
						break;
					}
				}
				count = reader.read();
			}
			reader.close();
			// count the number of elements in the array
			int sum=0;
			for (char each:buffer) {
				if (each!='\0') {
					sum++;
				}
			}
			// updating the buffer,when the buffer is not full
			if (sum!=0) {
				buffer =  Arrays.copyOfRange(buffer,0,sum);
			}else {
				//sum=0 means the file could be empty or no int found in the content
				throw new IOException("File is empty or the content is invalid");
			}
		}catch(RuntimeException e) {
			throw e;
		}catch (IOException e) {
			e.printStackTrace();
		}catch(Exception e) {
			System.out.println("Something went wrong");
		}finally {
			try {
				if (reader!=null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		for(int i=1;i<buffer.length;i++) {
			int swap = i % 2;
			if ((Integer.toString(swap)).equals(Integer.toString(1))) { 
				buffer[i] = ' ';
			}			
		}
		System.out.println(buffer);
	}
	
	/**
	 * This function swaps an old character with a new one in a text string
	 * @param text
	 * @param oldchar
	 * @param newchar
	 * @return String
	 */
	public static String charReplace(String text, char oldchar, char newchar) {
		String textUpdated = null;
		try {
			if (text.indexOf(oldchar)>0) {
				textUpdated = text.replace(oldchar, newchar);		
			}else if(text.indexOf(oldchar)<0) {
				textUpdated = text;
				System.out.println("Invalid content, cannot find the oldchar in the text");
			}
		}catch (NullPointerException e) {
			System.out.println("the text is null");
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return textUpdated;
	}

}