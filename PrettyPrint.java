import java.util.*;
import java.io.*;
import java.time.chrono.MinguoChronology;

public class PrettyPrint {

    public static double calcSlack(int current_length, int line_length, SlackFunctor sf) {
        return sf.f(line_length - current_length);
    }

    public static List<Integer> splitWords(int[] lengths, int L, SlackFunctor sf) {

        ArrayList<Double> slacks = new ArrayList<>(lengths.length + 1);
        ArrayList<Integer> breaks = new ArrayList<>(lengths.length + 1);
        LinkedList<Integer> output = new LinkedList<>();
        int n = lengths.length;

        // Check if any word length is greater than L
        for (int length : lengths) {
            if (length > L) {
                return null; // No solution possible
            }
        }
        slacks.add(0.0);
        breaks.add(0);
        for (int i = 1; i <= n; i++) {
            double min_slack = Double.MAX_VALUE;
            int min_break = -1;
            int curr_L = 0;
            for (int j = i - 1; j >= 0; j--) {
                if (j == (i - 1)) {
                    curr_L += lengths[j];
                } 
                
                else {
                    curr_L += lengths[j] + 1;
                }
                if (curr_L > L) {
                    break;
                }
                double curr_min = slacks.get(j) + sf.f(L - curr_L);
                if (curr_min < min_slack) {
                    min_slack = curr_min;
                    min_break = j;
                }    
            }
            breaks.add(min_break);
            slacks.add(min_slack);
        }
        int i = n;
        while (i > 0) {
            output.addFirst(i-1);
            i = breaks.get(i);
        }

        return output;
    }

    public static List<Integer> greedy_print(int[] lengths, int L, SlackFunctor sf) {

        /*  Greedy implementation

            It just places words on lines and inserts line breaks whenever the length
            would exceed L.
        */

        ArrayList<Integer> breaks = new ArrayList<Integer>();
        int current_length = 0;
        int current_end = -1;
        for (int word: lengths) {
            if (word > L) {
                return null;
            }

            if (current_length > 0 && current_length + 1 + word > L) {
                breaks.add(current_end);
                current_length = 0;
            }

            if (current_length > 0)
                current_length++;
            current_length += word;

            current_end++;
        }

        breaks.add(current_end);

        return breaks;
    }
    

    public static String help_message() {
        return
            "Usage: java PrettyPrint line_length [inputfile] [outputfile]\n" +
            "  line_length (required): an integer specifying the maximum length for a line\n" +
            "  inputfile (optional): a file from which to read the input text (stdin if a hyphen or omitted)\n" +
            "  outputfile (optional): a file in which to store the output text (stdout if omitted)"
            ;
    }

    public static void main(String[] args) {
        int max = 10;
        int [] lengths = {1,1,2,3,4,5,6,7,8};
        //int [] lengths = {4, 2, 8, 4, 5, 4, 5, 4, 3, 2};
        SlackFunctor sf = new SlackFunctor() {
           // public double f(int slack) { return Math.pow(slack, 3); }
           public double f(int slack) { return slack; }
        };

        List<Integer> output = splitWords(lengths, max, sf);
        System.out.println(output);


    }
}

