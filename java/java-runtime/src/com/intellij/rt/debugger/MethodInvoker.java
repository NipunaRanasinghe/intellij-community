// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.rt.debugger;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.invoke.WrongMethodTypeException;
import java.lang.reflect.Array;
import java.util.Arrays;

@SuppressWarnings({"SSBasedInspection", "unused"})
public final class MethodInvoker {
  // TODO: may leak objects here
  static ThreadLocal<Object> returnValue = new ThreadLocal<>();

  public static Object invoke0(MethodHandles.Lookup lookup, Class<?> cls, Object obj, String nameAndDescriptor, ClassLoader loader)
    throws Throwable {
    return invokeInternal(lookup, cls, obj, nameAndDescriptor, loader, true, new Object[]{});
  }

  public static Object invoke1(MethodHandles.Lookup lookup,
                               Class<?> cls,
                               Object obj,
                               String nameAndDescriptor,
                               ClassLoader loader,
                               Object arg1) throws Throwable {
    if (arg1 == null || arg1 instanceof Object[]) { // do not wrap Object arrays again
      return invokeInternal(lookup, cls, obj, nameAndDescriptor, loader, false, (Object[])arg1);
    }
    else {
      return invokeInternal(lookup, cls, obj, nameAndDescriptor, loader, true, new Object[]{arg1});
    }
  }

  public static Object invoke2(MethodHandles.Lookup lookup,
                               Class<?> cls,
                               Object obj,
                               String nameAndDescriptor,
                               ClassLoader loader,
                               Object arg1,
                               Object arg2) throws Throwable {
    return invokeInternal(lookup, cls, obj, nameAndDescriptor, loader, true, new Object[]{arg1, arg2});
  }

  public static Object invoke3(MethodHandles.Lookup lookup,
                               Class<?> cls,
                               Object obj,
                               String nameAndDescriptor,
                               ClassLoader loader,
                               Object arg1,
                               Object arg2,
                               Object arg3) throws Throwable {
    return invokeInternal(lookup, cls, obj, nameAndDescriptor, loader, true, new Object[]{arg1, arg2, arg3});
  }

  public static Object invoke4(MethodHandles.Lookup lookup,
                               Class<?> cls,
                               Object obj,
                               String nameAndDescriptor,
                               ClassLoader loader,
                               Object arg1,
                               Object arg2,
                               Object arg3,
                               Object arg4) throws Throwable {
    return invokeInternal(lookup, cls, obj, nameAndDescriptor, loader, true, new Object[]{arg1, arg2, arg3, arg4});
  }

  public static Object invoke5(MethodHandles.Lookup lookup,
                               Class<?> cls,
                               Object obj,
                               String nameAndDescriptor,
                               ClassLoader loader,
                               Object arg1,
                               Object arg2,
                               Object arg3,
                               Object arg4,
                               Object arg5) throws Throwable {
    return invokeInternal(lookup, cls, obj, nameAndDescriptor, loader, true, new Object[]{arg1, arg2, arg3, arg4, arg5});
  }

  public static Object invoke6(MethodHandles.Lookup lookup,
                               Class<?> cls,
                               Object obj,
                               String nameAndDescriptor,
                               ClassLoader loader,
                               Object arg1,
                               Object arg2,
                               Object arg3,
                               Object arg4,
                               Object arg5,
                               Object arg6) throws Throwable {
    return invokeInternal(lookup, cls, obj, nameAndDescriptor, loader, true, new Object[]{arg1, arg2, arg3, arg4, arg5, arg6});
  }

  public static Object invoke7(MethodHandles.Lookup lookup,
                               Class<?> cls,
                               Object obj,
                               String nameAndDescriptor,
                               ClassLoader loader,
                               Object arg1,
                               Object arg2,
                               Object arg3,
                               Object arg4,
                               Object arg5,
                               Object arg6,
                               Object arg7) throws Throwable {
    return invokeInternal(lookup, cls, obj, nameAndDescriptor, loader, true,
                          new Object[]{arg1, arg2, arg3, arg4, arg5, arg6, arg7});
  }

