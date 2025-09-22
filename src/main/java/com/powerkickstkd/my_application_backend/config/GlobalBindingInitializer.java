package com.powerkickstkd.my_application_backend.config;

import com.powerkickstkd.my_application_backend.transaction.constants.TransactionMethod;
import com.powerkickstkd.my_application_backend.transaction.constants.TransactionMonth;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;

// NOTE: Make @InitBinder global (so multiple controllers can use it), you should place it inside a @ControllerAdvice class
@ControllerAdvice
public class GlobalBindingInitializer {

    // NOTE: Add an InitBinder to convert trim input strings (@InitBinder - to catch Binding Errors)
    // @InitBinder - Remove leading and trailing white space (pre-process each time we request to our controller)
    @InitBinder
    public void initBinder(WebDataBinder dataBinder){
        // StringTrimmerEditor(true): removes whitespace - leading and trailing
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

        // register this custom editor on the databinder
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);

        // Convert the String to ENUM(TransactionMethod) safely (case-insensitive)
        dataBinder.registerCustomEditor(TransactionMethod.class, new PropertyEditorSupport(){
            @Override
            public void setAsText(String text) {
                // Convert the String to ENUM
                if (text != null) {
                    try{
                        setValue(TransactionMethod.valueOf(text.toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        setValue(null);
                    }
                }  else {
                    setValue(null);
                }

            }
        });

        // Convert the String to ENUM(TransactionMonth) safely (case-insensitive)
        dataBinder.registerCustomEditor(TransactionMonth.class, new PropertyEditorSupport(){
            @Override
            public void setAsText(String text) {
                // Convert the String to ENUM
                if (text != null) {
                    try{
                        setValue(TransactionMethod.valueOf(text.toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        setValue(null);
                    }
                }  else {
                    setValue(null);
                }

            }
        });

    }
}
