package configuration.model.engine;

import configuration.model.exceptions.DataTypeMismatchException;

/**
 * Created by Goodwin Chua on 25/03/2016.
 */
public class X87Handler {

    private RegisterList registerList;
    private X87StatusRegister status;
    private X87TagRegister tag;
    private X87ControlRegister control;

    private double[] barrel = new double[8];

    public X87Handler(RegisterList registerList) {
        this.registerList = registerList;
    }

    public void clear() {
        this.status.initializeValue();
        this.control.initializeValue();
        this.tag.initializeValue();
    }

    public void push(String value) throws DataTypeMismatchException {
        int top = this.status.getTop();
        if ( top == 0 ) {
            top = 7;
        } else {
            top--; // go down the stack
        }

        Double converted = Double.parseDouble(value);

        if ( tag.getTag(String.valueOf(top)).equals(X87TagRegister.EMPTY) ) {
            barrel[top] = converted;
            String tagMode = X87TagRegister.VALID;
            if ( converted.isNaN() || converted.isInfinite() ) {
                tagMode = X87TagRegister.SPECIAL;
            } else if ( converted.doubleValue() == 0.0 ) {
                tagMode = X87TagRegister.ZERO;
            }
            this.tag.setTag(String.valueOf(top), tagMode);
        } else {
            barrel[top] = Double.NaN;
            this.tag.setTag(String.valueOf(top), X87TagRegister.SPECIAL);
        }

        int index = 0;
        for ( int i = top; i < 8; i++ ) {
            this.registerList.set("ST" + index, String.valueOf(barrel[i]));
            index++;
        }

        this.status.setBinaryTop(top);

    }

    public String peek() {
        return registerList.get("ST0");
    }

    public String pop() throws DataTypeMismatchException {
        String popped = peek();
        // remove TOP contents
        int top = this.status.getTop();
        barrel[top] = 0;

        if ( top == 7 ) {
            top = 0;
        } else {
            top++; // go up the stack
        }

        int index = 0;
        for ( int i = top; i < 8; i++ ) {
            this.registerList.set("ST" + index, String.valueOf(barrel[i]));
            index++;
        }

        this.registerList.set("ST" + index, "00000000000000000000");

        this.status.setBinaryTop(top);

        return popped;
    }

    public X87StatusRegister status() {
        return this.status;
    }

    public void setStatus(X87StatusRegister register) {
        this.status = register;
    }

    public X87TagRegister tag() {
        return this.tag;
    }

    public void setTag(X87TagRegister register) {
        this.tag = register;
    }

    public X87ControlRegister control() {
        return this.control;
    }

    public void setControl(X87ControlRegister register) {
        this.control = register;
    }
}
