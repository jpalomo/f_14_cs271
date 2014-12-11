package ai;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import model.Rule;

/**
 * This class is responsible for reading a source input file of
 * rules given in a specific format and generates the necessary 
 * representations of the rules to be used in the algorithm.
 *
 *@author Feng Palomo 
 */
public class Rules {
    private static final String FILE_NAME = "rule_three.txt";

    private Map<Integer, Rule> rules;

    public Rules(String fileName){
    	createRules(fileName);
    }

    public Rules(){
    	createRules(FILE_NAME); 
    } 

    private void createRules(String fileName) {
        rules = new HashMap<Integer, Rule>();
        try {
            Scanner input = new Scanner(new File(fileName));
            while (input.hasNextLine()) {
                String line = input.nextLine();
                Rule rule = new Rule(line);
                rules.put(rule.getRuleId(), rule);
            }
            input.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int getNumRules() {
        return rules.size();
    }

    public Rule getEmptyMove() {
        return rules.get(0);
    }

    public Rule getRuleById(int rid) {
        if (rid > 0 && rid < rules.size()) {
            return rules.get(rid);
        }
        return null;
    }
}
