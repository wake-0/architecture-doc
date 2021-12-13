package com.example.architecture.doc;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@AnalyzeClasses(packages = "com.example.architecture.doc..")
public class ArchitectureTest {

        @ArchTest
        private final ArchRule serviceClassesShouldResideInServicesPackage = classes().that()
                        .areAnnotatedWith(Service.class)
                        .should()
                        .resideInAPackage("..services");

        @ArchTest
        private final ArchRule serviceClassesShouldEndWithServiceImpl = classes().that()
                        .areAnnotatedWith(Service.class)
                        .and()
                        .doNotHaveSimpleName("ServiceFacade")
                        .should()
                        .haveSimpleNameEndingWith("ServiceImpl");

        @ArchTest
        private final ArchRule serviceImplShouldBePackagePrivate = classes().that()
                        .haveSimpleNameEndingWith("ServiceImpl")
                        .should()
                        .bePackagePrivate();

        ArchCondition<JavaClass> implementServiceInterface = new ArchCondition<JavaClass>("implement *Service interface") {
                @Override
                public void check(JavaClass item, ConditionEvents events) {
                        final String className = item.getSimpleName();
                        final String expectedInterfaceName = className.substring(0, className.length() - 4);
                        final boolean implementsOwnInterface = item.getAllRawInterfaces()
                                        .stream()
                                        .map(JavaClass::getSimpleName)
                                        .anyMatch(implementedInterface -> implementedInterface.equals(expectedInterfaceName));

                        if (!implementsOwnInterface) {
                                String message = String.format("Class %s does not implement *Service interface", className);
                                events.add(SimpleConditionEvent.violated(item, message));
                        }
                }
        };

        @ArchTest
        private final ArchRule serviceClassesShouldImplementOwnInterface = classes().that()
                        .areAnnotatedWith(Service.class)
                        .and()
                        .doNotHaveSimpleName("ServiceFacade")
                        .should(implementServiceInterface);

        ArchCondition<JavaClass> notDirectlyCallServiceImpl = new ArchCondition<JavaClass>("not directly call *ServiceImpl") {
                @Override
                public void check(JavaClass item, ConditionEvents events) {
                        final String className = item.getSimpleName();
                        item.getAllFields()
                                        .stream()
                                        .filter(field -> field.getType().getName().endsWith("ServiceImpl"))
                                        .forEach(field -> {
                                                String message = String.format("The field %s of class %s does directly call a ServiceImpl", field.getFullName(), className);
                                                events.add(SimpleConditionEvent.violated(item, message));
                                        });
                }
        };

        @ArchTest
        private final ArchRule serviceClassShouldNotBeCalledDirectly = classes().that()
                        .areAnnotatedWith(Service.class)
                        .should(notDirectlyCallServiceImpl);

        @ArchTest
        private final ArchRule repositoryClassesShouldResideInRepositoriesPackage = classes().that()
                        .areAnnotatedWith(Repository.class)
                        .should()
                        .resideInAPackage("..repositories");

        @ArchTest
        private final ArchRule repositoryClassesShouldEndWithRepository = classes().that()
                        .areAnnotatedWith(Repository.class)
                        .should()
                        .haveSimpleNameEndingWith("Repository");

        @ArchTest
        private final ArchRule repositoryClassesShouldBeRepositoryAnnotated = classes().that()
                        .haveSimpleNameEndingWith("Repository")
                        .should()
                        .beAnnotatedWith(Repository.class);

}
