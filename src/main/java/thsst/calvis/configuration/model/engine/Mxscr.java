package thsst.calvis.configuration.model.engine;

import thsst.calvis.editor.model.Flag;

import java.util.ArrayList;

public class Mxscr extends Register {

    private ArrayList<Flag> flags;

    public Mxscr() {
        this.value = "00000000";
        buildFlags();
        initializeValue();
    }

    @Override
    public void initializeValue() {
        this.value = "00000000";
        for ( Flag flag : flags )
            flag.setValue("0");
    }

    public void buildFlags() {
        this.flags = new ArrayList<>();
        this.flags.add(new Flag("Invalid Operation", getInvalidOperationFlag()));
        this.flags.add(new Flag("Denormal", getDenormalFlag()));
        this.flags.add(new Flag("Divide-by-Zero", getDivideByZeroFlag()));
        this.flags.add(new Flag("Overflow", getOverflowFlag()));
        this.flags.add(new Flag("Underflow", getUnderflowFlag()));
        this.flags.add(new Flag("Precision", getPrecisionFlag()));
        this.flags.add(new Flag("Denormals are Zeroes", getDenormalsAreZeroes()));
        this.flags.add(new Flag("Invalid Operation Mask", getInvalidOperationMask()));
        this.flags.add(new Flag("Denormal Operation Mask", getDenormalOperationMask()));
        this.flags.add(new Flag("Divide-by-Zero Mask", getDivideByZeroMask()));
        this.flags.add(new Flag("Overflow Mask", getOverflowMask()));
        this.flags.add(new Flag("Underflow Mask", getUnderflowMask()));
        this.flags.add(new Flag("Precision Mask", getPrecisionMask()));
        this.flags.add(new Flag("Rounding Control", getRoundingControl()));
        this.flags.add(new Flag("Flush to Zero", getFlushToZero()));
    }

    public void refreshFlags() {
        for ( int i = 0; i < flags.size(); i++ ) {
            String flagName = flags.get(i).getName();
            switch ( flagName ) {
                case "Invalid Operation":
                    flags.get(i).setValue(getInvalidOperationFlag());
                    break;
                case "Denormal":
                    flags.get(i).setValue(getDenormalFlag());
                    break;
                case "Divide-by-Zero":
                    flags.get(i).setValue(getDivideByZeroFlag());
                    break;
                case "Overflow":
                    flags.get(i).setValue(getOverflowFlag());
                    break;
                case "Underflow":
                    flags.get(i).setValue(getUnderflowFlag());
                    break;
                case "Precision":
                    flags.get(i).setValue(getPrecisionFlag());
                    break;
                case "Denormals are Zeroes":
                    flags.get(i).setValue(getDenormalsAreZeroes());
                    break;
                case "Invalid Operation Mask":
                    flags.get(i).setValue(getInvalidOperationMask());
                    break;
                case "Denormal Operation Mask":
                    flags.get(i).setValue(getDenormalOperationMask());
                    break;
                case "Divide-by-Zero Mask":
                    flags.get(i).setValue(getDivideByZeroMask());
                    break;
                case "Overflow Mask":
                    flags.get(i).setValue(getOverflowMask());
                    break;
                case "Underflow Mask":
                    flags.get(i).setValue(getUnderflowMask());
                    break;
                case "Precision Mask":
                    flags.get(i).setValue(getPrecisionMask());
                    break;
                case "Rounding Control":
                    flags.get(i).setValue(getRoundingControl());
                    break;
                case "Flush to Zero":
                    flags.get(i).setValue(getFlushToZero());
                    break;
                default:
            }
        }
    }

    public ArrayList<Flag> getFlagList() {
        return this.flags;
    }

    public String getFlagIndex(int index) {
        int hex = Integer.parseInt(this.value, 16);
        String val = Integer.toBinaryString(hex);
        int missingZeroes = 32 - val.length();

        //zero extend
        for ( int k = 0; k < missingZeroes; k++ ) {
            val = "0" + val;
        }
        return val.charAt(32 - 1 - index) + "";
    }