  public static Object invoke8(MethodHandles.Lookup lookup,
                               Class<?> cls,
                               Object obj,
                               String nameAndDescriptor,
                               ClassLoader loader,
                               Object arg1,
                               Object arg2,
                               Object arg3,
                               Object arg4,
                               Object arg5,
                               Object arg6,
                               Object arg7,
                               Object arg8) throws Throwable {
    return invokeInternal(lookup, cls, obj, nameAndDescriptor, loader, true,
                          new Object[]{arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8});
  }

  public static Object invoke9(MethodHandles.Lookup lookup,
                               Class<?> cls,
                               Object obj,
                               String nameAndDescriptor,
                               ClassLoader loader,
                               Object arg1,
                               Object arg2,
                               Object arg3,
                               Object arg4,
                               Object arg5,
                               Object arg6,
                               Object arg7,
                               Object arg8,
                               Object arg9) throws Throwable {
    return invokeInternal(lookup, cls, obj, nameAndDescriptor, loader, true,
                          new Object[]{arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9});
  }

  public static Object invoke10(MethodHandles.Lookup lookup,
                                Class<?> cls,
                                Object obj,
                                String nameAndDescriptor,
                                ClassLoader loader,
                                Object arg1,
                                Object arg2,
                                Object arg3,
                                Object arg4,
                                Object arg5,
                                Object arg6,
                                Object arg7,
                                Object arg8,
                                Object arg9,
                                Object arg10) throws Throwable {
    return invokeInternal(lookup, cls, obj, nameAndDescriptor, loader, true,
                          new Object[]{arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10});
  }

  public static Object invoke(MethodHandles.Lookup lookup,
                              Class<?> cls,
                              Object obj,
                              String nameAndDescriptor,
                              ClassLoader loader,
                              boolean vararg,
                              Object[] args) throws Throwable {
    return invokeInternal(lookup, cls, obj, nameAndDescriptor, loader, true, args);
  }

  private static Object invokeInternal(MethodHandles.Lookup lookup,
                                       Class<?> cls,
                                       Object obj,
                                       String nameAndDescriptor,
                                       ClassLoader loader,
                                       boolean wrapped,
                                       Object[] args) throws Throwable {
    try {
      int separatorIndex = nameAndDescriptor.indexOf(';');
      String name = nameAndDescriptor.substring(0, separatorIndex);
      String descriptor = nameAndDescriptor.substring(separatorIndex + 1);

      MethodType mt = MethodType.fromMethodDescriptorString(descriptor, loader);
      MethodHandle method;
      if ("<init>".equals(name)) {
        method = lookup.findConstructor(cls, mt);
      }
      else if (obj != null) {
        method = lookup.findVirtual(cls, name, mt);
      }
      else {
        method = lookup.findStatic(cls, name, mt);
      }

      boolean vararg = method.isVarargsCollector();
      if (obj != null) {
        method = method.bindTo(obj);
        if (vararg) {
          method = method.asVarargsCollector(mt.parameterType(mt.parameterCount() - 1));
        }
      }

      Object result;

      int parameterCount = mt.parameterCount();
      Class<?> lastParameterType = parameterCount > 0 ? mt.parameterType(parameterCount - 1) : null;
      if (wrapped && vararg && parameterCount > 0) {
        if (args.length == parameterCount - 1 && lastParameterType.isArray()) {
          // add an empty array if empty vararg parameter is passed
          args = Arrays.copyOf(args, parameterCount);
          args[parameterCount - 1] = Array.newInstance(lastParameterType.getComponentType(), 0);
        }
      }
      if (wrapped) {
        if (vararg && args.length == parameterCount && lastParameterType.isAssignableFrom(args[args.length - 1].getClass())) {
          result = method.asFixedArity().invokeWithArguments(args);
        }
        else {
          result = method.invokeWithArguments(args);
        }
      }
      else {
        result = method.asFixedArity().invoke(args);
      }
      returnValue.set(result);
      return result;
    }
    catch (WrongMethodTypeException | ClassCastException e) {
      e.printStackTrace();
      throw e;
    }
  }
}
