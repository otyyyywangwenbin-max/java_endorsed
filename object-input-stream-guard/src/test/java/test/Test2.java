/*******************************************************************************
 * $Header$
 * $Revision$
 * $Date$
 *
 *==============================================================================
 *
 * Copyright (c) 2001-2017 Primeton Technologies, Ltd.
 * All rights reserved.
 * 
 * Created on Jul 13, 2020
 *******************************************************************************/

package test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * TODO 此处填写 class 信息
 *
 * @author wangwb (mailto:wangwb@primeton.com)
 */

public class Test2 {
    public static void main(String[] args) throws Throwable {
        System.out.println(System.getProperty("java.endorsed.dirs"));

        User user = new User();
        user.setId("id-01");
        user.setName("name-01");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(user);

        byte[] bytes = baos.toByteArray();
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        User user2 = (User) ois.readObject();
        System.out.println(user2.getName());
    }
}

/*
 * 修改历史
 * $Log$ 
 */