    public void setFlagIndex(int index, String a) {
        if ( a.equals("0") || a.equals("1") ) {
            int hex = Integer.parseInt(this.value, 16);
            String val = Integer.toBinaryString(hex);
            int missingZeroes = 32 - val.length();

            //zero extend
            for ( int k = 0; k < missingZeroes; k++ ) {
                val = "0" + val;
            }

            char[] extended = val.toCharArray();
            extended[32 - 1 - index] = a.charAt(0);
            val = new String(extended);
            String hexValue = Integer.toHexString(Integer.parseInt(val, 2));
            int zeroExtendHexValue = 8 - hexValue.length();

            //zero extend
            for ( int k = 0; k < zeroExtendHexValue; k++ ) {
                hexValue = "0" + hexValue;
            }

            this.value = hexValue.toUpperCase();
        } else {
            
        }
    }

    public String getInvalidOperationFlag() {
        return getFlagIndex(0);
    }

    public String getDenormalFlag() {
        return getFlagIndex(1);
    }

    public String getDivideByZeroFlag() {
        return getFlagIndex(2);
    }

    public String getOverflowFlag() {
        return getFlagIndex(3);
    }

    public String getUnderflowFlag() {
        return getFlagIndex(4);
    }

    public String getPrecisionFlag() {
        return getFlagIndex(5);
    }

    public String getDenormalsAreZeroes() {
        return getFlagIndex(6);
    }

    public String getInvalidOperationMask() {
        return getFlagIndex(7);
    }

    public String getDenormalOperationMask() {
        return getFlagIndex(8);
    }

    public String getDivideByZeroMask() {
        return getFlagIndex(9);
    }

    public String getOverflowMask() {
        return getFlagIndex(10);
    }

    public String getUnderflowMask() {
        return getFlagIndex(11);
    }

    public String getPrecisionMask() {
        return getFlagIndex(12);
    }

    public String getRoundingControl() {
        return getFlagIndex(13);
    }

    public String getFlushToZero() {
        return getFlagIndex(15);
    }

    public void setInvalidOperationFlag(String value) {
        setFlagIndex(0, value);
        setFlag("Invalid Operation", value);
    }

    public void setDenormalFlag(String value) {
        setFlagIndex(1, value);
        setFlag("Denormal", value);
    }

    public void setDivideByZeroFlag(String value) {
        setFlagIndex(2, value);
        setFlag("Divide-by-Zero", value);
    }

    public void setOverflowFlag(String value) {
        setFlagIndex(3, value);
        setFlag("Overflow", value);
    }

    public void setUnderflowFlag(String value) {
        setFlagIndex(4, value);
        setFlag("Underflow", value);
    }

    public void setPrecisionFlag(String value) {
        setFlagIndex(5, value);
        setFlag("Precision", value);
    }

    public void setDenormalsAreZeroes(String value) {
        setFlagIndex(6, value);
        setFlag("Denormals are Zeroes", value);
    }

    public void setInvalidOperationMask(String value) {
        setFlagIndex(7, value);
        setFlag("Invalid Operation Mask", value);
    }

    public void getDenormalOperationMask(String value) {
        setFlagIndex(8, value);
        setFlag("Denormal Operation Mask", value);
    }

    public void setDivideByZeroMask(String value) {
        setFlagIndex(9, value);
        setFlag("Divide-by-Zero Mask", value);
    }

    public void setOverflowMask(String value) {
        setFlagIndex(10, value);
        setFlag("Overflow Mask", value);
    }

    public void setUnderflowMask(String value) {
        setFlagIndex(11, value);
        setFlag("Underflow Mask", value);
    }

    public void setPrecisionMask(String value) {
        setFlagIndex(12, value);
        setFlag("Precision Mask", value);
    }

    public void setRoundingControl(String value) {
        setFlagIndex(13, value);
        setFlag("Rounding Control", value);
    }

    public void setFlushToZero() {
        setFlagIndex(15, value);
        setFlag("Flush to Zero", value);
    }

    private void setFlag(String name, String value) {
        for ( int i = 0; i < flags.size(); i++ ) {
            if ( flags.get(i).getName().equals(name) ) {
                flags.get(i).setValue(value);
                break;
            }
        }
    }

}
