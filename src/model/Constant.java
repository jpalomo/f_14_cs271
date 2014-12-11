package model;

public class Constant implements Variable {
    private Value val;

    public Constant(Value val) {
        this.val = val;
    }

    public Constant(String val) {
        if (val.equals("0")) {
            this.val = Value.UP;
        } else {
            this.val = Value.DOWN;
        }
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    public Value getValue() {
        return val;
    }

    public void setValue(Value val) {
        this.val = val;
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
        Constant other = (Constant) obj;
        if (val != other.val) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if (val == Value.UP) {
            return "0";
        }
        return "1";
    }

    public static enum Value {
        UP, DOWN
    }

}
