# Upgrade Plan: TP Gestion (20260606214905)

- **Generated**: 2026-06-06 21:49:05
- **HEAD Branch**: main
- **HEAD Commit ID**: N/A

## Available Tools

**JDKs**
- JDK 24: `C:\Program Files\Common Files\Oracle\Java\javapath\java.exe` (host available, not target)
- JDK 25: **<TO_BE_INSTALLED>** (required by Step 1 and final validation)
- JDK 17: not available (baseline will be skipped)

**Build Tools**
- Maven: not installed on host
- Maven 3.9.15: **<TO_BE_INSTALLED>** (required by Step 1 and final validation)

## Guidelines

> Note: You can add any specific guidelines or constraints for the upgrade process here if needed, bullet points are preferred.

- Upgrade the project runtime to the latest Java LTS (Java 25).
- Preserve existing application behavior and keep source changes minimal.
- Use Maven for build and validation.

## Options

- Working branch: appmod/java-upgrade-20260606214905
- Run tests before and after the upgrade: true

## Upgrade Goals

- Java 25 LTS runtime

## Technology Stack

| Technology/Dependency | Current | Min Compatible | Why Incompatible |
| --------------------- | ------- | -------------- | ---------------- |
| Java | 17 | 25 | User requested latest LTS runtime upgrade |
| Maven | not installed | 3.9.15 | Required for reliable build tooling on Java 25 |
| maven-compiler-plugin | 3.11.0 | 3.11.0 | Compatible with Java 25; no upgrade required |
| sqlite-jdbc | 3.42.0.0 | 3.42.0.0 | No compatibility issue with Java 25 expected |

## Derived Upgrades

- Java 17 → Java 25: Latest LTS target for runtime and compiler compatibility.
- Install Maven 3.9.15: No host Maven available; Maven is required to execute the project build.
- Keep `maven-compiler-plugin` at 3.11.0: current version is already compatible with Java 25.

## Impact Analysis

### Dependency Changes

| File | Dependency | Current | Action | Target | Reason |
|------|------------|---------|--------|--------|--------|
| pom.xml | `<maven.compiler.source>` | 17 | upgrade | 25 | Upgrade runtime/source compatibility to Java 25 |
| pom.xml | `<maven.compiler.target>` | 17 | upgrade | 25 | Upgrade runtime/target compatibility to Java 25 |

### Source Code Changes

| File | Location | Current | Required Change | Reason |
|------|----------|---------|----------------|--------|
| None | - | - | No source changes required | Project uses plain Java APIs compatible with Java 25 |

### Configuration Changes

| File | Property/Setting | Current | Required Change | Reason |
|------|------------------|---------|----------------|--------|
| pom.xml | maven.compiler properties | 17/17 | 25/25 | Align compiler properties with target JDK |

### CI/CD Changes

- None identified in the repository.

### Risks & Warnings

- **No unit tests present**: `src/test/java` is empty, so final validation will be compile-focused. Mitigation: use `mvn clean test` and `mvn package` to verify full build and runtime packaging.
- **JDK 25 must be installed**: host currently has JDK 24 only. Mitigation: install JDK 25 in Step 1 and use it for validation.
- **No local Maven**: build requires installing Maven 3.9.15 before the upgrade step.

## Upgrade Steps

- Step 1: Setup Environment
  - **Rationale**: Target validation requires a Java 25 runtime and a Maven installation because the host currently lacks a usable Maven and has only Java 24.
  - **Changes to Make**: Install JDK 25 and Maven 3.9.15; verify `java -version` and `mvn -v`.
  - **Verification**: `java -version && mvn -v` with JDK 25 and Maven 3.9.15

- Step 2: Setup Baseline
  - **Rationale**: Baseline is normally required, but Java 17 is not available on the host; this step will be skipped with documentation.
  - **Changes to Make**: None.
  - **Verification**: skipped because base JDK 17 is unavailable.

- Step 3: Upgrade Java runtime to 25 in `pom.xml`
  - **Rationale**: Update Maven compiler settings to target Java 25 runtime and language level.
  - **Changes to Make**: Update `<maven.compiler.source>` and `<maven.compiler.target>` to `25`, and optionally confirm compiler plugin config is still valid.
  - **Verification**: `mvn -q clean test-compile` with JDK 25

- Step 4: CVE Validation & Fix
  - **Rationale**: Validate direct dependency CVEs and ensure no explicit version pins create known security issues during upgrade.
  - **Changes to Make**: Run direct dependency CVE scan, then upgrade any vulnerable direct dependency versions found.
  - **Verification**: `mvn -q clean test-compile` after any dependency updates and re-run `#appmod-validate-cves-for-java`

- Step 5: Final Validation
  - **Rationale**: Ensure the upgrade is complete, the project builds cleanly with Java 25, and all tests (if any) pass.
  - **Changes to Make**: Resolve any compilation or test failures from prior steps.
  - **Verification**: `mvn clean test` with JDK 25
