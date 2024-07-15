/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.invoicingapp.tools;

import javafx.util.StringConverter;

public class DoubleStringConverter extends StringConverter<Double> {
    @Override
    public String toString(Double object) {
        return object != null ? object.toString() : "";
    }

    @Override
    public Double fromString(String string){
        try {
            return Double.valueOf(string);
        } catch (NumberFormatException e) {
            return 0.0;
        }

    }
}
