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

package primeton.java.io;

/**
 * TODO 此处填写 class 信息
 *
 * @author wangwb (mailto:wangwb@primeton.com)
 */

public class ObjectInputStreamGuard {
    public static void check(Class clazz) {
        System.out.println("66666:::: " + clazz);
        // TODO 用classname判断, 不能用class判断
        if ("test.User".equals(clazz.getName())) {
            throw new RuntimeException("Black class");
        }
    }
}

/*
 * 修改历史
 * $Log$ 
 */