package com.tetransfiroservices.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;

public class ArchitectureTests {

	private JavaClasses importedClasses = new ClassFileImporter().withImportOption(new DoNotIncludeTests())
	                                                             .importPackages("com.tetransfiroservices");
	@Test
	public void modelShouldNotDependOnApplication() {
		noClasses().that()
		           .resideInAPackage("..model..")
		           .should()
		           .dependOnClassesThat()
		           .resideInAPackage("..application..")
		           .check(importedClasses);
	}

	@Test
	public void modelShouldNotDependOnApi() {
		noClasses().that()
		           .resideInAPackage("..model..")
		           .should()
		           .dependOnClassesThat()
		           .resideInAPackage("..api..")
		           .check(importedClasses);
	}

	@Test
	public void modelShouldNotDependOnAmqp() {
		noClasses().that()
		           .resideInAPackage("..model..")
		           .should()
		           .dependOnClassesThat()
		           .resideInAPackage("..amqp..")
		           .check(importedClasses);
	}

	@Test
	public void amqpShouldNotDependOnApi() {
		noClasses().that()
		           .resideInAPackage("..amqp..")
		           .should()
		           .dependOnClassesThat()
		           .resideInAPackage("..api..")
		           .check(importedClasses);
	}

}
