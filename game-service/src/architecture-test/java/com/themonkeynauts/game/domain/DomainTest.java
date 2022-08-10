package com.themonkeynauts.game.domain;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

class DomainTest {

    @Test
    public void shouldNotHaveDependencyOnOtherPackages() {
        var classes = new ClassFileImporter().importPackages("com.themonkeynauts.game");

        var ruleDependencyOnApplicationPackages = classes().that().resideInAPackage("..domain..")
                .should().dependOnClassesThat().resideInAPackage("..java..")
                .orShould().dependOnClassesThat().resideInAPackage("..domain..")
                .orShould().dependOnClassesThat().resideInAPackage("..lombok..");

        ruleDependencyOnApplicationPackages.check(classes);
    }
}
