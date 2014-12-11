package model;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Vector {
    private List<Variable> array;
    private int length;

    public Vector(int length) {
        this.length = length;
        array = new ArrayList<Variable>(length);
    }

    public Vector(String str) {
        StringTokenizer tokenizer = new StringTokenizer(str, ",");
        length = tokenizer.countTokens();
        array = new ArrayList<Variable>(length);

        for (int i = 0; i < length; i++) {
            String token = tokenizer.nextToken();

            Variable var = null;
            if (token.startsWith("x")) { // token is a symbol
                var = new Symbol(Integer.parseInt(token.substring(1)));
            } else { // token is a constant
                var = new Constant(token);
            }
            appendVariable(var);
        }
    }

    public int getLength() {
        return length;
    }

    public boolean appendVariable(Variable var) {
        if (array.size() < length) {
            array.add(var);
            return true;
        }
        return false;
    }

    // 1 based
    public Variable getVariable(int pos) {
        if (pos > array.size()) {
            return null;
        }
        return array.get(pos - 1);
    }

    public boolean setVariable(int pos, Variable var) {
        if (pos > array.size()) {
            return false;
        }
        array.set(pos - 1, var);
        return true;
    }

    public Vector copyOf() {
        Vector result = new Vector(length);

        for (int i = 0; i < array.size(); i++) {
            if (array.get(i).isConstant()) {
                result.appendVariable(new Constant(((Constant) array.get(i)).getValue()));
            } else {
                result.appendVariable(new Symbol(((Symbol) array.get(i)).getPosition()));
            }
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < length; i++) {
            if (i < array.size()) {
                sb.append(array.get(i) + ", ");
            } else {
                sb.append("*, ");
            }
        }
        sb.replace(sb.lastIndexOf(","), sb.length(), "]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Vector vec = (Vector) obj;
        if (getLength() != vec.getLength()) {
            return false;
        }
        for (int i = 1; i <= getLength(); i++) {
            Variable var = getVariable(i);
            if (var != null) {
                if (!var.equals(vec.getVariable(i))) {
                    return false;
                }
            } else {
                if (vec.getVariable(i) != null) {
                    return false;
                }
            }
        }
        return true;
    }
}
