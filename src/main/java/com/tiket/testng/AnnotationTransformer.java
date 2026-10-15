package com.tiket.testng;

import com.tiket.model.TestDataElement;
import com.tiket.testbase.Configs;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

public class AnnotationTransformer implements IAnnotationTransformer {

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {

        annotation.setRetryAnalyzer(RetryAnalyzer.class);

        List<TestDataElement> testDataElements = Configs.TEST_DATA_ELEMENTS.stream()
                .filter(e -> e.title().equals(testMethod.getName()))
                .toList();

        if (testDataElements.isEmpty())
            annotation.setInvocationCount(0);
    }
}
