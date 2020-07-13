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

package primeton.java.io.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * TODO 此处填写 class 信息
 *
 * @author wangwb (mailto:wangwb@primeton.com)
 */

public class ObjectInputStreamGenerator {

    public static final File TARGET_DIR = new File("./generated");

    public static void main(String[] args) throws IOException {
        TARGET_DIR.delete();
        InputStream is = ObjectInputStreamGenerator.class.getResourceAsStream("/java/io/ObjectInputStream.class");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        copy(is, baos);

        ClassReader classReader = new ClassReader(baos.toByteArray());
        ClassWriter classWriter = new ClassWriter(classReader, 0);
        ClassVisitor cv = new MyClassVisitor(classWriter);
        classReader.accept(cv, 0);

        byte[] bytes = classWriter.toByteArray();

        File file = new File(TARGET_DIR, "java/io/ObjectInputStream.class");
        file.getParentFile().mkdirs();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes);
        fos.flush();
        fos.close();
        System.out.println("========= " + TARGET_DIR.getAbsolutePath());
    }

    private static Set set = new HashSet();

    static class MyClassVisitor extends ClassVisitor {
        public MyClassVisitor(ClassVisitor classVisitor) {
            super(Opcodes.ASM8, classVisitor);
        }

        public void visitInnerClass(String name, String outerName, String innerName, int access) {
            super.visitInnerClass(name, outerName, innerName, access);
            if (set.contains(name) || !name.startsWith("java/io/ObjectInputStream")) {
                return;
            }

            System.out.println("-------inner class: " + name);
            set.add(name);
            try {
                ClassReader reader = new ClassReader(name);
                ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                ClassVisitor visitor = new MyClassVisitor(writer);
                reader.accept(visitor, ClassReader.EXPAND_FRAMES);

                byte[] bytes = writer.toByteArray();
                File file = new File(TARGET_DIR, name + ".class");
                file.getParentFile().mkdirs();
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bytes);
                fos.flush();
                fos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
            if (name.equals("resolveClass")) {
                return new MyMethodVisitor(methodVisitor);
            } else {
                return methodVisitor;
            }
        }
    }

    static class MyMethodVisitor extends MethodVisitor {
        public MyMethodVisitor(MethodVisitor methodVisitor) {
            super(Opcodes.ASM8, methodVisitor);
        }

        public void visitInsn(int opcode) {
            if (opcode == Opcodes.ARETURN) {
                mv.visitInsn(Opcodes.DUP); // Return object
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, "primeton/java/io/ObjectInputStreamGuard", "check", "(Ljava/lang/Class;)V", false);
            }
            super.visitInsn(opcode);
        }
    }

    static int copy(InputStream in, OutputStream out) throws IOException {
        int byteCount = 0;
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
            byteCount += bytesRead;
        }
        out.flush();
        return byteCount;
    }

}

/*
 * 修改历史
 * $Log$ 
 */