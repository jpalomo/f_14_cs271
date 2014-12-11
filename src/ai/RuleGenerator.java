package ai;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RuleGenerator {

    public static final String ZERO = "0";
    public static final String ONE = "1";
    public static final String COST = "1";

    public static void nArrowProblem(int n) {
        if (n < 2) {
            return;
        }

        String[] symbols = new String[n + 1];
        for (int i = 1; i <= n; i++) {
            symbols[i] = "x" + i;
        }
        String[] constant_1 = { ZERO, ZERO, ZERO, ONE, ONE, ZERO, ONE, ONE };
        String[] constant_2 = { ONE, ONE, ONE, ZERO, ZERO, ONE, ZERO, ZERO };

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(n + "_arrow.txt")));
            StringBuilder sb = new StringBuilder();

            int rid = 0;
            // output root line
            sb.append(rid++ + "|");
            for (int times = 0; times < 2; times++) {
                for (int i = 1; i <= n; i++) {
                    sb.append(symbols[i] + ",");
                }
                sb.replace(sb.length() - 1, sb.length(), "|");
            }
            sb.append(0);
            writer.write(sb.toString());
            writer.newLine();

            // output rules
            for (int i = 1; i < n; i++) {
                StringBuilder prev = new StringBuilder();
                StringBuilder next = new StringBuilder();

                for (int j = 1; j < i; j++) {
                    prev.append(symbols[j] + ",");
                }
                for (int j = i + 2; j <= n; j++) {
                    next.append(symbols[j] + ",");
                }

                for (int j = 0; j < 4; j++) {
                    StringBuilder line = new StringBuilder();
                    // append rid
                    line.append(rid++ + "|");
                    // append precondition
                    line.append(prev.toString() + constant_1[j * 2] + "," + constant_1[j * 2 + 1] + ","
                        + next.toString());
                    line.replace(line.length() - 1, line.length(), "|");
                    // append action
                    line.append(prev.toString() + constant_2[j * 2] + "," + constant_2[j * 2 + 1] + ","
                        + next.toString());
                    line.replace(line.length() - 1, line.length(), "|");
                    // append cost
                    line.append(COST);
                    // write rule to file
                    writer.write(line.toString());
                    writer.newLine();
                }
            }

            writer.flush();
            writer.close();

            System.out.println("Done");

        } catch (IOException e) {
            System.out.println("Error");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        nArrowProblem(3);
    }
}
