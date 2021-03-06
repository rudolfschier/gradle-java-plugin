/*
 * Copyright (c) 2019 ekino (https://www.ekino.com/)
 */

package com.ekino.oss.gradle.plugin

import org.gradle.api.JavaVersion
import org.gradle.api.internal.tasks.testing.junitplatform.JUnitPlatformTestFramework
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.DisabledOnJre
import org.junit.jupiter.api.condition.JRE.JAVA_8
import strikt.api.expect
import strikt.api.expectThat
import strikt.assertions.containsExactly
import strikt.assertions.isA
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import strikt.assertions.isNotEmpty
import strikt.assertions.isTrue

class JavaPluginTest {

  @Test
  fun shouldContainsDefaultPlugins() {
    val project = ProjectBuilder.builder().build()

    expectThat(project.plugins).isEmpty()

    project.plugins.apply("com.ekino.oss.gradle.plugin.java")

    expect {
      that(project.pluginManager.hasPlugin("java")).isTrue()
      that(project.pluginManager.hasPlugin("org.unbroken-dome.test-sets")).isTrue()
      that(project.pluginManager.hasPlugin("maven-publish")).isTrue()
    }
    expectThat(project.getTasksByName("aggregateJunitReports", false)).isNotEmpty()
  }

  @Test
  @DisabledOnJre(JAVA_8)
  fun shouldTargetJava11ByDefault() {
    val project = ProjectBuilder.builder().build()

    project.plugins.apply("com.ekino.oss.gradle.plugin.java")

    val properties = project.properties
    expectThat(properties["sourceCompatibility"]) isEqualTo JavaVersion.VERSION_11
    expectThat(properties["targetCompatibility"]) isEqualTo JavaVersion.VERSION_11
  }

  @Test
  fun shouldSetBuildAsDefaultTask() {
    val project = ProjectBuilder.builder().build()

    project.plugins.apply("com.ekino.oss.gradle.plugin.java")

    val defaultTasks = project.defaultTasks
    expectThat(defaultTasks).containsExactly("build")
  }

  @Test
  fun shouldSetupJunit5() {
    val project = ProjectBuilder.builder().build()

    project.plugins.apply("com.ekino.oss.gradle.plugin.java")

    val testTasks = project.tasks.withType(org.gradle.api.tasks.testing.Test::class.java)
    expectThat(testTasks.getByName("test").testFramework).isA<JUnitPlatformTestFramework>()
  }
}
