/*============================================================================*
* Title       : Tagalog Stemmer
* Description : Tagalog Stemmer made with Java. 
* Filename    : TglStemmer.java
* Version     : v1.0
* Author      : Gensaya, Carl Jerwin F.
* Yr&Sec&Uni  : BSCS 3-3 PUP Main
* Subject     : Natural Language Processing
*============================================================================*/

import java.util.*;
import java.io.*;
import java.lang.*;

public class TglStemmer {

    //==============================================INITIALIZATION==============================================//

    private static final String vowels       = "aeiouAEIOU";				  						// Vowel constants
    private static final String consonants   = "bdghklmnngñprstyBDGHKLMNNGÑPRSTY";   			    // Consonant constants
    private static final List<String> prefix = getPrefix("prefix.txt");                             // Prefix
    private static final List<String> infix	 = getInfix("infix.txt");							    // Infix
    private static final List<String> suffix = getSuffix("suffix.txt");                             // Suffix
    private static int period_flag		     = 1;													// Period Flag

    //==========================================================================================================//


    //================================================CODE BODY=================================================//

    /*============================================================================*
     *  Function   : main
     *  Params     : None 
     *  Returns    : Void
     *  Description: Main function of the program.
     *=============================================================================*/
    public static void main(String[] args) {

        ArrayList<String> al_tokens = new ArrayList<String>();
        TglTokenizer tokenize 		= new TglTokenizer();
        TglStemmer stem = new TglStemmer();

        al_tokens = tokenize.GetParams();
        stem.StemTokens(al_tokens);

    } // End of main

    /*============================================================================*
     *  Function   : getPrefix
     *  Params     : String file_name: Prefixes text file container.
     *  Returns    : List<String> prefixes: Return list of prefixes.
     *  Description: Populates the prefixes list.
     *=============================================================================*/
    private static List<String> getPrefix(String file_name) {

        List<String> prefixes = new ArrayList<String>();

        try {
            File file = new File(file_name);
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);

            String str = new String(data, "UTF-8").replace("-", "");
            fis.close();

            prefixes = Arrays.asList(str.split("\\r\\n|\\n|\\r"));
        } catch(Exception e) { }

