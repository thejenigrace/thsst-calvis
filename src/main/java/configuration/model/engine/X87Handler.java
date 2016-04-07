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

    public X87Handler(RegisterList registerList) {
        this.registerList = registerList;
        this.status = new X87StatusRegister();
        this.tag = new X87TagRegister();
        this.control = new X87ControlRegister();
    }

    public void clear() {
        this.status.clear();
        this.control.clear();
        this.tag.clear();
    }

    public void push(String value) throws DataTypeMismatchException {
        int top = this.status.getTop();
        if ( top == 8 ) {
            // stack overflow
            this.status.set("C1", '1');
        } else {
            // okay no problem
            for ( int i = top; i > 0; i-- ) {
                String beforeValue = this.registerList.get("ST" + (i - 1));
                this.registerList.set("ST" + i, beforeValue);
            }
            this.registerList.set("ST0", value);

            this.status.incrementTop();
        }
    }

    public String peek() {
        return registerList.get("ST0");
    }

    public String pop() throws DataTypeMismatchException {
        String popped = peek();
        int top = this.status.getTop();
        if ( top < 0 ) {
            // stack underflow
            this.status.set("C1", '0');
        } else {
            // no problem
            for ( int i = 0; i < top; i++ ) {
                String beforeValue = this.registerList.get("ST" + (i + 1));
                this.registerList.set("ST" + i, beforeValue);
            }
            this.status.decrementTop();
        }
        return popped;
    }

    public int getRoundingMode() {
        return this.control.getRoundingMode();
    }

    public X87StatusRegister status() {
        return this.status;
    }

    public X87TagRegister tag() {
        return this.tag;
    }

    public X87ControlRegister control() {
        return this.control;
    }
}
