package com.joy.test.service;

import com.joy.rpc.common.annotation.RpcService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ai Lun on 2020-08-27.
 */
@RpcService(PersonService.class)
@Component
public class PersonServiceImpl implements PersonService{

    @Override
    public List<Person> getTestPerson(String name, int num) {
        List<Person> persons = new ArrayList<>(num);
        for (int i = 0; i < num; ++i) {
            persons.add(new Person(Integer.toString(i), name));
        }
        return persons;
    }

}
