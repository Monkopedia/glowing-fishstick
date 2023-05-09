package com.ttlock.bl.sdk.api;

/**
 * Created on  2019/6/11 0011 14:27
 *
 * @author theodore_hu
 */
public class ParamInvalidException extends Exception {
    public ParamInvalidException(){
        super();
    }

    @Override
    public String toString(){
        return "parameter value invalid exception";
    }
}
