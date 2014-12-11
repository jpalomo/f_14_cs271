package model;

public class Symbol implements Variable {
    private int pos;

    public Symbol(int pos) {
        this.pos = pos;
    }

    public int getPosition() {
        return pos;
    }

    public void setPosition(int pos) {
        this.pos = pos;
    }

    @Override
    public boolean isConstant() {
        return false;
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
        Symbol other = (Symbol) obj;
        if (pos != other.pos) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "x" + pos;
    }
}
