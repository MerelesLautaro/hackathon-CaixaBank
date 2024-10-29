package com.hackathon.bankingapp.Utils;

import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class NullAwareBeanUtils {
    public static void copyNonNullProperties(Object source, Object target) {
        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(source.getClass());
        Set<String> nullPropertyNames = new HashSet<>();

        for (PropertyDescriptor pd : propertyDescriptors) {
            Method getter = pd.getReadMethod();
            try {
                if (getter != null && getter.invoke(source) == null) {
                    nullPropertyNames.add(pd.getName());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String[] nullPropertyArray = new String[nullPropertyNames.size()];
        nullPropertyArray = nullPropertyNames.toArray(nullPropertyArray);

        BeanUtils.copyProperties(source, target, nullPropertyArray);
    }
}
