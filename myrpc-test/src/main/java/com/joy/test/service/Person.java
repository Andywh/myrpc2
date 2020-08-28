package com.joy.test.service;

/**
 * Created by Ai Lun on 2020-08-27.
 */
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Person {
    private static final long serialVersionUID = -3475626311941868983L;

    private String firstName;

    private String lastName;

}
