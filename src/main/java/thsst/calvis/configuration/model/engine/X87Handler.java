package thsst.calvis.configuration.model.engine;

import thsst.calvis.configuration.model.exceptions.DataTypeMismatchException;

/**
 * Created by Goodwin Chua on 25/03/2016.
 */
public class X87Handler {

    private final int LEFT = 0;
    private final int RIGHT = 1;

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
        this.barrel = new double[8];
    }

    public void push(String value) throws DataTypeMismatchException {
        int top = this.status.getTop();
        if ( top == 0 ) {
            top = 7;
        } else {
            top--; // go down the stack
        }

        rotateBarrel(LEFT);

        Double converted = Double.parseDouble(value);

        if ( tag.getTag(String.valueOf(top)).equals(X87TagRegister.EMPTY) ) {
            barrel[barrel.length - 1] = converted;
            String tagMode = X87TagRegister.VALID;
            if ( converted.isNaN() || converted.isInfinite() ) {
                tagMode = X87TagRegister.SPECIAL;
            } else if ( converted.doubleValue() == 0.0 ) {
                tagMode = X87TagRegister.ZERO;
            }
            this.tag.setTag(String.valueOf(top), tagMode);
        } else {
            barrel[barrel.length - 1] = Double.NaN;
            this.status.setStackFaultFlag();
            this.status.setInvalidOperationFlag();
            this.tag.setTag(String.valueOf(top), X87TagRegister.SPECIAL);
        }

        int index = 0;
        for ( int i = 7; i >= 0; i-- ) {
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

        if ( this.tag.getTag(String.valueOf(top)).equals(X87TagRegister.EMPTY) ) {
            // underflow
            this.status.setStackFaultFlag();
            this.status.setInvalidOperationFlag();
            this.status.set("C1", '0');
        }

        rotateBarrel(RIGHT);
        this.tag.setTag(String.valueOf(top), X87TagRegister.EMPTY);

        if ( top == 7 ) {
            top = 0;
        } else {
            top++; // go up the stack
        }

        int index = 0;
        for ( int i = 7; i >= 0; i-- ) {
            this.registerList.set("ST" + index, String.valueOf(barrel[i]));
            index++;
        }

        this.status.setBinaryTop(top);

        return popped;
    }

    public void setStackValue(String stIndex, String value) {
        double val = Double.parseDouble(value);
        int barrelIndex = 7 - Integer.parseInt(stIndex);

        barrel[barrelIndex] = val;

    }

    public void rotateBarrel(int direction) {
        if ( direction == LEFT ) {
            // to the left
            double a = barrel[0];
            for ( int i = 0; i < barrel.length - 1; i++ ) {
                barrel[i] = barrel[i + 1];
            }
            barrel[barrel.length-1] = a;
        } else { // to the right
            double a = barrel[barrel.length-1];
            for ( int i = barrel.length - 1; i > 0; i-- ) {
                barrel[i] = barrel[i - 1];
            }
            barrel[0] = a;
        }

    }

    public void refreshST() throws DataTypeMismatchException {
        int index = 0;
        for ( int i = 7; i >= 0; i-- ) {
            registerList.set("ST" + index, String.valueOf(barrel[i]));
            index++;
        }
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
