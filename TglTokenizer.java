/*============================================================================*
* Title       : Tagalog Stemmer
* Description : Tagalog Stemmer made with Java. 
* Filename    : TglTokenizer.java
* Version     : v1.0
* Author      : Santelices, Andrew P.
* Yr&Sec&Uni  : BSCS 3-3 PUP Main
* Subject     : Natural Language Processing
*============================================================================*/

import java.util.*;
import java.io.*;
import java.lang.*;


public class TglTokenizer{

    // Just for initialization of object instantiation
    public TglTokenizer(){}

    // Class contructor with arguments
    public TglTokenizer(String text){
        tokenize(text);
    }

    // Gets input from a file and pass it tokenize() function
    private ArrayList<String> tokenizeFromFile(String filename){
        
        String strInput = new String(), // Variable to hold input to be tokenized
                strLine = new String();

        try{

            BufferedReader buffRead = new BufferedReader(new FileReader(filename));

            while((strLine = buffRead.readLine()) != null){
                strInput += strLine +'\n';
            }
            buffRead.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }

        return tokenize(strInput);
    }

    // Tokenizes input -- returns array of tokens
    private ArrayList<String> tokenize(String text){

        ArrayList<String> tokens = new ArrayList<String>(),
                word 	 = new ArrayList<String>(),
                punct  = new ArrayList<String>();

        StringBuilder strBuild = new StringBuilder();

        text = text + '\0';

        // Iterates through the text
        for(int intCtr = 0; intCtr < text.length(); intCtr++){

            char currChar = text.charAt(intCtr);

            //Checks if whitespace or null or newline
            if(Character.isWhitespace(currChar) || currChar == '\0' || currChar == '\n'){

                if(word.isEmpty() && punct.isEmpty())		// Checks if there are any words yet stored in 'word'. None? Skip iteration.
                    continue;								// ^ Or punctuations in 'punct'.
                else if(!word.isEmpty())					// If 'word' is not empty, add it to 'tokens' List, then clears 'word' List.
                    addToken(strBuild, tokens, word); 		// Adds a word or punctuation to 'token'
                else if(!punct.isEmpty())					// Same treatment with 'word'.
                    addToken(strBuild, tokens, punct);

            }else if(Character.isLetter(currChar) || currChar == '-'){
                word.add(Character.toString(currChar));			// Adds every char to 'word' List.
                addToken(strBuild, tokens, punct);				// Adds a punct if a word is found.
            }else if(Character.toString(currChar).matches("\\p{Punct}")){
                punct.add(Character.toString(currChar));		// Adds every punct char to 'punct' List.
                addToken(strBuild, tokens, word);				// Adds a word if a punctuation is found.
            }

        }

        return tokens;
    }

    // Adds a word or a punctuation to the list of tokens
    private void addToken(StringBuilder sb, ArrayList<String> tokens, ArrayList<String> value){

        if(!value.isEmpty()){
            // Converts 'value' List to String before adding to 'tokens' List.
            for(String str : value)
                sb.append(str);

            tokens.add(sb.toString());
            value.clear();
            sb.setLength(0);	// Clears strBuild buffer
        }
    }

    /* Comment out/remove this main method when this tokenizer will be implemented
       to the other modules. */
    public ArrayList<String> GetParams() {

        TglTokenizer tokenizer = new TglTokenizer();
        String text = null;
        ArrayList<String> tokens = new ArrayList<String>();

        System.out.println("Enter text from: ");
        System.out.print("\t[1] Input Stream\n\t[2] File\n\t>> ");

        Scanner input = new Scanner(System.in);
        int choice = input.nextInt();

        input.nextLine(); //Throws away /n (problem with input buffers)

        if(choice == 1){
            System.out.print("Enter text: ");
            text = input.nextLine();
            tokens = tokenizer.tokenize(text);
        }else if(choice == 2){
            System.out.print("Enter filename: ");
            tokens = tokenizer.tokenizeFromFile("tagdic.txt");
        }else
            System.out.println("Invalid input!");

        return tokens;

    }

}


