package thsst.calvis.configuration.model.exceptions;

/**
 * Created by Goodwin Chua on 2/4/2016.
 */
public class DuplicateVariableException extends Exception {

    private String label;
    private int lineNumber;

    public DuplicateVariableException(String label) {
        super("Invalid duplicate variable name: " + label);
        this.label = label;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getLabel() {
        return label + ":";
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " at line number: " + lineNumber;
    }

}
