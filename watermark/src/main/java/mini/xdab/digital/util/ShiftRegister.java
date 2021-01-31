package mini.xdab.digital.util;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class ShiftRegister {

    @Getter
    protected Long register;

    public ShiftRegister() {
        register = 0L;
    }


    // GETs with return type specified by type of arbitrary size parameter

    public Long get(@NotNull Long size) {
        return (~((~0L) << size)) & register;
    }

    public Integer get(@NotNull Integer size) {
        return get(size.longValue()).intValue();
    }

    public Short get(@NotNull Short size) {
        return get(size.longValue()).shortValue();
    }

    public Byte get(@NotNull Byte size) {
        return get(size.longValue()).byteValue();
    }

    public Boolean get() {
        return (register & 1) > 0;
    }

    // GETs with return type specified by method name

    public Byte getByte() {
        return register.byteValue();
    }

    public Short getShort() {
        return register.shortValue();
    }

    public Integer getInt() {
        return register.intValue();
    }

    public Long getLong() {
        return register;
    }

    // SHIFTs with size determined by parameter type size

    public void shift(@NotNull Boolean bit) {
        register <<= 1;
        if (bit) register |= 1;
    }

    public void shift(@NotNull Byte _byte) {
        register <<= Byte.SIZE;
        register |= _byte;
    }

    public void shift(@NotNull Short _short) {
        register <<= Short.SIZE;
        register |= _short;
    }

    public void shift(@NotNull Integer _int) {
        register <<= Integer.SIZE;
        register |= _int;
    }

    public void shift(@NotNull Long _long) {
        register = _long;
    }

    // SHIFTs with size determined by method name

    public void shiftBit(@NotNull Integer bit) {
        shift((bit != 0));
    }

    public void shiftByte(@NotNull Integer _byte) {
        shift(_byte.byteValue());
    }

    public void shiftShort(@NotNull Integer _short) {
        shift(_short.shortValue());
    }

    public void shiftInt(@NotNull Integer _int) {
        shift(_int);
    }

    // SHIFTs with specified size

    public void shift(@NotNull Integer bits, @NotNull Integer size) {
        register <<= size;
        register |= (bits & (~((~0L) << size)));
    }

}
