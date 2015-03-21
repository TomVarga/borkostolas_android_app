package hu.tvarga.bor.borkostolas.controller;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecimalDigitsInputFilter implements InputFilter {

    Pattern mPattern;

    public DecimalDigitsInputFilter(int digitsBeforeZero,int digitsAfterZero) {
        mPattern=Pattern.compile("^d{1," + digitsBeforeZero + "}(\\.d{0," + digitsAfterZero + "})?$");
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        CharSequence match = TextUtils.concat(dest.subSequence(0, dstart), source.subSequence(start, end), dest.subSequence(dend, dest.length()));
        Matcher matcher=mPattern.matcher(match);
        if(!matcher.matches())
            return "";
        return null;
    }

}