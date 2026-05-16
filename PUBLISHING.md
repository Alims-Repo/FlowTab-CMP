# Publishing Guide

This document explains how to publish a new version of `flowtab-cmp` to Maven Central.

## Prerequisites

### 1. Sonatype Central Portal account
Create a free account at **https://central.sonatype.com/** and claim the namespace `io.github.alims-repo`.

### 2. Generate a User Token
Go to **Account → View Account → Generate User Token**.  
You'll get a username/token pair — these are your `mavenCentralUsername` / `mavenCentralPassword`.

### 3. GPG signing key
A 4096-bit RSA key pair is already generated and stored in:

| File | Purpose |
|---|---|
| `/tmp/flowtab_private.asc` | Armored private key (keep safe, back up offline) |
| `/tmp/flowtab_public.asc` | Armored public key (already uploaded to keyservers) |
| `~/.gradle/gradle.properties` | Signing key embedded as `signingInMemoryKey` |

The public key fingerprint is `25141275853511121677BCF31B1E6B7C25C963ED` (Key-ID: `25C963ED`).

> **⚠️ Back up `/tmp/flowtab_private.asc` somewhere safe (iCloud, password manager, etc.).**  
> `/tmp` is deleted on reboot. Without the private key, you cannot sign future releases.

---

## Local Setup (`~/.gradle/gradle.properties`)

```properties
# Maven Central credentials (from central.sonatype.com → Account → User Token)
mavenCentralUsername=<your-token-username>
mavenCentralPassword=<your-token-password>

# GPG signing (in-memory — no gpg binary required)
signingInMemoryKeyId=25C963ED
signingInMemoryKeyPassword=
signingInMemoryKey=-----BEGIN PGP PRIVATE KEY BLOCK-----\n...\n-----END PGP PRIVATE KEY BLOCK-----\n
```

The `signingInMemoryKey` value is already populated from the previous `python3 gen_pgp_key.py` run.

---

## Releasing a new version

### Step 1 — Bump the version
Edit `flow-tab/build.gradle.kts`:
```kotlin
coordinates(
    groupId = "io.github.alims-repo",
    artifactId = "flowtab-cmp",
    version = "0.5.8-beta"   // ← bump here
)
```
Also update the version in `README.md` and `docs/index.html`.

### Step 2 — Verify locally
```bash
export JAVA_HOME="/Users/alim/Applications/Android Studio.app/Contents/jbr/Contents/Home"
export PATH="$JAVA_HOME/bin:$PATH"

./gradlew :flow-tab:publishToMavenLocal
```
Check `~/.m2/repository/io/github/alims-repo/` for signed `.asc` files alongside each artifact.

### Step 3 — Publish to Maven Central
```bash
./gradlew :flow-tab:publishAllPublicationsToMavenCentralRepository
```

This uploads a signed ZIP bundle to **https://central.sonatype.com/publishing**.

### Step 4 — Release via the Portal UI
1. Open **https://central.sonatype.com/publishing/deployments**  
2. Find your deployment (status: `PENDING` or `VALIDATED`)  
3. Click **Publish** to promote it to Maven Central

> First-time namespace owners may need to wait for the namespace to be verified (~15 min).

### Step 5 — Tag the release in Git
```bash
git add flow-tab/build.gradle.kts README.md docs/index.html
git commit -m "Release v0.5.7-beta"
git tag v0.5.7-beta
git push origin main --tags
```

---

## Published artifacts

Each release publishes **4 publications** to Maven Central:

| Artifact ID | Description |
|---|---|
| `flowtab-cmp` | KMP metadata + common sources JAR |
| `flowtab-cmp-android` | Android AAR + sources JAR |
| `flowtab-cmp-iosarm64` | iOS physical device KLIB |
| `flowtab-cmp-iossimulatorarm64` | iOS Simulator (Apple Silicon) KLIB |

Every artifact has a corresponding `.asc` signature file.

---

## Consumer usage

```kotlin
// settings.gradle.kts
repositories { mavenCentral() }

// build.gradle.kts
dependencies {
    implementation("io.github.alims-repo:flowtab-cmp:0.5.7-beta")
}
```

---

## GitHub Actions (CI publishing)

See `.github/workflows/publish.yml` for automated publishing on tag push.  
Required secrets: `MAVEN_CENTRAL_USERNAME`, `MAVEN_CENTRAL_PASSWORD`, `SIGNING_KEY_ID`, `SIGNING_KEY`, `SIGNING_PASSWORD`.