        return prefixes;

    }

    /*============================================================================*
     *  Function   : getInfix
     *  Params     : String file_name: Infix text file container.
     *  Returns    : List<String> infix: Return list of infix.
     *  Description: Populates the infix list.
     *=============================================================================*/
    private static List<String> getInfix(String file_name) {

        List<String> infixes = new ArrayList<String>();

        try {
            File file = new File(file_name);
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);

            String str = new String(data, "UTF-8").replace("-", "");
            fis.close();

            infixes = Arrays.asList(str.split("\\r\\n|\\n|\\r"));
        } catch(Exception e) { }

        return infixes;

    }

    /*============================================================================*
     *  Function   : getSuffix
     *  Params     : String file_name: Suffix text file container.
     *  Returns    : List<String> suffixes: Return list of suffixes.
     *  Description: Populates the suffixes list.
     *=============================================================================*/
    private static List<String> getSuffix(String file_name) {

        List<String> suffixes = new ArrayList<String>();

        try {
            File file = new File(file_name);
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);

            String str = new String(data, "UTF-8").replace("-", "");
            fis.close();

            suffixes = Arrays.asList(str.split("\\r\\n|\\n|\\r"));
        } catch(Exception e) { }

        return suffixes;

    }

    /*============================================================================*
     *  Function   : StemTokens
     *  Params     : ArrayList<String> al_tokens: Token to be stemmed.
     *  Returns    : void
     *  Description: Stems the token passed into the function.
     *=============================================================================*/
    private void StemTokens(ArrayList<String> al_tokens) {  // STEMMING PROCESS

        ArrayList<String> al_stemmed 		= new ArrayList<String>();
        ArrayList<String> al_stemmed_washed = new ArrayList<String>();
        String dupli_stem1, dupli_stem2, prefix_stem, rep_stem, infix_stem, suffix_stem, check_stem, temp;

        for(String token : al_tokens) { // Loop through all tokens

            temp = token;
            if(period_flag == 1 || period_flag == 0 && Character.isLowerCase(token.charAt(0))) {
                token       = token.toLowerCase();
                dupli_stem1 = CheckDuplication(token);

                prefix_stem = CheckPrefix(dupli_stem1);
                rep_stem    = CheckRepitition(prefix_stem);
                infix_stem 	= CheckInfix(rep_stem);
                suffix_stem = CheckSuffix(infix_stem);
                dupli_stem2 = CheckDuplication(suffix_stem);
                check_stem  = CheckStemmedWord(dupli_stem2);

                StringBuilder sb_temp = new StringBuilder(check_stem);
                if(check_stem.contains("-"))
                    sb_temp.toString().replace("-", "");

                al_stemmed.add(temp + " | " + sb_temp.toString());
                period_flag = 0;

            } else if(period_flag == 0 && Character.isUpperCase(token.charAt(0)))
                al_stemmed.add(temp);

            else if(token.equals("."))
                period_flag = 1;

            else {
                al_stemmed.add(temp);

            }

        } // End of Stemming Process

        for(String str_temp : al_stemmed)	// Print all stemmed words.
            System.out.println(str_temp);
        // End of print words.

    } // End of StemTokens

    /*============================================================================*
     *  Function   : CheckDuplication
     *  Params     : String token: Token to be checked for duplication.
     *  Returns    : String token: Processed stemmed token.
     *  Description: Checks for the presence of duplication in the token.
     *=============================================================================*/
    private String CheckDuplication(String token) { // DUPLICATION

        if(token.contains("-") && token.indexOf("-") != 0 && token.indexOf("-") != token.length() - 1) {
            String[] split_token = token.split("-");

            if(split_token[0].equals(split_token[1]))		// Checks Full Reduplication
                return split_token[0];
            else
                return token;

        } else
            return token;

    } // End of CheckDuplication

    /*============================================================================*
     *  Function   : CheckPrefix
     *  Params     : String token: Token to be stemmed for prefixes.
     *  Returns    : String sb_temp: Resulting processed token.
     *  Description: Stems token for prefix.
     *=============================================================================*/
    private String CheckPrefix(String token) { // PREFIX

        StringBuilder sb_temp = new StringBuilder(token);
        int vowel_count = 0;

        for(String check_prefix : prefix) {
            if(token.length() - check_prefix.length() >= 3) {
                if(check_prefix.equals("i") && CheckConsonant(token.charAt(2)))
                    break;

                else if(token.substring(0, (check_prefix.length())).equals(check_prefix)) {
                    vowel_count = countVowel(token.substring(check_prefix.length(), token.length()));

                    if(vowel_count >= 2) {
                        sb_temp.delete(0, check_prefix.length());
                        break;
                    } else
                        break;
                }
            }
        }

        return sb_temp.toString();

    } // End of CheckPrefix

    /*============================================================================*
     *  Function   : CheckInfix
     *  Params     : String token: Token to be stemmed for infixes.
     *  Returns    : String sb_temp: Resulting processed token.
     *  Description: Stems token for infix.
     *=============================================================================*/
    private String CheckInfix(String token) { // INFIX

        StringBuilder sb_temp = new StringBuilder(token);

        for(String check_infix : infix) {
            if(token.length() - check_infix.length() >= 4) {
                if(token.charAt(0) == token.charAt(4) && token.substring(1, 3).equals(check_infix)) {
                    sb_temp.delete(0, 4);
                    break;
                } else if(token.substring(1, 3).equals(check_infix) && CheckVowel(token.charAt(3))) {
                    sb_temp.delete(1, 3);
                    break;
                }
            }
        }

        return sb_temp.toString();

    } // End of CheckInfix

    /*============================================================================*
     *  Function   : CheckRepitition
     *  Params     : String token: Token to be stemmed for repitition.
     *  Returns    : String sb_temp: Resulting processed token.
     *  Description: Stems token for repitition.
     *=============================================================================*/
    private String CheckRepitition(String token) { // REPITITION

        StringBuilder sb_temp = new StringBuilder(token);

        if(token.length() >= 4) {
            if (CheckVowel(token.charAt(0))) {
                if (token.charAt(0) == token.charAt(1)) {
                    sb_temp.deleteCharAt(0).toString();
                }
            } else if (CheckConsonant(sb_temp.charAt(0)) && token.length() >= 5) {
                if (token.substring(0, 2).equals(token.substring(2, 4))) {
                    sb_temp.delete(0, 2).toString();
                }
            }
        }

        return sb_temp.toString();

    } // End of CheckRepitition

    /*============================================================================*
     *  Function   : CheckVowel
     *  Params     : char letter: Character to be tested.
     *  Returns    : boolean vowels.contains(String.valueOf(letter))
                        : Boolean result for char testing.
     *  Description: Checks if a character is a vowel.
     *=============================================================================*/
    private  boolean CheckVowel(char letter) {	// FIRST LETTER IS VOWEL CHECK
        return vowels.contains(String.valueOf(letter));
    } // End of CheckVowel

    /*============================================================================*
     *  Function   : CheckConsonant
     *  Params     : char letter: Character to be tested.
     *  Returns    : boolean consonants.contains(String.valueOf(letter)): 
                        Boolean result for char testing.
     *  Description: Checks if a character is a consonant.
     *=============================================================================*/
    private boolean CheckConsonant(char letter) { // FIRST LETTER IS CONSONANT CHECK
        return consonants.contains(String.valueOf(letter));
    } // Check of CheckConsonant

    /*============================================================================*
     *  Function   : countVowel
     *  Params     : String token: Token to be stemmed for repitition.
     *  Returns    : int count: Vowel count of the token.
     *  Description: Returns the vowel count of the token.
     *=============================================================================*/
    private int countVowel(String token) { // VOWEL COUNT CHECK
        int count = 0;
        for (int i = 0; i < token.length(); i++) {
            char ch = token.charAt(i);
            if (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u') {
                count++;
            }
        }

        return count;
    }

    /*============================================================================*
     *  Function   : CheckSuffix
     *  Params     : String token: Token to be stemmed for suffix.
     *  Returns    : String sb_temp: Resulting processed token.
     *  Description: Stems token for suffixes.
     *=============================================================================*/
    private String CheckSuffix(String token) {	// SUFFIX

        StringBuilder sb_temp = new StringBuilder(token);

        for(String check_suffix : suffix) {
            if(token.length() - check_suffix.length() >= 4) {
                if(token.substring((token.length() - check_suffix.length()), token.length()).equals(check_suffix)) {
                    sb_temp.delete(token.length() - check_suffix.length(), token.length());
                    return sb_temp.toString();
                }
            }
        }

        return sb_temp.toString();

    } // End of CheckSuffix

    /*============================================================================*
     *  Function   : CheckStemmedWord
     *  Params     : String token: Token to be stemmed for repitition.
     *  Returns    : String sb_temp: Resulting processed token.
     *  Description: Checks resulting stemmed token.
     *=============================================================================*/
    private String CheckStemmedWord(String token) {

        StringBuilder sb_temp = new StringBuilder(token);
        int vowel_count = countVowel(token);

        if(token.length() >= 3) {
            if (CheckConsonant(sb_temp.charAt(sb_temp.length() - 1)) && sb_temp.charAt(token.length() - 2) == 'u')
                sb_temp.setCharAt(token.length() - 2, 'o');

            else if(sb_temp.charAt(sb_temp.length() - 1) == 'u')
                sb_temp.setCharAt(token.length() - 1, 'o');

            else if(sb_temp.charAt(sb_temp.length() - 1) == 'r' && CheckVowel(sb_temp.charAt(sb_temp.length() - 1)))
                sb_temp.setCharAt(token.length() - 1, 'd');

            else if(sb_temp.charAt(sb_temp.length() - 1) == 'r' && sb_temp.charAt(sb_temp.length() - 2) == 'i')
                sb_temp.setCharAt(token.length() - 1, 'd');

            else if(sb_temp.charAt(sb_temp.length() - 1) == 'h' && CheckVowel(sb_temp.charAt(sb_temp.length() - 1)))
                sb_temp.deleteCharAt(token.length() - 1);

            if(sb_temp.charAt(0) == '-')
                sb_temp.deleteCharAt(0);

            if(sb_temp.charAt(0) == 'a' && sb_temp.charAt(1) == 'a')
                sb_temp.deleteCharAt(0);

            if(sb_temp.substring(0, 2).equals("ka") && vowel_count >= 3) {
                sb_temp.delete(0, 2);
            }

            else if(sb_temp.length() >= 2) {
                if(sb_temp.charAt(sb_temp.length() - 1) == 'h' && CheckVowel(sb_temp.charAt(sb_temp.length() - 2)))
                    sb_temp.deleteCharAt(token.length() - 1);
            }


        }

        return sb_temp.toString();
    } // End of CheckStemmedWord

    //==========================================================================================================//

} // End of TglStemmer