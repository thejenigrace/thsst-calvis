package thsst.calvis.configuration.model.engine;

import thsst.calvis.editor.model.Flag;

import java.util.ArrayList;

public class EFlags extends Register {

    private ArrayList<Flag> flags;

    public EFlags() {
        this.value = "00000002";
        buildFlags();
        initializeValue();
    }

    @Override
    public void initializeValue() {
        this.value = "00000002";
        for ( Flag f : flags ) {
            f.setFlagValue("0");
        }
    }

    public void buildFlags() {
        this.flags = new ArrayList<>();
        this.flags.add(new Flag("Carry", getCarryFlag()));
        this.flags.add(new Flag("Parity", getParityFlag()));
        this.flags.add(new Flag("Auxiliary", getAuxiliaryFlag()));
        this.flags.add(new Flag("Zero", getZeroFlag()));
        this.flags.add(new Flag("Sign", getSignFlag()));
        this.flags.add(new Flag("Overflow", getOverflowFlag()));
        this.flags.add(new Flag("Interrupt", getInterruptFlag()));
        this.flags.add(new Flag("Direction", getDirectionFlag()));
    }

    public void refreshFlags() {
        for ( int i = 0; i < flags.size(); i++ ) {
            String flagName = flags.get(i).getName();
            switch ( flagName ) {
                case "Carry":
                    flags.get(i).setFlagValue(getCarryFlag());
                    break;
                case "Sign":
                    flags.get(i).setFlagValue(getSignFlag());
                    break;
                case "Overflow":
                    flags.get(i).setFlagValue(getOverflowFlag());
                    break;
                case "Zero":
                    flags.get(i).setFlagValue(getZeroFlag());
                    break;
                case "Parity":
                    flags.get(i).setFlagValue(getParityFlag());
                    break;
                case "Auxiliary":
                    flags.get(i).setFlagValue(getAuxiliaryFlag());
                    break;
                case "Direction":
                    flags.get(i).setFlagValue(getDirectionFlag());
                    break;
                case "Interrupt":
                    flags.get(i).setFlagValue(getInterruptFlag());
                    break;
                default:
            }
        }
    }

    public ArrayList<Flag> getFlagList() {
        return this.flags;
    }

    public char getFlagIndex(int index) {
        int hex = Integer.parseInt(this.value, 16);
        String val = Integer.toBinaryString(hex);
        int missingZeroes = 32 - val.length();

        //zero extend
        for ( int k = 0; k < missingZeroes; k++ ) {
            val = "0" + val;
        }
        return val.charAt(32 - 1 - index);
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
            System.out.println("Invalid flag value");
        }
    }

    public String getCarryFlag() {
        return getFlagIndex(0) + "";
    }

    public String getParityFlag() {
        return getFlagIndex(2) + "";
    }

    public String getAuxiliaryFlag() {
        return getFlagIndex(4) + "";
    }

    public String getZeroFlag() {
        return getFlagIndex(6) + "";
    }

    public String getSignFlag() {
        return getFlagIndex(7) + "";
    }

    public String getOverflowFlag() {
        return getFlagIndex(11) + "";
    }

    public String getDirectionFlag() {
        return getFlagIndex(10) + "";
    }

    public String getInterruptFlag() {
        return getFlagIndex(9) + "";
    }

    public void setCarryFlag(String value) {
        setFlagIndex(0, value);
        setFlag("Carry", value);
    }

    public void setParityFlag(String value) {
        setFlagIndex(2, value);
        setFlag("Parity", value);
    }

    public void setAuxiliaryFlag(String value) {
        setFlagIndex(4, value);
        setFlag("Auxiliary", value);
    }

    public void setZeroFlag(String value) {
        setFlagIndex(6, value);
        setFlag("Zero", value);
    }

    public void setSignFlag(String value) {
        setFlagIndex(7, value);
        setFlag("Sign", value);
    }

    public void setOverflowFlag(String value) {
        setFlagIndex(11, value);
        setFlag("Overflow", value);
    }

    public void setDirectionFlag(String value) {
        setFlagIndex(10, value);
        setFlag("Direction", value);
    }

    public void setInterruptFlag(String value) {
        setFlagIndex(9, value);
        setFlag("Interrupt", value);
    }

    private void setFlag(String name, String value) {
        for ( int i = 0; i < flags.size(); i++ ) {
            if ( flags.get(i).getName().equals(name) ) {
                flags.get(i).setFlagValue(value);
                break;
            }
        }
    }

}
